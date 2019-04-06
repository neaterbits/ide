package com.neaterbits.structuredlog.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

final class FilteredTexts {

	private final Map<Object, List<TextStyleOffset>> styleOffsets;
	
	FilteredTexts() {
		this.styleOffsets = new HashMap<>();
	}
	
	void addStyleOffset(Object object, TextStyleOffset styleOffset) {
		
		Objects.requireNonNull(object);
		Objects.requireNonNull(styleOffset);

		List<TextStyleOffset> offsets = styleOffsets.get(object);
		
		if (offsets == null) {
		
			offsets = new ArrayList<>();
			
			styleOffsets.put(object, offsets);
		}
		
		offsets.add(styleOffset);
	}
	
	List<TextStyleOffset> getStyleOffsets(Object object) {
		
		Objects.requireNonNull(object);
		
		final List<TextStyleOffset> list = styleOffsets.get(object);
	
		return list != null ? Collections.unmodifiableList(list) : null;
	}
	
	void clear() {
		styleOffsets.clear();
	}
}
