package com.kmecpp.jspark.language;

public enum Type {

	INT,
	DEC,
	STRING,
	BOOLEAN,

	;

	public static Type fromString(String text) {
		for (Type type : values()) {
			if (type.name().toLowerCase().equals(text)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Given string is not a valid type: '" + text + "'");
	}

}
