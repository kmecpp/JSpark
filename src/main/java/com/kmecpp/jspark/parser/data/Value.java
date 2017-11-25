package com.kmecpp.jspark.parser.data;

import com.kmecpp.jspark.language.Type;

public class Value {

	protected Type type;
	protected Object value;

	public Value(Type type) {
		this(type, null);
	}

	public Value(Type type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return type.getIdentifier() + " = " + value;
	}

}
