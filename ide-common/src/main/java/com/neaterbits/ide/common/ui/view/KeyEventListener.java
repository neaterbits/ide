package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.ui.keys.Key;
import com.neaterbits.ide.common.ui.keys.KeyMask;

public interface KeyEventListener {

	void onKeyPress(Key key, KeyMask mask);
	
	void onKeyRelease(Key key, KeyMask mask);
}
