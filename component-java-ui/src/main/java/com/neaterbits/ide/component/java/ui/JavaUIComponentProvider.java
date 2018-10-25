package com.neaterbits.ide.component.java.ui;

import org.eclipse.swt.widgets.Shell;

import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.Newable;
import com.neaterbits.ide.component.common.NewableCategoryName;
import com.neaterbits.ide.component.common.UIComponentProvider;

public final class JavaUIComponentProvider implements UIComponentProvider<Shell> {

	@Override
	public void openNewableDialog(
			Shell window,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess) {
		
		final NewJavaTypeDialog dialog = new NewJavaTypeDialog(
				window,
				"New " + newable.getDisplayName(),
				newable,
				sourceFolder,
				namespace,
				ideAccess);

		dialog.open();
	}
}
