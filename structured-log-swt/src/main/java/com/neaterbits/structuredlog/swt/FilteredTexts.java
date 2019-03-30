package com.neaterbits.structuredlog.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.structuredlog.binary.model.LogObject;

final class FilteredTexts {

	private final Map<LogObject, List<TextStyleOffset>> styleOffsets;
	
	FilteredTexts() {
		this.styleOffsets = new HashMap<>();
	}
	
	void addStyleOffset(LogObject logObject, TextStyleOffset styleOffset) {
		
		Objects.requireNonNull(logObject);
		Objects.requireNonNull(styleOffset);

		List<TextStyleOffset> offsets = styleOffsets.get(logObject);
		
		if (offsets == null) {
		
			offsets = new ArrayList<>();
			
			styleOffsets.put(logObject, offsets);
		}
		
		offsets.add(styleOffset);
	}
	
	List<TextStyleOffset> getStyleOffsets(LogObject logObject) {
		
		Objects.requireNonNull(logObject);
		
		final List<TextStyleOffset> list = styleOffsets.get(logObject);
	
		return list != null ? Collections.unmodifiableList(list) : null;
	}
	
	void clear() {
		styleOffsets.clear();
	}
}
