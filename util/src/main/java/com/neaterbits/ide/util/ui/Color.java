package com.neaterbits.ide.util.ui;

public class Color {

	private final int r;
	private final int g;
	private final int b;
	
	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public final int getR() {
		return r;
	}

	public final int getG() {
		return g;
	}

	public final int getB() {
		return b;
	}

	@Override
	public String toString() {
		return "Color [r=" + r + ", g=" + g + ", b=" + b + "]";
	}
}
