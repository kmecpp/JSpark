package com.kmecpp.jspark.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.data.Value;
import com.kmecpp.jspark.tokenizer.Token;
import com.kmecpp.jspark.tokenizer.TokenType;

public class Expression {

	private ArrayList<Token> tokens;

	public Expression(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}

	public Value evaluate() {
		return evaluate(new HashMap<>());
	}

	public Value evaluate(HashMap<String, Value> values) {
		for (Token token : tokens) {

			if (token.getType() == TokenType.IDENTIFIER) {
				return values.get(token.getText());
			} else {
				String text = token.getText();
				if (token.isString()) {
					return new Value(Type.STRING, text);
				} else if (token.isBoolean()) {

				} else if (token.isNumber()) {

				} else {

				}

			}
			if (token.isString()) {

			}
		}
		return null;
	}

}
