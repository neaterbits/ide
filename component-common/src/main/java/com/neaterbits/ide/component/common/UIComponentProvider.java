package com.neaterbits.ide.component.common;

import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public interface UIComponentProvider {

	void openNewableDialog(
			UIComponentContext uiContext,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess);
	
}
