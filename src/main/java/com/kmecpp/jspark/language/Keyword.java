package com.kmecpp.jspark.language;

public enum Keyword {

	THIS,
	CLASS,
	STATIC,
	PUBLIC,
	PRIVATE

	;

	private String value;

	static {
		for (Keyword keyword : values()) {
			keyword.value = keyword.name().toLowerCase();
		}
	}

	public String getValue() {
		return value;
	}

	public static Keyword fromString(String keyword) {
		for (Keyword key : values()) {
			if (key.name().equalsIgnoreCase(keyword)) {
				return key;
			}
		}
		return null;
	}

	public static boolean isKeyword(String str) {
		for (Keyword keyword : values()) {
			if (keyword.value.equals(str)) {
				return true;
			}
		}
		return false;
	}

}
