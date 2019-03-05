package com.neaterbits.ide.component.java.language;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.ide.component.common.ComponentProvider;
import com.neaterbits.ide.component.common.Newable;
import com.neaterbits.ide.component.common.NewableCategory;
import com.neaterbits.ide.component.common.NewableType;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.LanguageStyling;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;

public class JavaLanguageComponent
	implements ComponentProvider, LanguageComponent {

	public static final Newable CLASS 		= new Newable(NewableType.FILE, "Class", null);
	public static final Newable INTERFACE 	= new Newable(NewableType.FILE, "Interface", null);
	public static final Newable ENUM		= new Newable(NewableType.FILE, "Enum", null);

	private static final LanguageName NAME = new LanguageName("java");
	
	private static final List<String> FILE_SUFFIXES = Arrays.asList("java");
	
	private static final LanguageStyling STYLING = new JavaLanguageStyling();

	private static final JavaLanguage JAVA_LANGUAGE = new JavaLanguage();
	
	@Override
	public List<NewableCategory> getNewables() {
		return Arrays.asList(
			new NewableCategory("Java", null, Arrays.asList(
				CLASS,
				INTERFACE,
				ENUM
			))
		);
	}

	@Override
	public LanguageName getLanguageName() {
		return NAME;
	}

	@Override
	public Iterable<String> getFileSuffixes() {
		return FILE_SUFFIXES;
	}

	@Override
	public LanguageStyling getStyling() {
		return STYLING;
	}

	@Override
	public ParseableLanguage getParseableLanguage() {
		return JAVA_LANGUAGE;
	}
}
