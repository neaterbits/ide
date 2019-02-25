package com.neaterbits.ide.component.java.language;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.ide.common.language.CompilableLanguage;
import com.neaterbits.ide.common.resource.NamespaceResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledFileResource;
import com.neaterbits.ide.common.resource.compile.CompiledFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.ide.util.PathUtil;

public final class JavaCompilableLanguage implements CompilableLanguage {

	@Override
	public Set<CompleteName> getDependencies(CompiledFileResourcePath compiledFile) {
		// throw new UnsupportedOperationException();
		return new HashSet<>();
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
	public Set<CompleteName> getFilesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException {

		final Set<CompleteName> files;
		
		try (JarFile jarFile = new JarFile(compiledModuleFileResourcePath.getFile())) {
		
			files = jarFile.stream()
				.map(entry -> entry.getName())
				.filter(name -> name.endsWith(".class"))
				.map(name -> Strings.split(name, '/'))
				.map(parts -> new CompleteName(
						new NamespaceReference(Arrays.copyOf(parts, parts.length - 1)),
						null,
						new ClassName(parts[parts.length - 1])))
				.collect(Collectors.toSet());
		
		}
		
		return files;
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
}
