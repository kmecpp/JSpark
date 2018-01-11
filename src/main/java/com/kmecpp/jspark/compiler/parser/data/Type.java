package com.kmecpp.jspark.compiler.parser.data;

import com.kmecpp.jspark.compiler.tokenizer.Token;
import com.kmecpp.jspark.language.PrimitiveType;

public class Type {

	private final String fullName;
	private final PrimitiveType primitive;

	public static final Type INT = new Type(PrimitiveType.INTEGER);
	public static final Type DEC = new Type(PrimitiveType.DECIMAL);
	public static final Type BOOLEAN = new Type(PrimitiveType.BOOLEAN);
	public static final Type STRING = new Type(PrimitiveType.STRING);

	public Type(String fullName) {
		this.fullName = fullName;
		this.primitive = null;
	}

	public Type(PrimitiveType primitive) {
		this.fullName = primitive.getIdentifier();
		this.primitive = primitive;
	}

	public static Type getType(Token token) {
		PrimitiveType type = token.getPrimitiveType();
		if (type != null) {
			return new Type(type);
		} else {
			return new Type(token.getText());
		}
	}

	public String getFullName() {
		return fullName;
	}

	public String getName() {
		int index = fullName.indexOf(".");
		return fullName.substring(index == -1 ? 0 : index); //Its okay not to cache this data as a field because it will only be used in print outs
	}

	public boolean isClass() {
		return primitive == null;
	}

	public boolean isPrimitive() {
		return primitive != null;
	}

	public boolean isString() {
		return primitive == PrimitiveType.STRING;
	}

	public boolean isInteger() {
		return primitive == PrimitiveType.INTEGER;
	}

	public boolean isDecimal() {
		return primitive == PrimitiveType.DECIMAL;
	}

	public boolean isBoolean() {
		return primitive == PrimitiveType.BOOLEAN;
	}

	public PrimitiveType getPrimitiveType() {
		return primitive;
	}

}
