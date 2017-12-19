package com.kmecpp.jspark.compiler.parser.data;

import com.kmecpp.jspark.language.PrimitiveType;

public class Parameter {

	private final PrimitiveType type;
	private final String name;

	public Parameter(PrimitiveType type, String name) {
		this.type = type;
		this.name = name;
	}

	public PrimitiveType getType() {
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
