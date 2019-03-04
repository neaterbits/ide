package com.neaterbits.ide.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import com.neaterbits.ide.common.ui.keys.Key;
import com.neaterbits.ide.common.ui.keys.KeyMask;
import com.neaterbits.ide.common.ui.keys.QualifierKey;
import com.neaterbits.ide.common.ui.view.KeyEventListener;

final class SWTKeyEventListener implements KeyListener {

	private final KeyEventListener delegate;
	
	@FunctionalInterface
	interface KeyEventHandler {
		void onKey(Key key, KeyMask mask);
	}

	SWTKeyEventListener(KeyEventListener delegate) {
		
		Objects.requireNonNull(delegate);
		
		this.delegate = delegate;
	}
	
	void keyPressed(char character, int keyCode, int stateMask) {
		convertKeyEvent(character, keyCode, stateMask, delegate::onKeyPress);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		keyPressed(event.character, event.keyCode, event.stateMask);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		convertKeyEvent(event.character, event.keyCode, event.stateMask, delegate::onKeyRelease);
	}

	private void convertKeyEvent(char character, int keyCode, int stateMask, KeyEventHandler keyEventHandler) {
		
		final int convertedKeyCode;
		
		switch (keyCode) {
		case SWT.F1: 	convertedKeyCode = Key.F1; break;
		case SWT.F2: 	convertedKeyCode = Key.F2; break;
		case SWT.F3: 	convertedKeyCode = Key.F3; break;
		case SWT.F4: 	convertedKeyCode = Key.F4; break;
		case SWT.F5: 	convertedKeyCode = Key.F5; break;
		case SWT.F6: 	convertedKeyCode = Key.F6; break;
		case SWT.F7: 	convertedKeyCode = Key.F7; break;
		case SWT.F8: 	convertedKeyCode = Key.F8; break;
		case SWT.F9: 	convertedKeyCode = Key.F9; break;
		case SWT.F10: 	convertedKeyCode = Key.F10; break;
		case SWT.F11: 	convertedKeyCode = Key.F11; break;
		case SWT.F12: 	convertedKeyCode = Key.F12; break;
		
		default:
			convertedKeyCode = keyCode;
			break;
		}
		
		char c = Character.toLowerCase(character);
		
		final List<QualifierKey> qualifiers = new ArrayList<>();
		
		if ((stateMask & SWT.CTRL) != 0) {
			qualifiers.add(QualifierKey.CTRL);
	
			c = (char)(c + 'a' - 1);
		}

		if ((stateMask & SWT.SHIFT) != 0) {
			qualifiers.add(QualifierKey.SHIFT);
		}
		
		if ((stateMask & SWT.ALT) != 0) {
			qualifiers.add(QualifierKey.ALT);
		}

		keyEventHandler.onKey(
				new Key(c, convertedKeyCode),
				new KeyMask(qualifiers));
	}
}
