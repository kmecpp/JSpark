package com.kmecpp.jspark.runtime;

import com.kmecpp.jspark.language.PrimitiveType;

public class Value {

	protected PrimitiveType type;
	protected Object value;

	public Value(PrimitiveType type) {
		this(type, null);
	}

	public Value(PrimitiveType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public PrimitiveType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return type.getIdentifier() + " = " + value;
	}

}
