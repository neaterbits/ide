package com.neaterbits.ide.core.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.common.scheduling.IDEScheduler;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public final class SourceFilesModel {

	private final IDEScheduler scheduler;
	private final Languages languages;
	private final ResolvedTypes resolvedTypes;
	private final CompilerCodeMap codeMap;

	private static class ParsedSourceFile {

	    @SuppressWarnings("unused")
        private final SourceFileInfo sourceFileInfo;
	    private final SourceFileModel sourceFileModel;

	    ParsedSourceFile(SourceFileInfo sourceFileInfo, SourceFileModel sourceFileModel) {
	        
	        Objects.requireNonNull(sourceFileInfo);
	        Objects.requireNonNull(sourceFileModel);
	        
            this.sourceFileInfo = sourceFileInfo;
            this.sourceFileModel = sourceFileModel;
        }
	}
	
	private final Map<SourceFileResourcePath, ParsedSourceFile> parsedSourceFiles;
	
	public SourceFilesModel(IDEScheduler scheduler, Languages languages, ResolvedTypes resolvedTypes, CompilerCodeMap codeMap) {
		
		Objects.requireNonNull(scheduler);
		Objects.requireNonNull(languages);
		Objects.requireNonNull(resolvedTypes);
		
		this.scheduler = scheduler;
		this.languages = languages;
		this.resolvedTypes = resolvedTypes;
		this.codeMap = codeMap;
		this.parsedSourceFiles = new HashMap<>();
	}
	
	public SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath) {
	    
	    Objects.requireNonNull(sourceFileResourcePath);
	    
	    final ParsedSourceFile parsedSourceFile = parsedSourceFiles.get(sourceFileResourcePath);
	
	    return parsedSourceFile != null
	            ? parsedSourceFile.sourceFileModel
                : null;
	}

	public void parseModuleOnStartup(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> sourceFiles, LanguageName language) throws IOException {
		
		final LanguageComponent languageComponent = languages.getLanguageComponent(language);
		
		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels
				= languageComponent.getParseableLanguage().parseModule(modulePath, dependencies, sourceFiles, resolvedTypes, codeMap);
		
		for (Map.Entry<SourceFileResourcePath, SourceFileModel> entry : sourceFileModels.entrySet()) {
			
			final SourceFileInfo sourceFileInfo = new SourceFileInfo(entry.getKey(), languageComponent, resolvedTypes);
			final ParsedSourceFile parsedSourceFile = new ParsedSourceFile(sourceFileInfo, entry.getValue());

			parsedSourceFiles.put(sourceFileInfo.getPath(), parsedSourceFile);
		}
	}
	
	public void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel) {

		// Called from IDE so schedule asynchronously
		scheduler.scheduleTask(
				"parse",
				"Parse file " + sourceFile.getFile().getName(),
				Constraint.CPU,
				sourceFile,
				file -> {
					final SourceFileModel sourceFileModel  = file.getLanguage().getParseableLanguage().parseAndResolveChangedFile(
							sourceFile.getPath(),
							text.asString(),
							sourceFile.getResolvedTypes(),
							codeMap);

					return sourceFileModel;
				},
				(file, sourceFileModel) -> {

		            final ParsedSourceFile parsedSourceFile = new ParsedSourceFile(sourceFile, sourceFileModel);

					parsedSourceFiles.put(sourceFile.getPath(), parsedSourceFile);
					
					onUpdatedModel.accept(sourceFileModel);
				});
		
	}
}
