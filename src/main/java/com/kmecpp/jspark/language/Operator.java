package com.kmecpp.jspark.language;

import javax.naming.OperationNotSupportedException;

public enum Operator implements AbstractToken {

	//ARITHMETIC (TYPE 1)
	PLUS("+", 1, 1),
	MINUS("-", 1, 1),
	MULTIPLY("*", 1, 2),
	DIVIDE("/", 1, 2),
	MODULUS("%", 1, 3),
	EXPONENT("^", 1, 3),

	INCREMENT("++", 1, 4, true),
	DECREMENT("--", 1, 4, true),

	//RELATIONAL (TYPE 2)
	EQUALS("==", 2, 1),
	NOT_EQUALS("!=", 2, 1),
	LESS("<", 2, 1),
	LESS_EQUALS("<=", 2, 1),
	GREATER(">", 2, 1),
	GREATER_EQUALS(">=", 2, 1),

	//BITWISE (TYPE 2)
	BIT_AND("&", 3, 1),
	BIT_OR("|", 3, 1),
	BIT_XOR("", 3, 1),

	BIT_COMPLIMENT("~", 3, 1, true),

	;

	private String string;
	private int type;
	private int precedence;
	private boolean unary;

	private Operator(String string, int type, int precedence) {
		this(string, type, precedence, false);
	}

	private Operator(String string, int type, int precedence, boolean unary) {
		this.string = string;
		this.type = type;
		this.precedence = precedence;
		this.unary = unary;
	}

	public Object apply(int a, int b) {
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
			return (int) Math.pow(a, b);

		case EQUALS:
			return a == b;
		case NOT_EQUALS:
			return a != b;
		case LESS:
			return a < b;
		case LESS_EQUALS:
			return a <= b;
		case GREATER:
			return a > b;
		case GREATER_EQUALS:
			return a >= b;

		//		case DIVIDES:
		//			return a % b == 0;
		default:
			throw new RuntimeException(new OperationNotSupportedException("Cannot apply operator to numbers: " + a + " " + this.string + " " + b));
		}
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

	public boolean isUnary() {
		return unary;
	}

	public boolean isArithmetic() {
		return type == 1;
	}

	public boolean isRelational() {
		return type == 2;

	}

	public boolean isBitwise() {
		return type == 3;
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
