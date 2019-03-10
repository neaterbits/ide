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
import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.bytecode.common.TypeToDependencyFile;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap.CreateType;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap.LoadType;
import com.neaterbits.compiler.codemap.IntCodeMap;
import com.neaterbits.compiler.codemap.CodeMap.TypeResult;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.bytecode.common.loader.LoadClassHelper;
import com.neaterbits.compiler.bytecode.common.loader.LoadClassParameters;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.model.common.InformationGatherer;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;

public final class CodeMapGatherer extends InformationGatherer implements CodeMapModel {

	private final CompileableLanguage language;
	private final BytecodeFormat bytecodeFormat;
	
	private final TypeToDependencyFile typeToDependencyFile;
	
	private final HashTypeMap<ClassInfo> typeMap;
	private final IntCodeMap codeMap;

	private final List<TypeSuggestionFinder> typeSuggestionFinders;

	private final CreateType<ClassInfo> createType;
	
	private boolean codeScanComplete;
	
	public CodeMapGatherer(AsyncExecutor asyncExecutor, CompileableLanguage language, BytecodeFormat bytecodeFormat, BuildRoot buildRoot) {

		Objects.requireNonNull(asyncExecutor);
		Objects.requireNonNull(language);
		Objects.requireNonNull(bytecodeFormat);
		
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

			@Override
			boolean hasType(TypeName typeName) {
				return typeMap.hasType(typeName);
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

			@Override
			boolean hasType(TypeName typeName) {
				return typeToDependencyFile.hasType(typeName);
			}
		};
		
		this.typeSuggestionFinders = Arrays.asList(
				typeMapSuggestionFinder,
				dependencyFileSuggestionFinder,
				new SourceFileScannerTypeSuggestionFinder(buildRoot, language));
		
		this.createType = (typeName, typeNo, classByteCode) -> new ClassInfo(
				typeNo,
				typeName,
				language.getNamespaceString(typeName),
				language.getBinaryName(typeName),
				null,
				classByteCode);
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
	
	@Override
	public TypeName lookup(ScopedName scopedName) {

		TypeName result;
		
		if (scopedName.hasScope()) {
		
			result = null;
			
			final String [] parts = scopedName.getParts();
			
			// Try all combinations of type names
			for (int numOuterTypes = 0; numOuterTypes < parts.length - 1; ++ numOuterTypes) {
				final TypeName typeName = new TypeName(
						numOuterTypes == parts.length - 1
							? null
							: Arrays.copyOf(parts, parts.length - 1 - numOuterTypes),
							
						numOuterTypes == 0
							? null
							: Arrays.copyOfRange(parts, parts.length - 1 - numOuterTypes, parts.length - 1),
						
						parts[parts.length - 1]);
		
				if (hasType(typeName)) {
					result = typeName;
					break;
				}
			}
		}
		else {
			// not fully scoped so cannot resolve
			result = null;
		}
		
		return result;
	}
	
	private boolean hasType(TypeName typeName) {
		
		for (TypeSuggestionFinder typeSuggestionFinder : typeSuggestionFinders) {
			if (typeSuggestionFinder.hasType(typeName)) {
				return true;
			}
		}
		
		return false;
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

	public void addSystemLibraryFile(DependencyFile file) {
		
		final long start = System.currentTimeMillis();

		Set<TypeName> typeNames = null;
		
		try {
			typeNames = language.getTypesFromSystemLibraryFile(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		final ClassLibs systemLibraries = language.getSystemLibraries();
		
		if (typeNames != null) {
			for (TypeName typeName : typeNames) {
				try {
					final boolean addedType = loadClassAndBaseClassesAndAddToCodeMap(systemLibraries, typeName);
					
					if (!addedType) {
						System.out.println("## already added " + typeName.toDebugString());
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		System.out.println("Loading files took " + ((System.currentTimeMillis() - start) / 1000));
	}
	
	public CodeMapModel getModel() {
		return this;
	}
	
	public void addClassFile(SourceFileResourcePath sourceFile) throws IOException, ClassFileException {
		
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
	void loadAndAddToCodeMap(TypeName typeName) throws IOException, ClassFileException {
		
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

	private boolean loadClassAndBaseClassesAndAddToCodeMap(ClassLibs classLibs, TypeName typeName) throws IOException, ClassFileException {
		
		final LoadClassParameters<File, ClassInfo, Void> parameters = new LoadClassParameters<>(
				typeMap,
				codeMap,
				createType,
				null,
				type -> bytecodeFormat.loadClassBytecode(classLibs, type));
		
		final ClassBytecode addedByteCode = LoadClassHelper.loadClassAndBaseTypesAndAddToCodeMap(typeName, new TypeResult(), parameters);
		
		return addedByteCode != null;
	}
	
	private void loadAndAddToCodeMap(TypeName typeName, LoadType loadType) throws IOException, ClassFileException {
		
		Objects.requireNonNull(typeName);
		
		final TypeResult typeResult = new TypeResult();
		
		final ClassBytecode bytecode = typeMap.addOrGetType(
				typeName,
				codeMap,
				false,
				typeResult,
				createType,
				loadType);
		
		// System.out.println("## added type " + language.getCompleteNameString(typeName) + " with typeNo " + typeResult.type);
		
		if (bytecode != null) {
			final int methodCount = bytecode.getMethodCount();
		
			synchronized (typeMap) {
				codeMap.setMethodCount(typeResult.type, methodCount);
			}
		}
	}
}
