package com.neaterbits.ide.component.java.language;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.neaterbits.build.common.language.CompileableLanguage;
import com.neaterbits.build.common.tasks.util.SourceFileScanner;
import com.neaterbits.build.language.java.jdk.JavaBuildableLanguage;
import com.neaterbits.build.strategies.compilemodules.CompileModule;
import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.build.strategies.compilemodules.ResolvedModule;
import com.neaterbits.build.types.ClassLibs;
import com.neaterbits.build.types.DependencyFile;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.FileSystemResourcePath;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.NamespaceResource;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResource;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.java.bytecode.JavaBytecodeFormat;
import com.neaterbits.compiler.java.bytecode.JavaClassLibs;
import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.objects.ObjectProgramModel;
import com.neaterbits.compiler.model.objects.ObjectsCompilerModel;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.CompileSource;
import com.neaterbits.compiler.resolver.build.CompilerOptions;
import com.neaterbits.compiler.resolver.build.ModulesBuilder;
import com.neaterbits.compiler.resolver.build.ResolvedSourceModule;
import com.neaterbits.compiler.resolver.build.SourceBuilder;
import com.neaterbits.compiler.resolver.build.SourceModule;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.ide.component.common.language.compilercommon.CompilerSourceFileModel;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.util.parse.ParserException;

public final class JavaLanguage extends JavaBuildableLanguage implements CompileableLanguage, ParseableLanguage {

    private static final CompilerOptions COMPILER_OPTIONS = new CompilerOptions(true);
    
    private static final LanguageSpec LANGUAGE_SPEC = JavaLanguageSpec.INSTANCE;

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
		
		final Path jrePath = Path.of(jreDir);

		final List<String> list;
		
		if (Files.exists(jrePath.resolve("jmods"))) {
		    
            final List<String> fileNames = Arrays.asList("java.base.jmod");

            list = fileNames.stream()
                    .map(fileName -> jreDir + "/jmods/" + fileName)
                    .collect(Collectors.toList());
		}
		else {
		
		    final List<String> fileNames = Arrays.asList("rt.jar", "charsets.jar");
		
		    list = fileNames.stream()
		            .map(fileName -> jreDir + "/lib/" + fileName)
		            .collect(Collectors.toList());
		}
		
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

	private static ObjectsCompilerModel makeCompilerModel(CompilerCodeMap codeMap) {
	    
	    return new ObjectsCompilerModel(
                LANGUAGE_SPEC,
                JavaTypes.getBuiltinTypes(),
                codeMap::getTypeNoByTypeName);
	}

	@Override
	public Map<SourceFileResourcePath, SourceFileModel> parseModule(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> files,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException {

		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels = new HashMap<>(files.size());
		
		final ObjectsCompilerModel compilerModel = makeCompilerModel(codeMap);
		        
		final ModulesBuilder<CompilationUnit, ASTParsedFile> modulesBuilder
		    = new ModulesBuilder<>(LANGUAGE_SPEC, compilerModel, COMPILER_OPTIONS);
		
		final CompileModule compileModule
		    = new CompileModule(
		            (ProjectModuleResourcePath)modulePath,
		            files,
		            getCharset(),
		            Collections.emptyList(),
		            Collections.emptyList());
		
		final ResolvedModule<ASTParsedFile, ResolveError> resolvedModule;
		
		try {
            resolvedModule = modulesBuilder.compile(compileModule, codeMap);
        } catch (IOException | ParserException ex) {
            throw new IllegalStateException(ex);
        }

		for (ParsedWithCachedRefs<ASTParsedFile, ResolveError> parsedWithCachedRefs
		            : resolvedModule.getParsedModule().getParsed()) {

		    
		    final SourceFileModel sourceFileModel = makeSourceFileModel(
		                                                parsedWithCachedRefs,
		                                                resolvedTypes,
		                                                codeMap);
		    
	        final SourceFileResourcePath path = resolvedModule.getCompileModule().getSourceFiles().stream()
	                .filter(sourceFile -> sourceFile.getFile().equals(
	                                            parsedWithCachedRefs.getParsedFile().getFileSpec().getFile()))
	                .findFirst()
	                .orElseThrow(IllegalStateException::new);

	        sourceFileModels.put(path, sourceFileModel);
		}
		
		return sourceFileModels;
	}
	
	@Override
	public SourceFileModel parseAndResolveChangedFile(
			SourceFileResourcePath sourceFilePath,
			String string,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) {

		final ObjectsCompilerModel compilerModel = makeCompilerModel(codeMap);

		final SourceBuilder<CompilationUnit, ASTParsedFile> sourceBuilder
                                                    		    = new SourceBuilder<>(
                                                    		            LANGUAGE_SPEC,
                                                    		            compilerModel,
                                                    		            COMPILER_OPTIONS);
		
		final CompileSource compileSource = new CompileSource(string, sourceFilePath.getFile().getName());
		
		final SourceModule sourceModule = new SourceModule(
		        Arrays.asList(compileSource),
		        getCharset(),
		        Collections.emptyList(),
		        Collections.emptyList());

		final ResolvedSourceModule<ASTParsedFile> resolvedSourceModule;

        try {
            resolvedSourceModule = sourceBuilder.compile(
                                                    sourceModule,
                                                    codeMap,
                                                    fileName -> sourceFilePath.getFile());

        } catch (IOException | ParserException ex) {
            throw new IllegalStateException(ex);
        }
		
		final ParsedWithCachedRefs<ASTParsedFile, ResolveError> parsedWithCachedRefs
		        = resolvedSourceModule.getParsedModule().getParsed().get(0);

		final SourceFileModel sourceFileModel = makeSourceFileModel(
		                                                parsedWithCachedRefs,
		                                                resolvedTypes,
		                                                codeMap);
		
		return sourceFileModel;
	}

    private SourceFileModel makeSourceFileModel(
            ParsedWithCachedRefs<ASTParsedFile, ResolveError> parsedWithCachedRefs,
            ResolvedTypes resolvedTypes,
            CompilerCodeMap codeMap) {
        
        final List<CompileError> allErrors = new ArrayList<>();
        
        allErrors.addAll(parsedWithCachedRefs.getParsedFile().getErrors());
        allErrors.addAll(parsedWithCachedRefs.getResolveErrorsList());
        
        final SourceFileModel sourceFileModel = new CompilerSourceFileModel(
                new ObjectProgramModel(),
                parsedWithCachedRefs.getParsedFile().getParsed(),
                allErrors,
                resolvedTypes,
                parsedWithCachedRefs.getCodeMapFileNo(),
                codeMap);
        
        return sourceFileModel;
    }
}

