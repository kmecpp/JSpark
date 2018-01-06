package com.kmecpp.jspark.compiler.parser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.compiler.tokenizer.LiteralToken;
import com.kmecpp.jspark.compiler.tokenizer.Token;
import com.kmecpp.jspark.compiler.tokenizer.TokenType;
import com.kmecpp.jspark.compiler.tokenizer.Tokenizer;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;

public class Expression {

	private AbstractBlock block;
	private ArrayList<Token> tokens;
	private Type type;

	public Expression(AbstractBlock block, String expression) {
		this(block, Tokenizer.parseTokens(expression));
		type = Type.
	}

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

	public boolean isLiteral() {
		for (Token token : tokens) {
			if (token.is(Symbol.PERIOD)) {
				return false;
			}
		}
		return true;
	}

	public Object evaluate() {
		//		long start = System.nanoTime();
		Stack<Token> operators = new Stack<>();
		Stack<Token> operands = new Stack<>();
		//		System.out.println(tokens);
		System.out.println(tokens);

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
				Variable var = block.getVarData(value.getText());
				if (var != null) {
					return var.getValue();
				} else {
					throw new RuntimeException("Unknown type: " + value);
				}
			}
		} else {
			throw new RuntimeException("Expression could not be evaluated! Stack: " + operands + ", " + operators);
		}
	}

	private Token process(Stack<Token> operands, Stack<Token> operators) {
		//		System.out.println(operators + ", " + operands + "     <----");
		Operator operator = operators.pop().asOperator();
		if (operator.isUnary()) {
			return null;
		} else {
			Object value2 = evaluateToken(operands.pop());
			Object value1 = evaluateToken(operands.pop());

			boolean string = value1 instanceof String || value2 instanceof String;
			boolean integer = value1 instanceof Integer && value2 instanceof Integer;
			//			boolean decimal = !string && !integer;

			System.out.println(string || integer);
			Token result;

			switch (operator) {
			case PLUS:
				return string ? new LiteralToken("" + value1 + value2)
						: integer ? new LiteralToken((int) value1 + (int) value2)
								: new LiteralToken((double) value1 + (double) value2);
			default:
				break;
			}

			if (value1 instanceof String && operator == Operator.PLUS) {
				result = new LiteralToken(((String) value1) + value2);
			} else if (value1 instanceof Integer && value2 instanceof Integer) {
				result = new LiteralToken(operator.apply((int) value1, (int) value2));
			} else {
				result = new LiteralToken(operator.apply(((Number) value1).doubleValue(), ((Number) value2).doubleValue()));
			}

			return operands.push(result);
		}

		//		Token result;
		//
		//		if (value1 instanceof String && operator == Operator.PLUS) {
		//			result = new LiteralToken(((String) value1) + value2);
		//		} else if (value1 instanceof Integer && value2 instanceof Integer) {
		//			result = new LiteralToken(operator.apply((int) value1, (int) value2));
		//		} else {
		//			result = new LiteralToken(operator.apply(((Number) value1).doubleValue(), ((Number) value2).doubleValue()));
		//		}
		//		return operands.push(result);

		//		Token post = operands.pop();
		//		Token first = operands.pop();

		//		if (value1 instanceof String) {
		//			return operands.push(new LiteralToken(operator.apply((String) value1, String.valueOf(value2))));
		//		} else {
		//			return operands.push(new LiteralToken(operator.apply((double) value1, (double) value2)));
		//		}
	}

	private Object evaluateToken(Token token) {
		if (token.isIdentifier()) {
			return block.getVar(token.getText());
		} else if (token.isString()) {
			return token.asString();
		} else {
			return token.asDouble();
		}
	}

	@Override
	public String toString() {
		return tokens.stream()
				.map((token) -> token.isString() ? "\"" + token.getText() + "\"" : String.valueOf(token))
				.collect(Collectors.joining(" "));
	}

}
