package com.kmecpp.jspark.tokenizer;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;

public enum TokenType {

	SYMBOL,

	LITERAL,

	KEYWORD,

	OPERATOR,

	IDENTIFIER,

	;

	//	DECIMAL_LITERAL,
	//
	//	INTEGER_LITERAL,
	//
	//	STRING_LITERAL,
	//
	//	BOOLEAN_LITERAL;

	public static TokenType getType(String str) {
		if (str == null || str.isEmpty() || str.contains(" ")) {
			throw new IllegalArgumentException("Invalid token format: '" + str + "'");
		}

		if ((str.length() > 1 && str.charAt(0) == '-' && Character.isDigit(str.charAt(1)) || Character.isDigit(str.charAt(0)))
				|| str.startsWith("\"") || str.equals("true") || str.equals("false")) {
			return LITERAL;
		} else if (Keyword.isKeyword(str)) {
			return KEYWORD;
		} else if (Operator.isOperator(str)) {
			return OPERATOR;
		} else {
			return IDENTIFIER;
		}

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
