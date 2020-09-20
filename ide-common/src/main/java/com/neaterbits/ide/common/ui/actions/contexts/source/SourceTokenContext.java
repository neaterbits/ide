package com.neaterbits.ide.common.ui.actions.contexts.source;

import java.util.Objects;

import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.SourceTokenType;
import com.neaterbits.ide.component.common.language.model.ISourceTokenProperties;

public class SourceTokenContext extends SourceContext {

	private final ISourceToken token;
	private final ISourceTokenProperties tokenProperties;

	public SourceTokenContext(ISourceToken token, ISourceTokenProperties tokenProperties) {

		Objects.requireNonNull(token);
		Objects.requireNonNull(tokenProperties);
		
		this.token = token;
		this.tokenProperties = tokenProperties;
	}

	public SourceTokenType getTokenType() {
		return token.getTokenType();
	}

	public ISourceTokenProperties getTokenProperties() {
		return tokenProperties;
	}
}
