package com.kmecpp.jspark.parser.data;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.Expression;

public class Variable {

	private Type type;
	private String name;
	private Expression expression;

	//	public Variable(Type type, String name) {
	//		this(type, name, null);
	//	}

	public Variable(Type type, String name, Expression expression) {
		this.type = type;
		this.name = name;
		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public Expression getExpression() {
		return expression;
	}

	public Object evaluate() {
		return expression.evaluate();
	}

	@Override
	public String toString() {
		return type.getIdentifier() + " " + name + " = " + expression;
	}

}
