package com.neaterbits.ide.main;

import com.neaterbits.ide.common.ui.keys.Key;
import com.neaterbits.ide.common.ui.keys.KeyMask;
import com.neaterbits.ide.common.ui.keys.QualifierKey;
import com.neaterbits.ide.common.ui.view.KeyEventListener;

final class IDEKeyListener implements KeyEventListener {
	
	private final UIController<?> uiController;
	
	IDEKeyListener(UIController<?> uiController) {
		this.uiController = uiController;
	}

	@Override
	public void onKeyRelease(Key key, KeyMask mask) {

	}

	@Override
	public void onKeyPress(Key key, KeyMask mask) {
		
		if (   key.getKeyCode() == 116 // key.getCharacter() == 't'
			&& mask.isSet(QualifierKey.CTRL)
			&& mask.isSet(QualifierKey.SHIFT)) {

			uiController.askOpenType();
		}
		else if (key.getKeyCode() == 109 && mask.isSet(QualifierKey.CTRL)) {
			uiController.minMaxEditors();
		}
		else if (key.getKeyCode() == 110 && mask.isSet(QualifierKey.CTRL)) {
			uiController.askCreateNewable();
		}
		else if (   key.getKeyCode() == 119
			      && mask.isSet(QualifierKey.SHIFT)
			      && mask.isSet(QualifierKey.ALT)) {
			
			uiController.showCurrentEditedInProjectView();
		}
	}
}
