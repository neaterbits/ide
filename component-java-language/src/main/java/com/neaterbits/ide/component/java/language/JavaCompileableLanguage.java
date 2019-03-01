package com.neaterbits.ide.component.java.language;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.java.bytecode.JavaBytecodeFormat;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.resource.FileSystemResourcePath;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.NamespaceResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledFileResource;
import com.neaterbits.ide.common.resource.compile.CompiledFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.ide.util.PathUtil;

public final class JavaCompileableLanguage implements CompileableLanguage {

	private final BytecodeFormat bytecodeFormat;
	
	public JavaCompileableLanguage() {
		this.bytecodeFormat = new JavaBytecodeFormat();
	}
	
	@Override
	public CompleteName getCompleteName(SourceFileResourcePath sourceFile) {
		final NamespaceResource namespaceResource = (NamespaceResource)sourceFile.getFromLast(1);
	
		return new CompleteName(
				new NamespaceReference(namespaceResource.getNamespace()),
				null,
				classNameFromFile(sourceFile.getFile()));
	}
	
	private static ClassName classNameFromFile(File file) {
		
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

	
}
