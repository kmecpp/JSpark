package com.kmecpp.jspark.tokenizer;

public class LiteralToken extends Token {

	private Object value;

	public LiteralToken(double decimal) {
		this(TokenType.DECIMAL_LITERAL, decimal);
	}

	public LiteralToken(int integer) {
		this(TokenType.INTEGER_LITERAL, integer);
	}

	public LiteralToken(boolean bool) {
		this(TokenType.BOOLEAN_LITERAL, bool);
	}

	public LiteralToken(String string) {
		this(TokenType.STRING_LITERAL, string);
	}

	private LiteralToken(TokenType type, Object value) {
		super(type, String.valueOf(value));
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

}
