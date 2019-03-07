package com.neaterbits.ide.component.java.language;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.model.ObjectProgramModel;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.java.bytecode.JavaBytecodeFormat;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.resource.FileSystemResourcePath;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.NamespaceResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResource;
import com.neaterbits.ide.common.resource.compile.CompiledFileResource;
import com.neaterbits.ide.common.resource.compile.CompiledFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.ide.component.common.language.compilercommon.CompilerSourceFileModel;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.ide.util.PathUtil;

public final class JavaLanguage implements CompileableLanguage, ParseableLanguage {

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
	
	@Override
	public CompleteName getCompleteName(SourceFileResourcePath sourceFile) {

		return new CompleteName(
				new NamespaceReference(getNamespace(sourceFile).getNamespace()),
				null,
				classNameFromSourceFile(sourceFile.getFile()));
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
	public String getBinaryName(TypeName typeName) {
		return typeName.getName() + ".class";
	}

	private static ClassName classNameFromSourceFile(File file) {
		
		final String name = file.getName();
		
		return new ClassName(name.substring(0, name.length() - ".java".length()));
	}

	@Override
	public Set<TypeName> getTypesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException {
		return getTypesFromJarFile(compiledModuleFileResourcePath);
	}
	
	@Override
	public Set<TypeName> getTypesFromLibraryFile(LibraryResourcePath libraryResourcePath) throws IOException {
		return getTypesFromJarFile(libraryResourcePath);
	}

	private Set<TypeName> getTypesFromJarFile(FileSystemResourcePath jarFileResourcePath) throws IOException {

		return bytecodeFormat.getTypesFromLibraryFile(jarFileResourcePath.getFile());
	}

	@Override
	public CompiledFileResourcePath getCompiledFilePath(TargetDirectoryResourcePath targetDirectory, SourceFileResourcePath sourceFile) {
		
		final File sourceFolder = sourceFile.getSourceFolder();

		final String path = PathUtil.removeDirectoryFromPath(sourceFolder, sourceFile.getFile());

		final String classFilePath;
		
		if (path.endsWith(".java")) {
			classFilePath = path.substring(0, path.length() - ".java".length()) + ".class";
		}
		else {
			classFilePath = path;
		}
		
		final File classesDirectory = new File(targetDirectory.getFile(), "classes");
		
		final File classFile = new File(classesDirectory, classFilePath);

		return new CompiledFileResourcePath(targetDirectory, new CompiledFileResource(classFile));
	}

	@Override
	public boolean canReadCodeMapFromCompiledCode() {
		return true;
	}

	@Override
	public SourceFileModel parse(String string) {

		final Java8AntlrParser parser = new Java8AntlrParser(false);

		final CompilationUnit compilationUnit = parser.parse(string, false);
		
		return new CompilerSourceFileModel(new ObjectProgramModel(), compilationUnit);
	}
}
