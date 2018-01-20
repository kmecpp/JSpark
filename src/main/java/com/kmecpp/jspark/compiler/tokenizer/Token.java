package com.kmecpp.jspark.compiler.tokenizer;

import com.kmecpp.jspark.language.AbstractToken;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.PrimitiveType;

public class Token {

	private final TokenType type;
	private final String string;
	//	private final int index;

	public Token(TokenType type, String string) {
		this.type = type;
		this.string = string;
		//		this.index = index;
	}

	public TokenType getType() {
		return type;
	}

	public String getText() {
		return string;
	}

	//	public int getIndex() {
	//		return index;
	//	}

	public boolean isValue() {
		return isLiteral() || type == TokenType.IDENTIFIER;
	}

	//	public Variable asValue(AbstractBlock context) {
	//
	//	}

	public Operator asOperator() {
		return Operator.fromString(string);
	}

	//	public boolean is(Keyword keyword) {
	//		return string.equals(keyword.getString());
	//	}

	public boolean is(AbstractToken tokenText) {
		return string.equals(tokenText.getString());
	}

	public boolean isIdentifier() {
		return type == TokenType.IDENTIFIER;
	}

	public boolean isOperator() {
		return type == TokenType.OPERATOR;
	}

	public boolean isKeyword() {
		return type == TokenType.KEYWORD;
	}

	public boolean isLiteral() {
		return type == TokenType.STRING_LITERAL || type == TokenType.INTEGER_LITERAL || type == TokenType.DECIMAL_LITERAL || type == TokenType.BOOLEAN_LITERAL;
	}

	public boolean isPrimitiveType() {
		return PrimitiveType.isPrimitive(string);
	}

	public PrimitiveType getPrimitiveType() {
		return PrimitiveType.getPrimitiveType(string);
	}

	public boolean isAccessModifier() {
		return string.equals(Keyword.PUBLIC.getString()) || string.equals(Keyword.PRIVATE.getString());
	}

	//	public ObjectValue getValue() {
	//		if (type == TokenType.IDENTIFIER) {
	//			return new ObjectValue(string);
	//		} else if (type == TokenType.KEYWORD) {
	//			return new ObjectValue(Keyword.fromString(string));
	//		} else if (type == TokenType.OPERATOR) {
	//			return new ObjectValue(Operator.fromString(string));
	//		} else if (type == TokenType.LITERAL) {
	//			if (string.startsWith("\"")) {
	//				return new ObjectValue(string);
	//			} else if (string.startsWith("t") || string.startsWith("f")) {
	//				return new ObjectValue(string.equals("true") ? true : false);
	//			} else if (string.contains(".")) {
	//				try {
	//					return new ObjectValue(Integer.parseInt(string));
	//				} catch (NumberFormatException e) {
	//					return new ObjectValue(Long.parseLong(string));
	//				}
	//			} else {
	//				return new ObjectValue(Double.parseDouble(string));
	//			}
	//		}
	//		return new ObjectValue(null);
	//	}

	public boolean asBoolean() {
		if (string.equals("true")) {
			return true;
		} else if (string.equals("false")) {
			return false;
		}
		throw new IllegalArgumentException("Not a boolean! '" + string + "'");
	}

	public boolean isBoolean() {
		return type == TokenType.BOOLEAN_LITERAL;
		//		try {
		//			asBoolean();
		//			return true;
		//		} catch (Exception e) {
		//			return false;
		//		}
	}

	public String asString() {
		return string;
		//		return string.substring(1, string.length() - 1);
	}

	public boolean isString() {
		return type == TokenType.STRING_LITERAL;
		//		return string.startsWith("\"");
	}

	public int asInt() {
		return Integer.parseInt(string);
	}

	public boolean isInt() {
		return type == TokenType.INTEGER_LITERAL;
		//		try {
		//			asInt();
		//			return true;
		//		} catch (Exception e) {
		//			return false;
		//		}
	}

	public long asLong() {
		return Long.parseLong(string);
	}

	public boolean isLong() {
		return type == TokenType.INTEGER_LITERAL;
		//		try {
		//			asLong();
		//			return true;
		//		} catch (Exception e) {
		//			return false;
		//		}
	}

	public float asFloat() {
		return Float.parseFloat(string);
	}

	public boolean isFloat() {
		return type == TokenType.DECIMAL_LITERAL;
		//		try {
		//			asFloat();
		//			return true;
		//		} catch (Exception e) {
		//			return false;
		//		}
	}

	public double asDouble() {
		return Double.parseDouble(string);
	}

	public boolean isDecimal() {
		return type == TokenType.DECIMAL_LITERAL;
		//		try {
		//			asDouble();
		//			return true;
		//		} catch (Exception e) {
		//			return false;
		//		}
	}

	public boolean isNumber() {
		return isInt() || isDecimal();
	}

	@Override
	public String toString() {
		if (isString()) {
			return "\"" + string + "\"";
		}
		return string;
	}

}
