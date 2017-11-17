package com.kmecpp.jspark.parser.statements;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.Statement;

public class Variable extends Statement {

	private Type type;
	private String name;
	private Object value;

	public Variable(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	public Variable(String name, Object value) {
		this.name = name;
		this.value = value;
		//TODO What about the type?
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

}
