package com.neaterbits.ide.common.model.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.ide.common.model.common.SourceFileInfo;
import com.neaterbits.ide.common.scheduling.IDEScheduler;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.ui.text.Text;

public final class SourceFilesModel {

	private final IDEScheduler scheduler;
	
	private final Map<SourceFileInfo, SourceFileModel> parsedSourceFiles;
	
	public SourceFilesModel(IDEScheduler scheduler) {
		
		Objects.requireNonNull(scheduler);
		
		this.scheduler = scheduler;
		this.parsedSourceFiles = new HashMap<>();
	}
	
	public void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel) {

		scheduler.scheduleTask(
				"parse",
				"Parse file " + sourceFile.getFile().getName(),
				Constraint.CPU,
				sourceFile,
				file -> {
					final SourceFileModel sourceFileModel  = file.getLanguage().getParseableLanguage().parse(
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
