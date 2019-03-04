package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.translation.Translator;

public final class UIParameters {

	private final Translator translator;
	private final KeyBindings keyBindings;
	private final Menus menus;
	private final TextEditorConfig textEditorConfig;

	public UIParameters(Translator translator, KeyBindings keyBindings, Menus menus, TextEditorConfig textEditorConfig) {
		
		Objects.requireNonNull(translator);
		Objects.requireNonNull(keyBindings);
		Objects.requireNonNull(menus);
		Objects.requireNonNull(textEditorConfig);
		
		this.translator = translator;
		this.keyBindings = keyBindings;
		this.menus = menus;
		this.textEditorConfig = textEditorConfig;
	}

	public Translator getTranslator() {
		return translator;
	}
	
	public KeyBindings getKeyBindings() {
		return keyBindings;
	}

	public Menus getMenus() {
		return menus;
	}

	public TextEditorConfig getTextEditorConfig() {
		return textEditorConfig;
	}
}
