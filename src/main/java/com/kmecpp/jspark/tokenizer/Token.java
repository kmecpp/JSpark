package com.kmecpp.jspark.tokenizer;

import com.kmecpp.jspark.ObjectValue;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.AbstractToken;

public class Token {

	private String string;
	private TokenType type;

	//	public string(String string) {
	//		this.string = string;
	//		this.type = stringType.getType(string);
	//	}

	public Token(String string, TokenType type) {
		this.string = string;
		this.type = type;
	}

	public String getText() {
		return string;
	}

	public TokenType getType() {
		return type;
	}

	//	public boolean is(Keyword keyword) {
	//		return string.equals(keyword.getString());
	//	}

	public boolean is(AbstractToken tokenText) {
		return string.equals(tokenText.getString());
	}

	public boolean isAccessModifier() {
		return string.equals(Keyword.PUBLIC.getString()) || string.equals(Keyword.PRIVATE.getString());
	}

	public ObjectValue getValue() {
		if (type == TokenType.IDENTIFIER) {
			return new ObjectValue(string);
		} else if (type == TokenType.KEYWORD) {
			return new ObjectValue(Keyword.fromString(string));
		} else if (type == TokenType.OPERATOR) {
			return new ObjectValue(Operator.fromString(string));
		} else if (type == TokenType.LITERAL) {
			if (string.startsWith("\"")) {
				return new ObjectValue(string);
			} else if (string.startsWith("t") || string.startsWith("f")) {
				return new ObjectValue(string.equals("true") ? true : false);
			} else if (string.contains(".")) {
				try {
					return new ObjectValue(Integer.parseInt(string));
				} catch (NumberFormatException e) {
					return new ObjectValue(Long.parseLong(string));
				}
			} else {
				return new ObjectValue(Double.parseDouble(string));
			}
		}
		return new ObjectValue(null);
	}

	public boolean asBoolean() {
		if (string.equals("true")) {
			return true;
		} else if (string.equals("false")) {
			return false;
		}
		throw new IllegalArgumentException("Not a boolean! '" + string + "'");
	}

	public boolean isBoolean() {
		try {
			asBoolean();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String asString() {
		return string.substring(1, string.length() - 1);
	}

	public boolean isString() {
		return string.startsWith("\"");
	}

	public int asInt() {
		return Integer.parseInt(string);
	}

	public boolean isInt() {
		try {
			asInt();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public long asLong() {
		return Long.parseLong(string);
	}

	public boolean isLong() {
		try {
			asLong();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public float asFloat() {
		return Float.parseFloat(string);
	}

	public boolean isFloat() {
		try {
			asFloat();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public double asDouble() {
		return Double.parseDouble(string);
	}

	public boolean isDouble() {
		try {
			asDouble();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isNumber() {
		return isDouble();
	}

	@Override
	public String toString() {
		return type + ": " + string;
	}

}
