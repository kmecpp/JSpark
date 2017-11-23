package com.kmecpp.jspark.parser.data;

import com.kmecpp.jspark.language.Type;

public class Variable extends Value {

	private String name;

	public Variable(Type type, String name) {
		this(type, name, null);
	}

	public Variable(Type type, String name, Object value) {
		super(type, value);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return type + " " + name + (value == null ? "" : " = " + value);
	}

}
