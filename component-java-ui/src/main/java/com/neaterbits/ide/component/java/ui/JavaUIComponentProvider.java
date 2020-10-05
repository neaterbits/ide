package com.neaterbits.ide.component.java.ui;

import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.Newable;
import com.neaterbits.ide.component.common.NewableCategoryName;
import com.neaterbits.ide.component.common.UIComponentContext;
import com.neaterbits.ide.component.common.UIComponentProvider;
import com.neaterbits.ide.ui.swt.SWTUIContext;

public final class JavaUIComponentProvider implements UIComponentProvider {

	@Override
	public void openNewableDialog(
			UIComponentContext uiContext,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess) {

		final SWTUIContext swtContext = (SWTUIContext)uiContext;
		
		final NewJavaTypeDialog dialog = new NewJavaTypeDialog(
				swtContext.getWindow(),
				"New " + newable.getDisplayName(),
				newable,
				sourceFolder,
				namespace,
				ideAccess);

		dialog.open();
	}
}
