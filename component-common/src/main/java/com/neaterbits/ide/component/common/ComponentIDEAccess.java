package com.neaterbits.ide.component.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.component.common.language.Languages;

public interface ComponentIDEAccess extends IDEComponentsFinder {

    Translator getTranslator();
    
	void writeAndOpenFile(
			String projectName,
			String sourceFolder,
			String [] namespacePath,
			String fileName,
			String text) throws IOException;

	boolean isValidSourceFolder(String projectName, String sourceFolder);
	
	Languages getLanguages();
	
	File getRootPath();
	
	List<ProjectModuleResourcePath> getRootModules();
	
	<T> T readConfigurationFile(
	        Class<? extends ConfiguredComponent> componentType,
	        Class<T> configurationType,
	        ProjectModuleResourcePath module) throws IOException;
	
	void saveConfigurationFile(
            Class<? extends ConfiguredComponent> componentType,
	        Object configuration,
	        ProjectModuleResourcePath module) throws IOException;
	
	void displayError(String title, Exception ex);
}
