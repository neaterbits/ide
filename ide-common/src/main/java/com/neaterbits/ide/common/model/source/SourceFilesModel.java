package com.neaterbits.ide.common.model.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.ide.common.model.common.SourceFileInfo;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.scheduling.IDEScheduler;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.ui.text.Text;

public final class SourceFilesModel {

	private final IDEScheduler scheduler;
	private final Languages languages;
	private final ResolvedTypes resolvedTypes;
	
	private final Map<SourceFileInfo, SourceFileModel> parsedSourceFiles;
	
	public SourceFilesModel(IDEScheduler scheduler, Languages languages, ResolvedTypes resolvedTypes) {
		
		Objects.requireNonNull(scheduler);
		Objects.requireNonNull(languages);
		Objects.requireNonNull(resolvedTypes);
		
		this.scheduler = scheduler;
		this.languages = languages;
		this.resolvedTypes = resolvedTypes;
		this.parsedSourceFiles = new HashMap<>();
	}

	public void parseModuleOnStartup(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> sourceFiles, LanguageName language) throws IOException {
		
		final LanguageComponent languageComponent = languages.getLanguageComponent(language);
		
		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels
				= languageComponent.getParseableLanguage().parseModule(modulePath, dependencies, sourceFiles, resolvedTypes);

		
		for (Map.Entry<SourceFileResourcePath, SourceFileModel> entry : sourceFileModels.entrySet()) {
			
			final SourceFileInfo sourceFileInfo = new SourceFileInfo(entry.getKey(), languageComponent, resolvedTypes);
			
			parsedSourceFiles.put(sourceFileInfo, entry.getValue());
		}
	}
	
	public void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel) {

		scheduler.scheduleTask(
				"parse",
				"Parse file " + sourceFile.getFile().getName(),
				Constraint.CPU,
				sourceFile,
				file -> {
					final SourceFileModel sourceFileModel  = file.getLanguage().getParseableLanguage().parseFile(
							text.asString(),
							sourceFile.getResolvedTypes());

					return sourceFileModel;
				},
				(file, sourceFileModel) -> {

					parsedSourceFiles.put(sourceFile, sourceFileModel);
					
					onUpdatedModel.accept(sourceFileModel);
				});
		
	}
}
