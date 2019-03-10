package com.neaterbits.ide.common.model.codemap;

import java.util.Objects;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

final class TypeSuggestionImpl implements TypeSuggestion {

	private final TypeVariant typeVariant;
	private final String namespace;
	private final String name;
	private final String binaryName;
	private final SourceFileResourcePath sourceFile;

	TypeSuggestionImpl(TypeVariant typeVariant, String namespace, String name, String binaryName, SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(name);
		
		this.typeVariant = typeVariant;
		this.name = name;
		this.binaryName = binaryName;
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
	public String getBinaryName() {
		return binaryName;
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
