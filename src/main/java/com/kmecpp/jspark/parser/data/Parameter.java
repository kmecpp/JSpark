package com.kmecpp.jspark.parser.data;

import com.kmecpp.jspark.language.Type;

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
		return type.getIdentifier() + " " + name;
	}

}
