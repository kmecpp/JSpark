package com.kmecpp.jspark.language;

public enum Type {

	INTEGER("int"),
	DECIMAL("dec"),
	STRING("string"),
	BOOLEAN("boolean"),

	OBJECT(null),

	;

	private final String identifier;

	private Type(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public static Type getPrimitiveType(String text) {
		if (text != null) {
			for (Type type : values()) {
				if (type.identifier.equals(text)) {
					return type;
				}
			}
		}
		throw new IllegalArgumentException("Given string is not a valid type: '" + text + "'");
	}

}
