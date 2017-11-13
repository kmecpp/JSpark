package com.kmecpp.jspark.language;

public enum Symbol {

	COLON(":"),
	SEMICOLON(";"),
	OPEN_PAREN("("),
	CLOSE_PAREN(")"),
	OPEN_BRACE("{"),
	CLOSE_BRACE("}"),
	QUESTION_MARK("?"),

	;

	private String symbol;

	private Symbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

}
