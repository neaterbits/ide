package com.neaterbits.ide.common.ui.model.text.difftextmodel;

import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.BaseTextModelTest;
import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.StringText;

public class DiffTextModelTest extends BaseTextModelTest {

	@Override
	protected BaseTextModel makeTextModel(LineDelimiter lineDelimiter, StringText initialText) {
		return new DiffTextModel(lineDelimiter, initialText);
	}
}
