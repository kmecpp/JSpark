package com.kmecpp.jspark.compiler.parser.data;

public class Parameter {

	private final Type type;
	private final String name;

	public Parameter(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return type.getFullName() + " " + name;
	}

}
