package com.neaterbits.ide.common.ui.model.text;

import com.neaterbits.ide.common.ui.model.text.util.CharText;

public class CharTextTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new CharText(string);
	}
}
