package com.neaterbits.ide.component.java.language;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.ide.component.common.instantiation.InstantiationComponent;
import com.neaterbits.ide.component.common.instantiation.Newable;
import com.neaterbits.ide.component.common.instantiation.NewableCategory;
import com.neaterbits.ide.component.common.instantiation.NewableType;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.LanguageStyling;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;

public class JavaLanguageComponent
	implements InstantiationComponent, LanguageComponent {

	public static final Newable CLASS 		= new Newable(NewableType.FILE, "Class", null);
	public static final Newable INTERFACE 	= new Newable(NewableType.FILE, "Interface", null);
	public static final Newable ENUM		= new Newable(NewableType.FILE, "Enum", null);

	public static final LanguageName LANGUAGE_NAME = new LanguageName("java");
	
	private static final List<String> FILE_SUFFIXES = Arrays.asList("java");
	
	private static final LanguageStyling STYLING = new JavaTokenLanguageStyling(); // new JavaRegexpLanguageStyling();

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
		return LANGUAGE_NAME;
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getLanguage(Class<T> languageInterface) {
        return (T)JAVA_LANGUAGE;
    }
}
