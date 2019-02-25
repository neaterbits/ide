package com.neaterbits.ide.common.ui.model.text;

import com.neaterbits.ide.common.ui.model.text.util.CharText;

public class CharTextSubstringTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new CharText("prefix" + string + "postfix")
				.substring(
						"prefix".length(),
						"prefix".length() + string.length());
	}
}
