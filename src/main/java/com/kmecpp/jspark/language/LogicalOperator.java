package com.kmecpp.jspark.language;

public enum LogicalOperator {

	OR("||", 1),
	XOR("^^", 1),
	AND("&&", 1),
	NOT("!", 1),
	DIVIDES("%%", 3),

	;

	private String string;
	private int precedence;

	private LogicalOperator(String character, int precedence) {
		this.string = character;
		this.precedence = precedence;
	}

	public String getString() {
		return string;
	}

	public int getPrecedence() {
		return precedence;
	}

}
