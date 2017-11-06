package com.kmecpp.jspark.tokenizer;

public enum TokenType {

	LITERAL,

	KEYWORD,

	OPERATOR,

	IDENTIFIER,

	;

	public static final String[] KEYWORDS = new String[] { "static", "class" };

	public static final String[] OPERATORS = new String[] { "+", "-", "*", "/", "%", "^", "|", "?", ":", "" };

	//	DECIMAL_LITERAL,
	//
	//	INTEGER_LITERAL,
	//
	//	STRING_LITERAL,
	//
	//	BOOLEAN_LITERAL;

	public static boolean isIn(String[] list, String str) {
		for (String keyword : KEYWORDS) {
			if (keyword.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static TokenType getType(String str) {
		if (str == null || str.isEmpty() || str.contains(" ")) {
			throw new IllegalArgumentException("Invalid token format: '" + str + "'");
		}

		return str.startsWith("\"") || Character.isDigit(str.charAt(0)) || str.charAt(0) == '-' || str.equals("true") || str.equals("false")
				? LITERAL
				: isIn(KEYWORDS, str) ? KEYWORD
						: isIn(OPERATORS, str) ? OPERATOR
								: IDENTIFIER;

		//		if (str.startsWith("\"")) {
		//			return STRING_LITERAL;
		//		} else if (Character.isDigit(str.charAt(0))) {
		//			return str.contains(".") ? DECIMAL_LITERAL : INTEGER_LITERAL;
		//		} else if (str.length() == 1 && !Character.isLetterOrDigit(str.charAt(0))) {
		//			return CHARACTER;
		//		} else {
		//			Optional.empty().
		//			return IDENTIFIER;
		//		}
		//		throw new IllegalArgumentException("Invalid token: '" + str + "'");

	}

}
