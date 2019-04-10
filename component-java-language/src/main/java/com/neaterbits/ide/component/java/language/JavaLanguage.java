package com.neaterbits.ide.component.java.language;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.ParsedFile;
import com.neaterbits.compiler.ast.parser.SourceFile;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.java.JavaTypes;
import com.neaterbits.compiler.java.bytecode.JavaBytecodeFormat;
import com.neaterbits.compiler.java.bytecode.JavaClassLibs;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.resolver.FilesResolver;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolveLogger;
import com.neaterbits.compiler.resolver.UnresolvedDependencies;
import com.neaterbits.compiler.resolver.ast.ASTModelImpl;
import com.neaterbits.compiler.resolver.ast.ProgramLoader;
import com.neaterbits.compiler.resolver.ast.model.ObjectProgramModel;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.modules.ModuleSpec;
import com.neaterbits.compiler.util.modules.SourceModuleSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParseError;
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

	@Override
	public boolean canReadCodeMapFromCompiledCode() {
		return true;
	}

	@Override
	public Map<SourceFileResourcePath, SourceFileModel> parseModule(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> files,
			ResolvedTypes resolvedTypes) throws IOException {

		final Java8AntlrParser parser = new Java8AntlrParser(false);

		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels = new HashMap<>(files.size());
		final Map<SourceFileResourcePath, ParsedFile> compilationUnits = new HashMap<>(files.size());
		
		final ObjectProgramModel programModel = new ObjectProgramModel();
		
		for (SourceFileResourcePath path : files) {
		
			final File file = path.getFile();
			
			try (FileInputStream inputStream = new FileInputStream(file)) {
			
				final ParsedFile parsedFile = parseFile(parser, inputStream, file, programModel, resolvedTypes);
				
				final SourceFileModel sourceFileModel = new CompilerSourceFileModel(programModel, parsedFile.getParsed(), parsedFile.getErrors(), resolvedTypes);

				compilationUnits.put(path, parsedFile);
				sourceFileModels.put(path, sourceFileModel);
			}
		}

		resolveParsedModule(modulePath, dependencies, compilationUnits.values(), programModel, resolvedTypes);
		
		return sourceFileModels;
	}

	@Override
	public SourceFileModel parseAndResolveChangedFile(SourceFileResourcePath sourceFilePath, String string, ResolvedTypes resolvedTypes) {

		final Java8AntlrParser parser = new Java8AntlrParser(false);
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(string.getBytes());
		
		final SourceFileModel sourceFileModel;
		
		try {
			
			final ObjectProgramModel programModel = new ObjectProgramModel();
			
			final ParsedFile parsedFile = parseFile(parser, inputStream, sourceFilePath.getFile(), programModel, resolvedTypes);
			
			final Collection<ResolveError> resolveErrors = resolveParsedFiles(
					Arrays.asList(ProgramLoader.makeCompiledFile(parsedFile)),
					programModel,
					resolvedTypes);
			
			final List<CompileError> allErrors = new ArrayList<>(parsedFile.getErrors().size() + resolveErrors.size());
			
			allErrors.addAll(parsedFile.getErrors());
			allErrors.addAll(resolveErrors);
			
			sourceFileModel = new CompilerSourceFileModel(programModel, parsedFile.getParsed(), allErrors, resolvedTypes);

		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
		
		return sourceFileModel;
	}

	private static ParsedFile parseFile(
			Java8AntlrParser parser,
			InputStream inputStream,
			File file,
			ObjectProgramModel programModel,
			ResolvedTypes resolvedTypes) throws IOException {
		
		final List<ParseError> errors = new ArrayList<>();
		
		final CompilationUnit compilationUnit = parser.parse(inputStream, errors, file.getName(), null);
		
		if (compilationUnit == null) {
			throw new IllegalStateException();
		}
		
		final List<CompileError> compileErrors = errors.stream().map(error -> (CompileError)error).collect(Collectors.toList());
		
		final ParsedFile parsedFile = new ParsedFile(
				new SourceFile(file),
				compileErrors,
				null,
				compilationUnit);
		
		return parsedFile;
	}

	private static void resolveParsedModule(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			Collection<ParsedFile> compilationUnits,
			ObjectProgramModel programModel,
			ResolvedTypes resolvedTypes) {
		
		final ModuleSpec moduleSpec = new SourceModuleSpec(
				modulePath.getModuleId(),
				dependencies.stream()
					.map(dependency -> new SourceModuleSpec(dependency.getModuleId(), null, dependency.getFile()))
					.collect(Collectors.toList()),
				modulePath.getFile());
		
		final Module module = new Module(moduleSpec, compilationUnits);
		
		final Program program = new Program(module);

		final Collection<CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>> allFiles = ProgramLoader.getCompiledFiles(program);
		
		resolveParsedFiles(allFiles, programModel, resolvedTypes);
	}
	

	private static Collection<ResolveError> resolveParsedFiles(
			Collection<CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>> allFiles,
			ObjectProgramModel programModel,
			ResolvedTypes resolvedTypes) {

		final ASTModelImpl astModel = new ASTModelImpl();

		final ResolveLogger<BuiltinType, ComplexType<?, ?, ?>, TypeName, CompilationUnit> logger = new ResolveLogger<>(System.out);

		final FilesResolver<BuiltinType, ComplexType<?, ?, ?>, TypeName, CompilationUnit> filesResolver
			= new FilesResolver<>(
					logger,
					JavaTypes.getBuiltinTypes(),
					scopedName -> {
						
						System.out.println("## lookup scoped name " + scopedName);
						
						return resolvedTypes.lookup(scopedName);
						
					},
					programModel,
					astModel);
		
		final ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveResult = filesResolver.resolveFiles(allFiles);
		
		final UnresolvedDependencies unresolved = resolveResult.getUnresolvedDependencies();

		final List<ResolveError> resolveErrors;
		
		if (!unresolved.isEmpty()) {
			resolveErrors = new ArrayList<>(unresolved.getCount());
			
			unresolved.forEach((fileSpec, compiledTypeDependency) -> {
				
				final ResolveError resolveError = new ResolveError("Cannot resolve name " + compiledTypeDependency.getScopedName().toString());
				
				resolveErrors.add(resolveError);
			});
		}
		else {
			resolveErrors = Collections.emptyList();
		}
		
		System.out.println("## resolved files: " + resolveResult.getResolvedFiles());
	
		return resolveErrors;
	}
}

