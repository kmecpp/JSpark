package com.kmecpp.jspark.compiler.parser.data;

public class Variable {

	protected Type type;
	protected String name;
	private Object value;

	public Variable(Type type, Object value) {
		this(type, null, value);
	}

	public Variable(Type type, String name, Object value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public boolean isInteger() {
		return type.isInteger();
	}

	public boolean isDecimal() {
		return type.isDecimal();
	}

	public boolean isBoolean() {
		return type.isBoolean();
	}

	public boolean isString() {
		return type.isString();
	}

	public boolean isObject() {
		return type.isClass();
	}

	public boolean isPrimitiveType() {
		return type.isPrimitive();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return type.getFullName() + " " + name + " = " + value;
	}

}
