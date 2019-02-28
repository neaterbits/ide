package com.neaterbits.ide.util.ui.text.styling;

import java.util.Collection;

import com.neaterbits.ide.util.ui.text.Text;

public interface TextStyling {

	Collection<TextStyleOffset> applyStyles(Text text);

}
