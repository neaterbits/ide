package com.neaterbits.ide.common.model.codemap;

import java.util.Objects;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

final class ClassInfo implements TypeSuggestion {

	private final int typeNo;
	private final TypeName typeName;
	private final String namespace;
	private final String binaryName;
	private final TypeVariant typeVariant;
	private final SourceFileResourcePath sourceFileResourcePath;

	ClassInfo(
			int typeNo,
			TypeName typeName,
			String namespace,
			String binaryName,
			SourceFileResourcePath sourceFileResourcePath,
			ClassBytecode classByteCode) {
		
		if (typeNo < 0) {
			throw new IllegalArgumentException();
		}
		
		Objects.requireNonNull(typeName);
		
		this.typeNo = typeNo;
		this.typeName = typeName;
		this.namespace = namespace;
		this.binaryName = binaryName;
		this.typeVariant = classByteCode.getTypeVariant();
		this.sourceFileResourcePath = sourceFileResourcePath;
	}
	
	int getTypeNo() {
		return typeNo;
	}

	@Override
	public TypeVariant getType() {
		return typeVariant;
	}

	@Override
	public String getName() {
		return typeName.getName();
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getBinaryName() {
		return binaryName;
	}

	@Override
	public SourceFileResourcePath getSourceFile() {
		return sourceFileResourcePath;
	}
}
