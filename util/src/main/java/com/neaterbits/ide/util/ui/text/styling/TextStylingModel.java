package com.neaterbits.ide.util.ui.text.styling;

import java.util.Collection;

public interface TextStylingModel {

	Collection<TextStyleOffset> getStyleOffsets(long startPos, long length);
	
}
