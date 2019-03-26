package com.neaterbits.ide.common.ui.view.dialogs;

import com.neaterbits.ide.common.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.common.ui.view.ButtonListener;
import com.neaterbits.ide.common.ui.view.TextInputChangeListener;
import com.neaterbits.ide.common.ui.view.TextInputEnterKeyListener;

public interface FindReplaceDialog {

	FindReplaceDialogModel getModel();
	
	void setFindTextSelected();
	
	void addFindTextChangeListener(TextInputChangeListener listener);
	void addFindTextEnterKeyListener(TextInputEnterKeyListener listener);
	
	void addReplaceWithTextListener(TextInputChangeListener listener);
	
	void addDirectionForwardListener(ButtonListener listener);
	void addDirectionBackwardListener(ButtonListener listener);
	
	void addScopeAllListener(ButtonListener listener);
	void addScopeSelectedLinesListener(ButtonListener listener);
	
	void addOptionsCaseSensitiveListener(ButtonListener listener);
	void addOptionsWrapSearchListener(ButtonListener listener);
	void addOptionsWholeWordListener(ButtonListener listener);

	void addFindButtonListener(ButtonListener listener);
	void addReplaceFindButtonListener(ButtonListener listener);
	void addReplaceButtonListener(ButtonListener listener);
	void addReplaceAllButtonListener(ButtonListener listener);

	void setFindButtonEnabled(boolean enabled);
	void setReplaceFindButtonEnabled(boolean enabled);
	void setReplaceButtonEnabled(boolean enabled);
	void setReplaceAllButtonEnabled(boolean enabled);
	
}
