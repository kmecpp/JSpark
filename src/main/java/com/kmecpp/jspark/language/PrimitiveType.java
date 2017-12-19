package com.kmecpp.jspark.language;

public enum PrimitiveType {

	INTEGER("int", 0),
	DECIMAL("dec", 0.0),
	STRING("string", ""),
	BOOLEAN("boolean", false),

	OBJECT(null, null),

	;

	private final String identifier;
	private final Object defaultValue;

	private PrimitiveType(String identifier, Object defaultValue) {
		this.identifier = identifier;
		this.defaultValue = defaultValue;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public static boolean isPrimitive(String text) {
		if (text != null) {
			for (PrimitiveType type : values()) {
				if (text.equals(type.identifier)) {
					return true;
				}
			}
		}
		return false;
	}

	public static PrimitiveType getPrimitiveType(String text) {
		if (text != null) {
			for (PrimitiveType type : values()) {
				if (text.equals(type.identifier)) {
					return type;
				}
			}
		}
		return null;
		//		throw new IllegalArgumentException("Given string is not a valid type: '" + text + "'");
	}

}
