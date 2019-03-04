package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;

final class ActionApplicableParametersImpl implements ActionApplicableParameters {

	private final ActionExecuteState executeState;

	ActionApplicableParametersImpl(ActionExecuteState executeState) {
		
		Objects.requireNonNull(executeState);
		
		this.executeState = executeState;
	}

	@Override
	public Clipboard getClipboard() {
		return executeState.getClipboard();
	}
}
