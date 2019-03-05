package com.neaterbits.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import com.neaterbits.ide.util.ui.text.Text;

public abstract class BaseTextTest {

	protected abstract Text createText(String string);
	
	@Test
	public void testSubstringOneParamArgs() {
		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);

		try {
			text.substring(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(string.length() + 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
	
	@Test
	public void testSubstringOneParam() {

		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);
		
		assertThat(text.substring(0).asString()).isEqualTo(string);
		assertThat(text.substring(4).asString()).isEqualTo(string.substring(4));

		assertThat(text.substring(string.length() - 1).asString()).isEqualTo("t");
		assertThat(text.substring(string.length() - 1).asString()).isEqualTo(string.substring(string.length() - 1));
		
		
		
		assertThat(text.substring(string.length()).asString()).isEqualTo("");
		assertThat(text.substring(string.length()).asString()).isEqualTo(string.substring(string.length()));
	}

	@Test
	public void testSubstringTwoParamsArgs() {
		final String string = "some\n lines\n \nof text";

		final Text text = createText(string);

		try {
			text.substring(-1, 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(0, -1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(-1, -1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(1, 0);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(0, string.length() + 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
	
	@Test
	public void testSubstringTwoParams() {

		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);
		
		assertThat(text.substring(0, string.length()).asString()).isEqualTo(string);
		assertThat(text.substring(4, string.length()).asString()).isEqualTo(string.substring(4));

		assertThat(text.substring(4, string.length() - 3).asString()).isEqualTo(string.substring(4, string.length() - 3));

		assertThat(text.substring(string.length() - 1, string.length()).asString()).isEqualTo("t");
		assertThat(text.substring(string.length() - 1, string.length()).asString()).isEqualTo(string.substring(string.length() - 1));
		
		
		
		assertThat(text.substring(string.length(), string.length()).asString()).isEqualTo("");
		assertThat(text.substring(string.length(), string.length()).asString()).isEqualTo(string.substring(string.length(), string.length()));
	}
}
