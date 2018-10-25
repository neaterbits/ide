package com.neaterbits.ide.component.java.language;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.ide.component.common.ComponentProvider;
import com.neaterbits.ide.component.common.Newable;
import com.neaterbits.ide.component.common.NewableCategory;
import com.neaterbits.ide.component.common.NewableType;
import com.neaterbits.ide.component.common.language.LanguageComponent;

public class JavaLanguageComponent
	implements ComponentProvider, LanguageComponent {

	public static final Newable CLASS 		= new Newable(NewableType.FILE, "Class", null);
	public static final Newable INTERFACE 	= new Newable(NewableType.FILE, "Interface", null);
	public static final Newable ENUM		= new Newable(NewableType.FILE, "Enum", null);
	
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
}
