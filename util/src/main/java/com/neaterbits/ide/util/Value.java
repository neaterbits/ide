package com.neaterbits.ide.util;

import java.util.Objects;

public class Value<T> {

	private T value;

	public Value() {

	}
	
	public Value(T value) {

		Objects.requireNonNull(value);
		
		this.value = value;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}

	public boolean isNotNull() {
	    return value != null;
	}
	
	public void setNull() {
	    this.value = null;
	}
}
