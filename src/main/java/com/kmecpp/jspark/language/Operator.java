package com.kmecpp.jspark.language;

public enum Operator implements AbstractToken {

	PLUS("+"),
	MINUS("-"),
	MULTIPLY("*"),
	DIVIDE("/"),
	MODULUS("%"),
	EXPONENT("^"),
	DIVIDES("|"),
	INVOCATION("."),

	;

	private String string;

	private Operator(String character) {
		this.string = character;
	}

	@Override
	public String getString() {
		return string;
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
