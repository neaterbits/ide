package com.neaterbits.ide.common.ui.keys;

import java.util.Collection;

import com.neaterbits.ide.util.EnumMask;

public final class KeyMask extends EnumMask<QualifierKey> {

	public KeyMask(QualifierKey... values) {
		super(QualifierKey.class, values);
	}

	public KeyMask(Collection<QualifierKey> values) {
		super(QualifierKey.class, values);
	}
}
