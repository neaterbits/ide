package com.neaterbits.ide.common.model.codemap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.bytecode.common.TypeToDependencyFile;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap.LoadType;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.codemap.IntCodeMap;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap.TypeResult;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.model.common.InformationGatherer;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;

public final class CodeMapGatherer extends InformationGatherer implements CodeMapModel {

	private final AsyncExecutor asyncExecutor;
	private final CompileableLanguage language;
	private final BytecodeFormat bytecodeFormat;
	
	private final TypeToDependencyFile typeToDependencyFile;
	
	private final HashTypeMap<ClassInfo> typeMap;

	private final IntCodeMap codeMap;

	private final List<TypeSuggestionFinder> typeSuggestionFinders;
	
	private boolean codeScanComplete;
	
	private static class ClassInfo implements TypeSuggestion {

		private final int typeNo;
		private final TypeName typeName;
		private final String namespace;
		private final String binaryName;
		private final TypeVariant typeVariant;
		private final SourceFileResourcePath sourceFileResourcePath;

		ClassInfo(
				int typeNo,
				TypeName typeName,
				String namespace,
				String binaryName,
				SourceFileResourcePath sourceFileResourcePath,
				ClassBytecode classByteCode) {
			
			this.typeNo = typeNo;
			this.typeName = typeName;
			this.namespace = namespace;
			this.binaryName = binaryName;
			this.typeVariant = classByteCode.getTypeVariant();
			this.sourceFileResourcePath = sourceFileResourcePath;
		}
		
		int getTypeNo() {
			return typeNo;
		}

		@Override
		public TypeVariant getType() {
			return typeVariant;
		}

		@Override
		public String getName() {
			return typeName.getName();
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public String getBinaryName() {
			return binaryName;
		}

		@Override
		public SourceFileResourcePath getSourceFile() {
			return sourceFileResourcePath;
		}
	}
	
	public CodeMapGatherer(AsyncExecutor asyncExecutor, CompileableLanguage language, BytecodeFormat bytecodeFormat, BuildRoot buildRoot) {

		Objects.requireNonNull(asyncExecutor);
		Objects.requireNonNull(language);
		Objects.requireNonNull(bytecodeFormat);
		
		this.asyncExecutor = asyncExecutor;
		this.language = language;
		this.bytecodeFormat = bytecodeFormat;
		
		this.typeToDependencyFile = new TypeToDependencyFile();
		
		this.typeMap = new HashTypeMap<>(ClassInfo::getTypeNo);
		this.codeMap = new IntCodeMap();
		
		final TypeSuggestionFinder typeMapSuggestionFinder = new TypeSuggestionFinder() {
			@Override
			boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst) {
				
				typeMap.forEachKeyValueSynchronized((typeName, classInfo) -> {
					
				});
				
				return codeScanComplete;
			}
			
			@Override
			boolean hasSourceCode() {
				
				// Might have source code if is project type
				return true;
			}

			@Override
			boolean canRetrieveTypeVariant() {
				return true;
			}
		};
		
		final TypeSuggestionFinder dependencyFileSuggestionFinder = new TypeSuggestionFinder() {
			
			@Override
			boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst) {

				typeToDependencyFile.forEachKeyValue((typeName, file) -> {
					
					final String namespaceString = language.getNamespaceString(typeName);
					
					final TypeName match = matcher.matches(typeName, null, namespaceString, typeName.getName());
					
					if (match != null) {
						dst.put(typeName, new TypeSuggestionImpl(
								null,
								namespaceString,
								typeName.getName(),
								language.getBinaryName(typeName),
								null));
					}
					
				});
				
				return false;
			}
			
			@Override
			boolean hasSourceCode() {
				return false;
			}

			@Override
			boolean canRetrieveTypeVariant() {
				return false;
			}
		};
		
		this.typeSuggestionFinders = Arrays.asList(
				dependencyFileSuggestionFinder,
				new SourceFileScannerTypeSuggestionFinder(buildRoot, language));
	}
	
	@Override
	public TypeSuggestions findSuggestions(String searchText, boolean onlyTypesWithSourceCode) {

		final String searchTextLowerCase = searchText.toLowerCase();
		
		final TypeNameMatcher typeNameMatcher = (typeNameIfKnown, sourceFileResourcePath, namespace, name) -> {
		
			final TypeName result;
			
			if (onlyTypesWithSourceCode && sourceFileResourcePath == null) {
				result = null;
			}
			else {
				result = Strings.startsWithToFindLowerCase(name, searchTextLowerCase)
					? typeNameIfKnown != null
							? typeNameIfKnown
							: getTypeName(sourceFileResourcePath, namespace, name)
					: null;
			}
			
			return result;
		};
		
		// Map instead of List for distinct type suggestions
		final Map<TypeName, TypeSuggestion> suggestions = new HashMap<>(10000);
		
		boolean completeResult = false;
		
		for (TypeSuggestionFinder typeSuggestionFinder : typeSuggestionFinders) {

			if (onlyTypesWithSourceCode && !typeSuggestionFinder.hasSourceCode()) {
				continue;
			}
			
			completeResult = typeSuggestionFinder.findSuggestions(typeNameMatcher, suggestions);
			
			if (completeResult) {
				break;
			}
		}
		
		final List<TypeSuggestion> suggestionsList = new ArrayList<>(suggestions.values());
		
		Collections.sort(suggestionsList, (t1, t2) -> t1.getName().compareTo(t2.getName()));

		return new TypeSuggestions(suggestionsList, completeResult);
	}
	
	private TypeName getTypeName(SourceFileResourcePath sourceFileResourcePath, String namespace, String name) {

		final TypeName typeName;
		
		if (sourceFileResourcePath != null) {
			typeName = language.getTypeName(sourceFileResourcePath);
		}
		else {
			typeName = language.getTypeName(namespace, name);
		}
		
		return typeName;
	}

	public void addCompiledModuleFileTypes(CompiledModuleFileResourcePath module, Set<TypeName> types) {
		typeToDependencyFile.mergeModuleDependencyTypes(new DependencyFile(module.getFile(), true), types);
	}
	
	public void addLibraryFileTypes(LibraryResourcePath module, Set<TypeName> types) {
		typeToDependencyFile.mergeLibraryDependencyTypesIfNotPresent(new DependencyFile(module.getFile(), true), types);
	}

	public void addSystemLibraryFileTypes(File libraryFile, Set<TypeName> types) {
		typeToDependencyFile.mergeLibraryDependencyTypesIfNotPresent(new DependencyFile(libraryFile, true), types);
	}

	public void addSystemLibraryFile(File file) {
		
		Set<TypeName> typeNames = null;
		
		try {
			typeNames = language.getTypesFromSystemLibraryFile(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		if (typeNames != null) {
			for (TypeName typeName : typeNames) {
				try {
					bytecodeFormat.loadClassBytecode(typeToDependencyFile, typeName);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public CodeMapModel getModel() {
		return this;
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
		
		final ClassBytecode bytecode = typeMap.addOrGetType(
				typeName,
				codeMap,
				typeResult,
				(typeNo, classByteCode) -> new ClassInfo(
						typeNo,
						typeName,
						language.getNamespaceString(typeName),
						language.getBinaryName(typeName),
						null,
						classByteCode),
				
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
