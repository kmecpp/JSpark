package com.kmecpp.jspark.tokenizer;

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

	@Override
	public String toString() {
		return type + ": " + token;
	}
}
