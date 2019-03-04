package com.neaterbits.ide.common.ui.model.source;

import java.util.Collection;

import com.neaterbits.ide.util.EnumMask;

final class SourceElementMask extends EnumMask<SourceElementFlag> {

	SourceElementMask(Collection<SourceElementFlag> values) {
		super(SourceElementFlag.class, values);
	}

	SourceElementMask(SourceElementFlag... values) {
		super(SourceElementFlag.class, values);
	}
}
