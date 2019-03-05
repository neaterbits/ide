package com.neaterbits.ide.model.text;

import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.model.text.StringTextModel;
import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.StringText;

public class StringTextModelTest extends BaseTextModelTest {

	@Override
	protected TextModel makeTextModel(LineDelimiter lineDelimiter, StringText initialText) {
		return new StringTextModel(lineDelimiter, initialText);
	}
}
