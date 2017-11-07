package com.kmecpp.jspark.tokenizer;

import com.kmecpp.jspark.language.Keyword;

public class Token {

	private String token;
	private TokenType type;

	public Token(String token) {
		this.token = token;
		this.type = TokenType.getType(token);
	}

	public String getToken() {
		return token;
	}

	public TokenType getType() {
		return type;
	}

	public boolean is(Keyword keyword) {
		return token.equals(keyword.getValue());
	}

	@Override
	public String toString() {
		return type + ": " + token;
	}

}
