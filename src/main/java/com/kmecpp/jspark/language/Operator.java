package com.kmecpp.jspark.language;

import javax.naming.OperationNotSupportedException;

public enum Operator implements AbstractToken {

	PLUS("+", 1),
	MINUS("-", 1),
	MULTIPLY("*", 2),
	DIVIDE("/", 2),
	MODULUS("%", 3),
	EXPONENT("^", 3),

	//	DIVIDES("%%", 3),
	//	OR("||", 1),
	//	XOR("^^", 1),
	//	AND("&&", 1),
	//	NOT("!", 1),

	;

	private String string;
	private int precedence;

	private Operator(String character, int precedence) {
		this.string = character;
		this.precedence = precedence;
	}

	public double apply(double a, double b) {
		switch (this) {
		case PLUS:
			return a + b;
		case MINUS:
			return a - b;
		case MULTIPLY:
			return a * b;
		case DIVIDE:
			return a / b;
		case MODULUS:
			return a % b;
		case EXPONENT:
			return Math.pow(a, b);
		//		case DIVIDES:
		//			return a % b == 0;
		default:
			throw new RuntimeException(new OperationNotSupportedException("Cannot apply operator to numbers: " + a + " " + this.string + " " + b));
		}
	}

	public String apply(String a, Object b) {
		if (this == PLUS) {
			return a + b;
		}
		throw new RuntimeException(new OperationNotSupportedException("Cannot apply operator to strings: " + a + " " + this.string + " " + b));
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
