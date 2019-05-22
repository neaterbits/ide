package com.neaterbits.ide.component.java.language;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.java.JavaProgramModel;
import com.neaterbits.compiler.java.JavaTypes;
import com.neaterbits.compiler.java.bytecode.JavaBytecodeFormat;
import com.neaterbits.compiler.java.bytecode.JavaClassLibs;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.ast.objects.BuildAndResolve;
import com.neaterbits.compiler.resolver.ast.objects.ProgramLoader;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectProgramModel;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.FileSystemFileSpec;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.resource.FileSystemResourcePath;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.NamespaceResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResource;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.component.common.language.compilercommon.CompilerSourceFileModel;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public final class JavaLanguage extends JavaBuildableLanguage implements CompileableLanguage, ParseableLanguage {

	private final BytecodeFormat bytecodeFormat;
	
	public JavaLanguage() {
		this.bytecodeFormat = new JavaBytecodeFormat();
	}
	
	private static NamespaceResource getNamespace(SourceFileResourcePath sourceFile) {
		
		final NamespaceResource namespaceResource;
		
		if (sourceFile.getFromLast(1) instanceof NamespaceResource) {
			namespaceResource = (NamespaceResource)sourceFile.getFromLast(1);
		}
		else {
			
			final SourceFolderResource sourceFolder = (SourceFolderResource)sourceFile.getFromLast(1);
			
			namespaceResource = SourceFileScanner.getNamespaceResource(sourceFolder.getFile(), sourceFile.getFile()).getNamespace();
		}
		
		return namespaceResource;
	}

	
	public static File getSystemJarFilePath(String libName) {
		
		final String jreDir = System.getProperty("java.home");
		
		return new File(jreDir + "/lib/" + libName);
	}
	
	@Override
	public ClassLibs getSystemLibraries() {

		final String jreDir = System.getProperty("java.home");
		
		System.out.println("## jre dir " + jreDir);
		
		final List<String> fileNames = Arrays.asList("rt.jar", "charsets.jar");
		
		final List<String> list = fileNames.stream()
			.map(fileName -> jreDir + "/lib/" + fileName)
			.collect(Collectors.toList());
		
		try {
			return new JavaClassLibs(list);
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public TypeName getTypeName(SourceFileResourcePath sourceFile) {

		return new TypeName(
				getNamespace(sourceFile).getNamespace(),
				null,
				classNameStringFromSourceFile(sourceFile.getFile()));
	}
	
	@Override
	public TypeName getTypeName(String namespace, String name) {

		final String [] parts = namespace != null && !namespace.isEmpty()
					? Strings.split(namespace, '.')
					: null; 
					
		return new TypeName(parts, null, name);
	}
	
	@Override
	public String getNamespaceString(TypeName typeName) {
		
		Objects.requireNonNull(typeName);
		Objects.requireNonNull(typeName.getNamespace());
		
		return Strings.join(typeName.getNamespace(), '.');
	}
	

	@Override
	public String getCompleteNameString(TypeName typeName) {

		Objects.requireNonNull(typeName);
		
		return typeName.join('.');
	}

	@Override
	public String getBinaryName(TypeName typeName) {
		return typeName.getName() + ".class";
	}

	private static String classNameStringFromSourceFile(File file) {
		
		final String name = file.getName();
		
		return name.substring(0, name.length() - ".java".length());
	}

	@Override
	public Set<TypeName> getTypesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException {
		return getTypesFromJarFile(compiledModuleFileResourcePath);
	}
	
	@Override
	public Set<TypeName> getTypesFromLibraryFile(LibraryResourcePath libraryResourcePath) throws IOException {
		return getTypesFromJarFile(libraryResourcePath);
	}
	
	@Override
	public Set<TypeName> getTypesFromSystemLibraryFile(DependencyFile systemLibraryPath) throws IOException {

		return getTypesFromJarFile(systemLibraryPath.getFile());
	}

	private Set<TypeName> getTypesFromJarFile(FileSystemResourcePath jarFileResourcePath) throws IOException {
		return getTypesFromJarFile(jarFileResourcePath.getFile());
	}

	private Set<TypeName> getTypesFromJarFile(File file) throws IOException {

		return bytecodeFormat.getTypesFromLibraryFile(file);
	}
	
	private Charset getCharset() {
	    
	    return Charset.defaultCharset();
	}

	@Override
	public boolean canReadCodeMapFromCompiledCode() {
		return true;
	}

	@Override
	public Map<SourceFileResourcePath, SourceFileModel> parseModule(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> files,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException {

		final Java8AntlrParser parser = new Java8AntlrParser(false);

		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels = new HashMap<>(files.size());
		final Map<SourceFileResourcePath, ASTParsedFile> compilationUnits = new HashMap<>(files.size());
		
		final ObjectProgramModel programModel = new JavaProgramModel();
		
		for (SourceFileResourcePath path : files) {
		
			final File file = path.getFile();
			
			try (FileInputStream inputStream = new FileInputStream(file)) {
			
				final ASTParsedFile parsedFile = BuildAndResolve.parseFile(
						parser,
						inputStream,
						getCharset(),
						new FileSystemFileSpec(file),
						resolvedTypes);
				
				final SourceFileModel sourceFileModel = new CompilerSourceFileModel(
						programModel,
						parsedFile.getParsed(),
						parsedFile.getErrors(),
						resolvedTypes,
						-1,
						codeMap);

				compilationUnits.put(path, parsedFile);
				sourceFileModels.put(path, sourceFileModel);
			}
		}

		BuildAndResolve.resolveParsedModule(
				modulePath,
				dependencies,
				ModuleResourcePath::getModuleId,
				ModuleResourcePath::getFile,
				compilationUnits.values(),
				programModel,
				JavaTypes.getBuiltinTypeRefs(),
				resolvedTypes);
		
		return sourceFileModels;
	}

	@Override
	public SourceFileModel parseAndResolveChangedFile(
			SourceFileResourcePath sourceFilePath,
			String string,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) {

		final Java8AntlrParser parser = new Java8AntlrParser(false);
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(string.getBytes());
		
		final SourceFileModel sourceFileModel;
		
		try {
			final ObjectProgramModel programModel = new JavaProgramModel();
			
			final FileSpec fileSpec = new FileSystemFileSpec(sourceFilePath.getFile());
			
			final ASTParsedFile parsedFile = BuildAndResolve.parseFile(
					parser,
					inputStream,
					getCharset(),
					fileSpec,
					resolvedTypes);
			
			final Map<FileSpec, List<ResolveError>> resolveErrors = BuildAndResolve.resolveParsedFiles(
					Arrays.asList(ProgramLoader.makeCompiledFile(parsedFile)),
					programModel,
					JavaTypes.getBuiltinTypeRefs(),
					resolvedTypes)
					.getResolveErrors();
			
			final List<CompileError> allErrors = new ArrayList<>(parsedFile.getErrors().size() + resolveErrors.size());
			
			allErrors.addAll(parsedFile.getErrors());
			
			final List<ResolveError> resolve = resolveErrors.get(fileSpec);
			
			if (resolve != null) {
				allErrors.addAll(resolve);
			}

			sourceFileModel = new CompilerSourceFileModel(
					new JavaProgramModel(),
					parsedFile.getParsed(),
					allErrors,
					resolvedTypes,
					-1,
					codeMap);

		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
		
		return sourceFileModel;
	}
}

