package com.kmecpp.jspark.compiler.parser.data;

import com.kmecpp.jspark.compiler.tokenizer.Token;
import com.kmecpp.jspark.language.PrimitiveType;

public class Type {

	private final String fullName;
	private final PrimitiveType primitive;

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

	public boolean isPrimitive() {
		return primitive != null;
	}

	public PrimitiveType getPrimitiveType() {
		return primitive;
	}

}
