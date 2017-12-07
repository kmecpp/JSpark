package com.kmecpp.jspark.parser.statement.block.module;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.Expression;
import com.kmecpp.jspark.parser.data.Value;

public class Field {

	private Type type;
	private String name;
	private Value value;

	public Field(Type type, String name, Expression expression) {
		this.type = type;
		this.name = name;
		this.value = expression == null ? null : expression.evaluate(); //TODO: Add previously defined fields to expression namespace
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return type.getIdentifier() + " " + name + " = " + value;
	}

}
