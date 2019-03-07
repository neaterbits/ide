package com.neaterbits.ide.common.model.codemap;

import java.util.Objects;

import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

final class TypeSuggestionImpl implements TypeSuggestion {

	private final TypeVariant typeVariant;
	private final String namespace;
	private final String name;
	private final SourceFileResourcePath sourceFile;

	TypeSuggestionImpl(TypeVariant typeVariant, String namespace, String name, SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(sourceFile);
		
		this.typeVariant = typeVariant;
		this.name = name;
		this.namespace = namespace;
		this.sourceFile = sourceFile;
	}

	@Override
	public TypeVariant getType() {
		return typeVariant;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}

	@Override
	public String toString() {
		return "TypeSuggestionImpl [typeVariant=" + typeVariant + ", name=" + name + ", namespace=" + namespace
				+ ", sourceFile=" + sourceFile + "]";
	}
}
