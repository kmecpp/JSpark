package com.kmecpp.jspark.language;

public enum Keyword implements TokenText {

	THIS,
	CLASS,
	STATIC,
	PUBLIC,
	PRIVATE

	;

	private String string;

	static {
		for (Keyword keyword : values()) {
			keyword.string = keyword.name().toLowerCase();
		}
	}

	@Override
	public String getString() {
		return string;
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
			if (keyword.string.equals(str)) {
				return true;
			}
		}
		return false;
	}

}
