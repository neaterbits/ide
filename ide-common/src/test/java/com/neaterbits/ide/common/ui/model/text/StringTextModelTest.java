package com.neaterbits.ide.common.ui.model.text;

import com.neaterbits.ide.common.ui.model.text.util.StringText;

public class StringTextModelTest extends BaseTextModelTest {

	@Override
	protected BaseTextModel makeTextModel(LineDelimiter lineDelimiter, StringText initialText) {
		return new StringTextModel(lineDelimiter, initialText);
	}
}
