package com.kmecpp.jspark.tokenizer;

import com.kmecpp.jspark.ObjectValue;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;

public class Token {

	private String token;
	private TokenType type;

	//	public Token(String token) {
	//		this.token = token;
	//		this.type = TokenType.getType(token);
	//	}

	public Token(String token, TokenType type) {
		this.token = token;
		this.type = type;
	}

	public String getText() {
		return token;
	}

	public TokenType getType() {
		return type;
	}

	public boolean is(Keyword keyword) {
		return token.equals(keyword.getString());
	}

	public ObjectValue getValue() {
		if (type == TokenType.IDENTIFIER) {
			return new ObjectValue(token);
		} else if (type == TokenType.KEYWORD) {
			return new ObjectValue(Keyword.fromString(token));
		} else if (type == TokenType.OPERATOR) {
			return new ObjectValue(Operator.fromString(token));
		} else if (type == TokenType.LITERAL) {
			if (token.startsWith("\"")) {
				return new ObjectValue(token);
			} else if (token.startsWith("t") || token.startsWith("f")) {
				return new ObjectValue(token.equals("true") ? true : false);
			} else if (token.contains(".")) {
				try {
					return new ObjectValue(Integer.parseInt(token));
				} catch (NumberFormatException e) {
					return new ObjectValue(Long.parseLong(token));
				}
			} else {
				return new ObjectValue(Double.parseDouble(token));
			}
		}
		return new ObjectValue(null);
	}

	public boolean asBoolean() {
		if (token.equals("true")) {
			return true;
		} else if (token.equals("false")) {
			return false;
		}
		throw new IllegalArgumentException("Not a boolean! '" + token + "'");
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
		return token.substring(1, token.length() - 1);
	}

	public boolean isString() {
		return token.startsWith("\"");
	}

	public int asInt() {
		return Integer.parseInt(token);
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
		return Long.parseLong(token);
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
		return Float.parseFloat(token);
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
		return Double.parseDouble(token);
	}

	public boolean isDouble() {
		try {
			asDouble();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return type + ": " + token;
	}

}
