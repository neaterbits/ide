package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.translation.Translator;

public final class UIParameters {

	private final Translator translator;
	private final KeyBindings keyBindings;
	private final UIModels uiModels;
	private final TextEditorConfig textEditorConfig;

	public UIParameters(Translator translator, KeyBindings keyBindings, UIModels uiModels, TextEditorConfig textEditorConfig) {
		
		Objects.requireNonNull(translator);
		Objects.requireNonNull(keyBindings);
		Objects.requireNonNull(uiModels);
		Objects.requireNonNull(textEditorConfig);
		
		this.translator = translator;
		this.keyBindings = keyBindings;
		this.uiModels = uiModels;
		this.textEditorConfig = textEditorConfig;
	}

	public Translator getTranslator() {
		return translator;
	}
	
	public KeyBindings getKeyBindings() {
		return keyBindings;
	}

	public UIModels getUIModels() {
		return uiModels;
	}

	public TextEditorConfig getTextEditorConfig() {
		return textEditorConfig;
	}
}
