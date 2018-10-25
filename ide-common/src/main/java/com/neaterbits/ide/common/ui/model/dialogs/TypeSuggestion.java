package com.neaterbits.ide.common.ui.model.dialogs;

import java.util.Objects;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public final class TypeSuggestion {

	private final SuggestionType type;
	private final String name;
	private final String namespace;
	private final SourceFileResourcePath sourceFile;

	public TypeSuggestion(SuggestionType type, String name, String namespace, SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(sourceFile);
		
		this.type = type;
		this.name = name;
		this.namespace = namespace;
		this.sourceFile = sourceFile;
	}

	public SuggestionType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getNamespace() {
		return namespace;
	}

	public SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}
}
