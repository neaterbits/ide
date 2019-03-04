package com.neaterbits.ide.common.ui.actions;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.controller.EditActions;
import com.neaterbits.ide.common.ui.view.UIDialogs;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;

public interface ActionExecuteParameters {

	IDEComponentsConstAccess getComponents();
	
	SourceFileResourcePath getCurrentEditedFile();
	
	UIDialogs getUIDialogs();
	
	ComponentIDEAccess getComponentIDEAccess();
	
	BuildRoot getBuildRoot();
	
	EditActions getEditActions();
}