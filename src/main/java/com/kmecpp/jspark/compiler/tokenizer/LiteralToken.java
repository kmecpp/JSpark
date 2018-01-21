package com.kmecpp.jspark.compiler.tokenizer;

public class LiteralToken extends Token {

	private Object value;

	public LiteralToken(int index, double decimal) {
		this(index, TokenType.DECIMAL_LITERAL, decimal);
	}

	public LiteralToken(int index, int integer) {
		this(index, TokenType.INTEGER_LITERAL, integer);
	}

	public LiteralToken(int index, boolean bool) {
		this(index, TokenType.BOOLEAN_LITERAL, bool);
	}

	public LiteralToken(int index, String string) {
		this(index, TokenType.STRING_LITERAL, string);
	}

	private LiteralToken(int index, TokenType type, Object value) {
		super(index, type, String.valueOf(value));
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

}
