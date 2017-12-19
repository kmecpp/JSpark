package com.kmecpp.jspark.compiler.parser.data;

public class Variable {

	private Type type;
	private String name;
	private Object value;
	//	private Expression expression;

	//	public Variable(Type type, String name) {
	//		this(type, name, null);
	//	}

	public Variable(Type type, String name, Object value) {//, Expression expression) {
		this.type = type;
		this.name = name;
		this.value = value;
		//		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
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
		return type.getFullName() + " " + name + " = " + value;
	}

}
