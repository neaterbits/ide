package com.neaterbits.ide.common.ui.keys;

public class Key {
	public static final int F1 	= 0x10000001;
	public static final int F2 	= 0x10000002;
	public static final int F3 	= 0x10000003;
	public static final int F4 	= 0x10000004;
	public static final int F5 	= 0x10000005;
	public static final int F6 	= 0x10000006;
	public static final int F7 	= 0x10000007;
	public static final int F8 	= 0x10000008;
	public static final int F9 	= 0x10000009;
	public static final int F10 = 0x10000010;
	public static final int F11 = 0x10000011;
	public static final int F12 = 0x10000012;
	
	private final char character;
	private final int keyCode;

	public Key(char character, int keyCode) {
		this.character = character;
		this.keyCode = keyCode;
	}

	public char getCharacter() {
		return character;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
