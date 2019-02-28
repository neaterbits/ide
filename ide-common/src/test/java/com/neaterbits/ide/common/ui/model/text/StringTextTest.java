package com.neaterbits.ide.common.ui.model.text;

import com.neaterbits.ide.common.ui.model.text.util.StringText;

public class StringTextTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new StringText(string);
	}
}
