package com.kmecpp.jspark.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.data.Value;
import com.kmecpp.jspark.tokenizer.Token;
import com.kmecpp.jspark.tokenizer.TokenType;
import com.kmecpp.jspark.tokenizer.Tokenizer;

public class Expression {

	private ArrayList<Token> tokens;

	public Expression(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}

	public Value evaluate() {
		return evaluate(new HashMap<>());
	}

	public static void main(String[] args) {
		String program = "3.43 + 4 - 5 ";
		System.out.println(new Tokenizer(program).readAll());
		System.out.println(new Expression(new Tokenizer("3 + 4 - 5 ").readAll()).evaluate());
	}

	public Value evaluate(HashMap<String, Value> values) {
		if (tokens.stream().anyMatch(Token::isString)) {
			StringBuilder sb = new StringBuilder();
			for (Token token : tokens) {
				if (token.isString()) {
					sb.append(token.asString());
				} else if (token.is(Operator.PLUS)) {
					continue;
				} else {
					throw new IllegalArgumentException("Unexpected token while parsing string: '" + token.getText() + "'");
				}
			}
			return new Value(Type.STRING, sb);
		} else if (tokens.stream().anyMatch(Token::isBoolean)) {

		} else {

		}

		Stack<Token> operators = new Stack<>();
		Stack<Token> operands = new Stack<>();

		for (Token token : tokens) {
			if (token.getType() == TokenType.OPERATOR) {
				if (operators.isEmpty() || token.is(Symbol.OPEN_PAREN) || operators.peek().asOperator().getPrecedence() < token.asOperator().getPrecedence()) {
					operators.push(token);
				}
			} else if (token.is(Symbol.OPEN_PAREN)) {
				operators.push(token);
			} else if (token.is(Symbol.CLOSE_PAREN)) {

			} else {
				operands.push(token);
			}
		}

		for (Token token : tokens) {
			if (token.getType() == TokenType.IDENTIFIER) {
				return values.get(token.getText());
			} else {
				if (token.isString()) {
					return new Value(Type.STRING, token.asString());
				} else if (token.isBoolean()) {

				} else if (token.isNumber()) {

				} else {

				}

			}
		}
		return null;
	}

}
