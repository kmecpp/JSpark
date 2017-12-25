package com.kmecpp.jspark.language;

public enum Symbol implements AbstractToken {

	COLON(':'),
	EQUALS('='),
	PERIOD('.'),
	COMMMA(','),
	SEMICOLON(';'),
	LESS_THAN('<'),
	GREATER_THAN('>'),
	OPEN_PAREN('('),
	CLOSE_PAREN(')'),
	OPEN_BRACE('{'),
	CLOSE_BRACE('}'),
	QUESTION_MARK('?'),

	;

	private char symbol;

	private Symbol(char symbol) {
		this.symbol = symbol;
	}

	@Override
	public String getString() {
		return String.valueOf(symbol);
	}

	public char getSymbol() {
		return symbol;
	}

	public static boolean isSymbol(char c) {
		for (Symbol symbol : values()) {
			if (symbol.symbol == c) {
				return true;
			}
		}
		return false;
	}

}
