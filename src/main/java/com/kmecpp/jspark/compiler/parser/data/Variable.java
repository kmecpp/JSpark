package com.kmecpp.jspark.compiler.parser.data;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Variable {

	private final Type type;
	private final String name;
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

	public boolean isDeclared() {
		return name != null;
	}

	public boolean isList() {
		return type.isList();
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
		//		if (expression != null) {
		//			Object value = expression.evaluate();
		//			expression = null;
		//			return value;
		//		}
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Variable clone() {
		return new Variable(type, name, value);
	}

	@Override
	public String toString() {
		if (type.isList()) {
			return "ArrayList " + name + " = new ArrayList(Arrays.asList(" +
					((ArrayList<?>) value).stream().map(String::valueOf).collect(Collectors.joining(", "))
					+ "))";
		}

		return type.getFullName() + (name != null ? " " + name : "") + (value != null ? " = " + value : "");
	}

}
