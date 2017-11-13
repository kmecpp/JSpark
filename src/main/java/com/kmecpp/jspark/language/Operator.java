package com.kmecpp.jspark.language;

public enum Operator {

	PLUS("+"),
	MINUS("-"),
	MULTIPLY("*"),
	DIVIDE("/"),
	MODULUS("%"),
	EXPONENT("^"),
	DIVIDES("|"),
	INVOCATION("."),

	;

	private String character;

	private Operator(String character) {
		this.character = character;
	}

	public String getCharacter() {
		return character;
	}

	public static Operator fromString(String operator) {
		for (Operator op : values()) {
			if (op.character.equalsIgnoreCase(operator)) {
				return op;
			}
		}
		return null;
	}

	public static boolean isOperator(String str) {
		for (Operator operator : values()) {
			if (operator.character.equals(str)) {
				return true;
			}
		}
		return false;
	}

}
