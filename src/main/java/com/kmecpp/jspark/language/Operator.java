package com.kmecpp.jspark.language;

public enum Operator implements AbstractToken {

	PLUS("+", 1),
	MINUS("-", 1),
	MULTIPLY("*", 2),
	DIVIDE("/", 2),
	MODULUS("%", 3),
	EXPONENT("^", 3),
	DIVIDES("|", 3),

	OR("||", 1),
	XOR("^", 1),
	AND("&&", 1),
	NOT("!", 1),

	;

	private String string;
	private int precedence;

	private Operator(String character, int precedence) {
		this.string = character;
		this.precedence = precedence;
	}

	@Override
	public String getString() {
		return string;
	}

	public int getPrecedence() {
		return precedence;
	}

	public static Operator fromString(String operator) {
		for (Operator op : values()) {
			if (op.string.equalsIgnoreCase(operator)) {
				return op;
			}
		}
		return null;
	}

	public static boolean isOperator(String str) {
		for (Operator operator : values()) {
			if (operator.string.equals(str)) {
				return true;
			}
		}
		return false;
	}

}
