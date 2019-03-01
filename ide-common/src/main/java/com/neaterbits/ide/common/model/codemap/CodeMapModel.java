package com.neaterbits.ide.common.model.codemap;

import com.neaterbits.compiler.common.resolver.codemap.IntCodeMap;

public class CodeMapModel {

	private final IntCodeMap codeMap;

	CodeMapModel() {
		this.codeMap = new IntCodeMap();
	}
	
	IntCodeMap getCodeMap() {
		return codeMap;
	}
}

