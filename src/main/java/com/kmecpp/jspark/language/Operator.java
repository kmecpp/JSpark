package com.kmecpp.jspark.language;

public enum Operator {

	PLUS("+"),
	MINUS("-"),
	MULTIPLY("*"),
	DIVIDE("/"),
	MODULUS("%"),
	EXPONENT("^"),
	DIVIDES("|"),
	TERNARY_TRUE("?"),
	TERNARY_FALSE(":"),

	;

	private String character;

	private Operator(String character) {
		this.character = character;
	}

	public String getCharacter() {
		return character;
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
