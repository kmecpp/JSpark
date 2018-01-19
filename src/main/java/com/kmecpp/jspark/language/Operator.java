package com.kmecpp.jspark.language;

import java.util.HashMap;

import javax.naming.OperationNotSupportedException;

import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;

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
	INCREMENT_DELAYED("++", 1, 4, true),
	DECREMENT_DELAYED("--", 1, 4, true),

	//RELATIONAL (TYPE 2)
	EQUALS("==", 2, 1),
	NOT_EQUALS("!=", 2, 1),
	LESS("<", 2, 1),
	LESS_EQUALS("<=", 2, 1),
	GREATER(">", 2, 1),
	GREATER_EQUALS(">=", 2, 1),

	//BITWISE (TYPE 3)
	BIT_AND("&", 3, 1),
	BIT_OR("|", 3, 1),
	BIT_XOR("", 3, 1),
	BIT_COMPLIMENT("~", 3, 1, true),

	//ASSIGNMENT (TYPE 4)
	ASSIGN("=", 4, 4),
	ASSIGN_PLUS("+=", 4, 4),
	ASSIGN_MINUS("-=", 4, 4),
	ASSIGN_MULTIPLY("*=", 4, 4),
	ASSIGN_DIVIDE("/=", 4, 4),
	ASSIGN_MOD("%=", 4, 4),

	;

	private String string;
	private int type;
	private int precedence;
	private boolean unary;

	private static final HashMap<String, Operator> operators = new HashMap<>();

	static {
		for (Operator operator : values()) {
			operators.put(operator.string, operator);
		}
	}

	private Operator(String string, int type, int precedence) {
		this(string, type, precedence, false);
	}

	private Operator(String string, int type, int precedence, boolean unary) {
		this.string = string;
		this.type = type;
		this.precedence = precedence;
		this.unary = unary;
	}

	public Variable apply(Variable var1, Variable var2) {
		switch (this) {
		case PLUS:
			if (var1.isString()) {
				return new Variable(Type.STRING, ((String) var1.getValue()) + var2.getValue());
			} else if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() + (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() + (double) var2.getValue());
			}
		case MINUS:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() - (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() - (double) var2.getValue());
			}
		case MULTIPLY:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() * (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() * (double) var2.getValue());
			}
		case DIVIDE:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() / (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() / (double) var2.getValue());
			}
		case MODULUS:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() % (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() % (double) var2.getValue());
			}
		case EXPONENT:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) Math.pow((int) var1.getValue(), (int) var2.getValue()));
			} else {
				return new Variable(Type.DEC, Math.pow((double) var1.getValue(), (double) var2.getValue()));
			}

			//RELATIONAL
		case EQUALS:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() == (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() == (double) var2.getValue());
			}
		case NOT_EQUALS:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() != (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() % (double) var2.getValue());
			}
		case LESS:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() < (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() < (double) var2.getValue());
			}
		case LESS_EQUALS:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() <= (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() <= (double) var2.getValue());
			}
		case GREATER:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() > (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() > (double) var2.getValue());
			}
		case GREATER_EQUALS:
			if (var1.isInteger() && var2.isInteger()) {
				return new Variable(Type.INT, (int) var1.getValue() >= (int) var2.getValue());
			} else {
				return new Variable(Type.DEC, (double) var1.getValue() >= (double) var2.getValue());
			}
		default:
			return null;
		}
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

	public int applyInt(Variable var) {
		switch (this) {
		case INCREMENT:
			return (int) var.getValue() + 1;
		case DECREMENT:
			return (int) var.getValue() - 1;
		default:
			throw new RuntimeException(new OperationNotSupportedException("Operator: " + this + " is not unary!"));
		}
	}

	public double applyDouble(Variable var) {
		switch (this) {
		case INCREMENT:
			return (double) var.getValue() + 1;
		case DECREMENT:
			return (double) var.getValue() - 1;
		default:
			throw new RuntimeException(new OperationNotSupportedException("Operator: " + this + " is not unary!"));
		}
	}

	@Override
	public String getString() {
		return string;
	}

	public int getPrecedence() {
		return precedence;
	}

	public boolean isDelayed() {
		return this == INCREMENT_DELAYED || this == Operator.DECREMENT_DELAYED;
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

	public boolean isAssignment() {
		return type == 4;
	}

	public static Operator fromString(String operator) {
		return operators.get(operator);
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
