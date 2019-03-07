package com.neaterbits.ide.common.model.codemap;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.bytecode.common.TypeToDependencyFile;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap.LoadType;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap.TypeResult;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.model.common.InformationGatherer;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;

public final class CodeMapGatherer extends InformationGatherer {

	private final CompileableLanguage language;
	private final BytecodeFormat bytecodeFormat;
	
	private final TypeToDependencyFile typeToDependencyFile;
	private final CodeMapModel model;
	
	private final HashTypeMap typeMap;
	
	public CodeMapGatherer(CompileableLanguage language, BytecodeFormat bytecodeFormat) {

		Objects.requireNonNull(language);
		Objects.requireNonNull(bytecodeFormat);
		
		this.language = language;
		this.bytecodeFormat = bytecodeFormat;
		
		this.typeToDependencyFile = new TypeToDependencyFile();
		this.model = new CodeMapModel();
		this.typeMap = new HashTypeMap();
	}
	
	public void addCompiledModuleFileTypes(CompiledModuleFileResourcePath module, Set<TypeName> types) {
		typeToDependencyFile.addModuleDependencyTypes(new DependencyFile(module.getFile(), true), types);
	}
	
	public void addLibraryFileTypes(LibraryResourcePath module, Set<TypeName> types) {
		typeToDependencyFile.addLibraryDependencyTypes(new DependencyFile(module.getFile(), true), types);
	}
	
	public ClassLibs getClassLibs() {
		return typeToDependencyFile;
	}
	
	public void addClassFile(SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(sourceFile);
		
		final TypeName typeName = language.getTypeName(sourceFile);
		
		try (FileInputStream inputStream = new FileInputStream(sourceFile.getFile())) {

			loadAndAddToCodeMap(typeName, type -> {
				ClassBytecode classBytecode = null;
				
				try {
					classBytecode = bytecodeFormat.loadClassBytecode(inputStream);
				} catch (IOException | ClassFileException ex) {
					ex.printStackTrace();
				}
				catch (Exception ex) {
					System.out.print("## error while reading " + sourceFile.getFile().getPath());
				}
				
				return classBytecode;
			});
		}
		catch (IOException ex) {
			System.out.println("## error while reading " + sourceFile.getFile().getPath());
			// ex.printStackTrace();
		}
	}

	// load classfile from local library
	void loadAndAddToCodeMap(TypeName typeName) {
		
		loadAndAddToCodeMap(typeName, type -> {
					
					ClassBytecode classBytecode = null;
					
					try {
						classBytecode = bytecodeFormat.loadClassBytecode(typeToDependencyFile, type);
					} catch (IOException | ClassFileException ex) {
						ex.printStackTrace();
					}
					
					return classBytecode;
				});

	}

	
	private void loadAndAddToCodeMap(TypeName typeName, LoadType loadType) {
		
		Objects.requireNonNull(typeName);
		
		final TypeResult typeResult = new TypeResult();
		
		final CodeMap codeMap = model.getCodeMap();
		
		final ClassBytecode bytecode = typeMap.addOrGetType(
				typeName,
				codeMap,
				typeResult,
				loadType);
		

		if (bytecode != null) {
			final int methodCount = bytecode.getMethodCount();
		
			synchronized (typeMap) {
				System.out.println("## set method count " + methodCount);
				
				codeMap.setMethodCount(typeResult.type, methodCount);
			}
		}
	}
}
