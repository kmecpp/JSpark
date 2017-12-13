package com.kmecpp.jspark.language;

public enum Keyword implements AbstractToken {

	INT,
	DEC,
	STRING,
	BOOLEAN,

	DEF,
	FOR,
	NEW,
	THIS,
	CLASS,
	STATIC,
	IMPORT,
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
			if (key.name().toLowerCase().equals(keyword)) {
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
