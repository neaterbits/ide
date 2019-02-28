package com.neaterbits.ide.common.ui.model.text;

import com.neaterbits.ide.util.ui.text.StringText;
import com.neaterbits.ide.util.ui.text.Text;

public class StringTextTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new StringText(string);
	}
}
