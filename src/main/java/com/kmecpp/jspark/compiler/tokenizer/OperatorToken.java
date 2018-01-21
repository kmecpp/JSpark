package com.kmecpp.jspark.compiler.tokenizer;

import com.kmecpp.jspark.language.Operator;

public class OperatorToken extends Token {

	private final Operator operator;

	public OperatorToken(int index, Operator operator) {
		super(index, TokenType.OPERATOR, operator.getString());
		this.operator = operator;
	}

	@Override
	public Operator asOperator() {
		return operator;
	}

}
