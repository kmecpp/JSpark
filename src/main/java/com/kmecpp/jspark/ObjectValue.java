package com.kmecpp.jspark;

public class ObjectValue {

	private Object value;

	public ObjectValue(Object value) {
		this.value = value;
	}

	public boolean asBoolean() {
		return (boolean) value;
	}

	public boolean isBoolean() {
		return value instanceof Boolean;
	}

	public String asString() {
		return (String) value;
	}

	public boolean isString() {
		return value instanceof String;
	}

	public int asInt() {
		return (int) value;
	}

	public boolean isInt() {
		return value instanceof Integer;
	}

	public long asLong() {
		return (long) value;
	}

	public boolean isLong() {
		return value instanceof Long;
	}

	public float asFloat() {
		return (float) value;
	}

	public boolean isFloat() {
		return value instanceof Float;
	}

	public double asDouble() {
		return (double) value;
	}

	public boolean isDouble() {
		return value instanceof Double;
	}

}
