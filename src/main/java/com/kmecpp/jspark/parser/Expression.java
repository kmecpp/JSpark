package com.kmecpp.jspark.parser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.parser.data.Variable;
import com.kmecpp.jspark.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.tokenizer.LiteralToken;
import com.kmecpp.jspark.tokenizer.Token;
import com.kmecpp.jspark.tokenizer.TokenType;

public class Expression {

	private AbstractBlock block;
	private ArrayList<Token> tokens;

	public Expression(AbstractBlock block, ArrayList<Token> tokens) {
		this.block = block;
		this.tokens = tokens;
		//		this.variables = block.getVariables();
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public AbstractBlock getBlock() {
		return block;
	}

	//	public Value evaluate() {
	//		return evaluate(new HashMap<>());
	//	}

	public Object evaluate() {
		//		long start = System.nanoTime();
		Stack<Token> operators = new Stack<>();
		Stack<Token> operands = new Stack<>();

		for (Token token : tokens) {
			//			System.out.println(operators + ", " + operands);
			if (token.is(Symbol.OPEN_PAREN)) {
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
			} else {
				operands.push(token);
			}
		}
		while (!operators.isEmpty()) {
			process(operands, operators);
		}
		//		System.out.println(operators + ", " + operands);
		//		System.out.println("Evaluation Time: " + (System.nanoTime()  - start) / 1000000F + "ms");
		if (operands.size() == 1) {
			Token value = operands.pop();
			if (value.isNumber()) {
				double number = value.asDouble();
				if (number % 1 == 0) {
					return (int) number;
					//					return new Value(Type.INTEGER, (int) number);
				} else {
					return number;
					//					return new Value(Type.DECIMAL, number);
				}
			} else if (value.isString()) {
				return value.asString();
				//				return new Value(Type.STRING, value.asString());
			} else if (value.isBoolean()) {
				return value.asBoolean();
				//				return new Value(Type.STRING, value.asBoolean());
			} else {
				//If token is a variable
				Variable var = block.getVariable(value.getText());
				if (var != null) {
					return var.evaluate();
				} else {
					throw new RuntimeException("Unknown type: " + value);
				}
			}
		} else {
			throw new RuntimeException("Expression could not be evaluated! Stack: " + operands + ", " + operators);
		}
	}

	private static Token process(Stack<Token> operands, Stack<Token> operators) {
		//		System.out.println(operators + ", " + operands + "     <----");
		Operator operator = operators.pop().asOperator();

		Token post = operands.pop();
		Token first = operands.pop();

		if (first.isString()) {
			return operands.push(new LiteralToken(operator.apply(first.asString(), post.asString())));
		} else {
			return operands.push(new LiteralToken(operator.apply(first.asDouble(), post.asDouble())));
		}
	}

	@Override
	public String toString() {
		return tokens.stream().map(String::valueOf).collect(Collectors.joining());
	}

}
