package com.kmecpp.jspark.tokenizer;

public class LiteralToken extends Token {

	private Object value;

	public LiteralToken(String string, TokenType type, Object value) {
		super(string, type);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

}
