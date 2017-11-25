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
		String program = "1 - (3 - 1 + 8) / 2^2 ";//"3.43 - (4 + 5) ";
		System.out.println(new Tokenizer(program).readAll());
		System.out.println(new Expression(new Tokenizer(program).readAll()).evaluate());
	}

	public Value evaluate(HashMap<String, Value> values) {
		long start = System.nanoTime();
		//
		//		if (tokens.stream().anyMatch(Token::isString)) {
		//			StringBuilder sb = new StringBuilder();
		//			for (Token token : tokens) {
		//				if (token.isString()) {
		//					sb.append(token.asString());
		//				} else if (token.is(Operator.PLUS)) {
		//					continue;
		//				} else {
		//					throw new IllegalArgumentException("Unexpected token while parsing string: '" + token.getText() + "'");
		//				}
		//			}
		//			return new Value(Type.STRING, sb);
		//		} else if (tokens.stream().anyMatch(Token::isBoolean)) {
		//
		//		} else {
		Stack<Token> operators = new Stack<>();
		Stack<Token> operands = new Stack<>();

		for (Token token : tokens) {
			System.out.println(operators + ", " + operands);
			if (token.isNumber()) {
				operands.push(token);
			} else if (token.is(Symbol.OPEN_PAREN)) {
				operators.push(token);
			} else if (token.getType() == TokenType.OPERATOR) {
				while (!operators.isEmpty() && !operators.peek().is(Symbol.OPEN_PAREN) && operators.peek().asOperator().getPrecedence() >= token.asOperator().getPrecedence()) {
					process(operands, operators);
				}
				operators.push(token);
			} else if (token.is(Symbol.CLOSE_PAREN)) {
				while (!operators.peek().is(Symbol.OPEN_PAREN)) {
					process(operands, operators);
				}
				operators.pop();
			}
		}
		while (!operators.isEmpty()) {
			process(operands, operators);
		}
		System.out.println(operators + ", " + operands);
		System.out.println("Hi" + 3 * 4);
		System.out.println("Time: " + (System.nanoTime() - start) / 1000000F + "ms");
		if (operands.size() == 1) {
			Token value = operands.pop();
			if (value.isNumber()) {
				double number = value.asDouble();
				if (number % 1 == 0) {
					return new Value(Type.INTEGER, (int) number);
				} else {
					return new Value(Type.DECIMAL, value);
				}
			} else if (value.isString()) {
				return new Value(Type.STRING, value.asString());
			} else if (value.isBoolean()) {
				return new Value(Type.STRING, value.asBoolean());
			} else {
				throw new RuntimeException("Unknown type: " + value);
			}
		} else {
			throw new RuntimeException("Expression could not be evaluated! Stack: " + operands + ", " + operators);
		}

		//		for (Token token : tokens) {
		//			if (token.getType() == TokenType.IDENTIFIER) {
		//				return values.get(token.getText());
		//			} else {
		//				if (token.isString()) {
		//					return new Value(Type.STRING, token.asString());
		//				} else if (token.isBoolean()) {
		//
		//				} else if (token.isNumber()) {
		//
		//				} else {
		//
		//				}
		//
		//			}
		//		}
		//		return null;
	}

	private static Token process(Stack<Token> operands, Stack<Token> operators) {
		System.out.println(operators + ", " + operands + "     <----");
		Operator operator = operators.pop().asOperator();

		Token post = operands.pop();
		Token first = operands.pop();
		if (first.isString()) {
			return new Token(operator.apply(first.asString(), post.asString()), TokenType.STRING_LITERAL);
		} else {
			return operands.push(new Token(String.valueOf(operator.apply(first.asDouble(), post.asDouble())), TokenType.DECIMAL_LITERAL));
		}
	}

}
