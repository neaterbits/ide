package com.neaterbits.ide.common.ui.view;

import java.util.Collection;
import java.util.function.Consumer;

import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.model.codemap.TypeSuggestion;
import com.neaterbits.ide.common.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.common.ui.model.dialogs.NewableSelection;
import com.neaterbits.ide.common.ui.model.dialogs.OpenTypeDialogModel;
import com.neaterbits.ide.common.ui.view.dialogs.FindReplaceDialog;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.Newable;
import com.neaterbits.ide.component.common.NewableCategory;
import com.neaterbits.ide.component.common.NewableCategoryName;
import com.neaterbits.ide.component.common.UIComponentProvider;

public interface UIDialogs {

	TypeSuggestion askOpenType(OpenTypeDialogModel model);

	NewableSelection askCreateNewable(Collection<NewableCategory> categories);

	void openNewableDialog(
			UIComponentProvider uiComponentProvider,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess);

	void askFindReplace(FindReplaceDialogModel lastModel, Consumer<FindReplaceDialog> onCreated);
	
}