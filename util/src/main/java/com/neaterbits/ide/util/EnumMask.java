package com.neaterbits.ide.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public abstract class EnumMask<T extends Enum<T>> {

	private final int mask;
	
	public EnumMask(@SuppressWarnings("unchecked") T ... values) {
		this(Arrays.asList(values));
	}

	public EnumMask(Collection<T> values) {

		int mask = 0;
		
		for (T value : values) {
			if (value.ordinal() > 31) {
				throw new IllegalArgumentException();
			}
			
			mask |= (1 << value.ordinal());
		}
		
		this.mask = mask;
	}
	
	public final boolean isSet(T value) {
		Objects.requireNonNull(value);

		return (mask & (1 << value.ordinal())) != 0;
	}

	
	@SafeVarargs
	public final boolean isSetOnly(T ... values) {
		Objects.requireNonNull(values);
		
		if (values.length == 0) {
			throw new IllegalArgumentException();
		}

		int expected = 0;
		
		for (T value : values) {
			expected |= 1 << value.ordinal();
		}
		
		return (mask & expected) == expected;
	}
}
