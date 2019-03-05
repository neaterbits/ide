package com.neaterbits.ide.component.common.language.model;

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
