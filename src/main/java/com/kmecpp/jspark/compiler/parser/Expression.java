package com.kmecpp.jspark.compiler.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.compiler.tokenizer.OperatorToken;
import com.kmecpp.jspark.compiler.tokenizer.Token;
import com.kmecpp.jspark.compiler.tokenizer.Tokenizer;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;

public class Expression {

	private AbstractBlock block;
	private ArrayList<Token> tokens;
	private Type resultType;

	public Expression(AbstractBlock block, String expression) {
		this(block, Tokenizer.parseTokens(expression));
	}

	public Expression(AbstractBlock block, ArrayList<Token> tokens) {
		this.block = block;
		this.tokens = tokens;

		for (Token token : tokens) {
			if (token.isString()) {
				resultType = Type.STRING;
				break;
			} else if (token.isDecimal()) {
				resultType = Type.DEC;
				break;
			} else if (token.isBoolean()) {
				resultType = Type.BOOLEAN;
				break;
			}
		}
		resultType = Type.INT;
		//		this.variables = block.getVariables();
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public AbstractBlock getBlock() {
		return block;
	}

	public Type getResultType() {
		return resultType;
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
		Stack<Variable> operands = new Stack<>();

		Token previousToken = null;
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			//			System.out.println(operators + ", " + operands);
			if (token.is(Symbol.OPEN_PAREN)) {
				if (previousToken != null && previousToken.isIdentifier()) {
					//TODO: METHOD INVOCATIONS
				} else {
					operators.push(token);
				}
			} else if (token.isOperator() || token.is(Keyword.NEW)) {
				Operator operator = token.asOperator();
				while (!operators.isEmpty() && !operators.peek().is(Symbol.OPEN_PAREN) && operators.peek().asOperator().getPrecedence() >= operator.getPrecedence()) {
					process(operands, operators);
				}

				if (operator.isUnary() && i > 0) {
					Token previous = tokens.get(i - 1);
					if (operator == Operator.INCREMENT) {
						operators.push(new OperatorToken(token.getIndex(), previous.isIdentifier() ? Operator.INCREMENT_DELAYED : Operator.INCREMENT));
					} else {
						operators.push(new OperatorToken(token.getIndex(), previous.isIdentifier() ? Operator.DECREMENT_DELAYED : Operator.DECREMENT));
					}
				} else {
					operators.push(token);
				}
			} else if (token.is(Symbol.CLOSE_PAREN)) {
				while (!operators.peek().is(Symbol.OPEN_PAREN)) {
					process(operands, operators);
				}
				operators.pop();
			} else if (token.isIdentifier()) {
				if (tokens.get(i + 1).is(Symbol.OPEN_PAREN)) {
					//TODO
					//					block.getModule().executeStaticMethod(token.getText(), )
				}
				Variable variable = block.getVariable(token.getText());
				if (variable == null) {
					throw new RuntimeException("Variable '" + token.getText() + "' is is undefined in the expression \"" + toString() + "\"");
				} else {
					operands.push(variable);
				}
			} else if (token.is(Symbol.OPEN_BRACKET)) {
				ListParser listParser = parseList(i);
				i += listParser.getOriginalIndexOffset();
				operands.push(listParser.parse());
			} else {
				operands.push(token.asVariable());
				//				System.err.println("Unknown token in expression: '" + token + "'");
			}
			previousToken = token;
		}
		while (!operators.isEmpty()) {
			process(operands, operators);
		}
		//		System.out.println(operators + ", " + operands);
		//		System.out.println("Evaluation Time: " + (System.nanoTime()  - start) / 1000000F + "ms");
		if (operands.size() == 1) {
			return operands.pop().getValue();

			//			Token value = operands.pop();
			//			if (value.isInt()) {
			//				return value.asInt();
			//			} else if (value.isDecimal()) {
			//				return value.asDouble();
			//			}
			//			//			if (value.isNumber()) {
			//			//				double number = value.asDouble();
			//			//				if (number % 1 == 0) {
			//			//					return (int) number;
			//			//					//					return new Value(Type.INTEGER, (int) number);
			//			//				} else {
			//			//					return number;
			//			//					//					return new Value(Type.DECIMAL, number);
			//			//				}
			//			//			} 
			//			else if (value.isString()) {
			//				return value.asString();
			//				//				return new Value(Type.STRING, value.asString());
			//			} else if (value.isBoolean()) {
			//				return value.asBoolean();
			//				//				return new Value(Type.STRING, value.asBoolean());
			//			} else {
			//				//If token is a variable
			//				Variable var = block.getVariable(value.getText());
			//				if (var != null) {
			//					return var.getValue();
			//				} else {
			//					throw new RuntimeException("Unknown type: " + value);
			//				}
			//			}
		} else {
			throw new RuntimeException("Expression could not be evaluated! Operands: " + operands + ", Operators: " + operators);
		}
	}

	private ListParser parseList(int index) {
		LinkedList<Token> listTokens = new LinkedList<>();
		boolean comprehension = false;
		for (int j = index + 1;; j++) {
			Token listToken = tokens.get(j);
			if (listToken.is(Symbol.CLOSE_BRACKET)) {
				break;
			} else if (listToken.is(Keyword.FOR)) {
				comprehension = true;
			}
			listTokens.add(listToken);
		}
		return new ListParser(block, listTokens, comprehension);
	}

	//	private void process(Stack<Token> operands, Stack<Token> operators) {
	//		Object obj = processImpl(operands, operators);
	//		if (obj instanceof Boolean) {
	//			operands.push(new LiteralToken((boolean) obj));
	//		} else if (obj instanceof Integer) {
	//			operands.push(new LiteralToken((int) obj));
	//		} else if (obj instanceof Double) {
	//			operands.push(new LiteralToken((double) obj));
	//		} else {
	//			operands.push(new LiteralToken((String) obj));
	//		}
	//	}

	/*
	 * PROCESS THE STACK
	 */
	private void process(Stack<Variable> operands, Stack<Token> operators) {
		//		System.out.println(operators + ", " + operands + "     <----");
		Operator operator = operators.pop().asOperator();
		if (operator.isUnary()) {
			//			System.out.println("Unary!");

			Variable var = operands.pop();

			if (!var.isDeclared()) {
				System.err.println("Cannot apply unary operator to literal: \"" + var.getValue() + "\"");
			}

			if (operator.isDelayed()) {
				if (var.getType().isInteger()) {
					operands.push(var.clone());
					var.setValue(operator.applyInt(var));
				} else {
					operands.push(var.clone());
					var.setValue(operator.applyDouble(var));
				}
			} else {
				if (var.getType().isInteger()) {
					var.setValue(operator.applyInt(var));
					operands.push(var.clone());
				} else {
					var.setValue(operator.applyDouble(var));
					operands.push(var.clone());
				}
			}

			//			Object value = evaluateToken(token);

			//			if (value instanceof Integer) {
			//				int newValue = operator.apply((int) value);
			//				operands.push(new LiteralToken(newValue));
			//				block.getVariable(token.getText()).setValue(newValue);
			//			} else {
			//				double newValue = operator.apply((double) value);
			//				operands.push(new LiteralToken(newValue));
			//				block.getVariable(token.getText()).setValue(newValue);
			//			}

		} else {

			//			Object value2 = evaluateToken(operands.pop());
			//			Object value1 = evaluateToken(operands.pop());

			Variable var2 = operands.pop();
			Variable var1 = operands.pop();

			//			Object result = operator.apply(var1, var);

			operands.push(operator.apply(var1, var2));

			//			if(var1.isString() && operator == Operator.PLUS) {
			//				operands.push(var1.setValue(var1.getValue() + var2.getValue()))
			//			}
			//
			//			if (value1 instanceof String && operator == Operator.PLUS) {
			//				operands.push(new LiteralToken(((String) value1) + value2));
			//			} else if (value1 instanceof Integer && value2 instanceof Integer) {
			//				if (operator.isArithmetic()) {
			//					operands.push(new LiteralToken((int) operator.apply((int) value1, (int) value2)));
			//				} else {
			//					operands.push(new LiteralToken((boolean) operator.apply((int) value1, (int) value2)));
			//				}
			//			} else {
			//				operands.push(new LiteralToken(operator.apply(((Number) value1).doubleValue(), ((Number) value2).doubleValue())));
			//			}
		}

	}

	//	private Object evaluateToken(Token token) {
	//		if (token.isIdentifier()) {
	//			return block.getValue(token.getText());
	//		} else if (token.isBoolean()) {
	//			return token.asBoolean();
	//		} else if (token.isString()) {
	//			return token.asString();
	//		} else if (token.isInt()) {
	//			return token.asInt();
	//		} else {
	//			return token.asDouble();
	//		}
	//	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Token lastToken = null;
		//		String listName = "";
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			//			if (token.is(Symbol.OPEN_BRACKET)) {
			//				ListParser parser = parseList(i);
			//				i += parser.getOriginalIndexOffset();
			//				//				String listName = block.getAvailableVariableName("list");
			//				StringBuilder listStr = new StringBuilder("ArrayList " + listName + " = ");
			//				if (parser.isComprehension()) {
			//					listStr.append("new ArrayList();");
			//					ListSpec spec = parser.getSpec();
			//					listStr.append("for(int i = " + spec.getMin() + "; i < " + spec.getMax() + "; i++){");
			//					if (spec.getCondition() != null) {
			//						listStr.append("if(" + spec.getCondition() + ") {");
			//						listStr.append(listName + ".add(" + spec.getExpression() + "); \n}");
			//					} else {
			//						listStr.append(listName + ".add(" + spec.getExpression() + ");");
			//					}
			//					listStr.append("}}\n");
			//				} else {
			//					listStr.append("new ArrayList(Arrays.asList("
			//							+ ((ArrayList<?>) parser.parse().getValue()).stream().map(String::valueOf).collect(Collectors.joining(", "))
			//							+ "))");
			//				}
			//				sb.append(listStr);
			//				break;
			//			}

			if (token.isString()) {
				sb.append(token.toString());
			}

			else if (token.isOperator() && token.asOperator().isUnary()) {
				if (lastToken != null && lastToken.isIdentifier()) {
					sb.deleteCharAt(sb.length() - 1);
					sb.append(token.getText());
				} else {
					sb.append(token.getText());
				}
			} else {
				sb.append(token.getText() + " ");
			}

			lastToken = token;
		}
		return sb.toString().trim();

		//		return tokens.stream()
		//				.map((token) -> token.isString() ? "\"" + token.getText() + "\"" : String.valueOf(token))
		//				.collect(Collectors.joining(" "));
	}

}

//package com.kmecpp.jspark.compiler.parser;
//
//import java.util.ArrayList;
//import java.util.Stack;
//import java.util.stream.Collectors;
//
//import com.kmecpp.jspark.compiler.parser.data.Variable;
//import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
//import com.kmecpp.jspark.compiler.tokenizer.Token;
//import com.kmecpp.jspark.compiler.tokenizer.TokenType;
//import com.kmecpp.jspark.compiler.tokenizer.Tokenizer;
//import com.kmecpp.jspark.language.Operator;
//import com.kmecpp.jspark.language.Symbol;
//
//public class Expression {
//
//	private AbstractBlock block;
//	private ArrayList<Token> tokens;
//	//	private Type type;
//
//	public Expression(AbstractBlock block, String expression) {
//		this(block, Tokenizer.parseTokens(expression));
//	}
//
//	public Expression(AbstractBlock block, ArrayList<Token> tokens) {
//		this.block = block;
//		this.tokens = tokens;
//		//		this.variables = block.getVariables();
//	}
//
//	public ArrayList<Token> getTokens() {
//		return tokens;
//	}
//
//	public AbstractBlock getBlock() {
//		return block;
//	}
//
//	//	public Value evaluate() {
//	//		return evaluate(new HashMap<>());
//	//	}
//
//	public boolean isLiteral() {
//		for (Token token : tokens) {
//			if (token.is(Symbol.PERIOD)) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	public Object evaluate() {
//		//		long start = System.nanoTime();
//
//		Stack<Token> operators = new Stack<>();
//		Stack<Object> operands = new Stack<>();
//		//		System.out.println(tokens);
//		System.out.println(tokens);
//
//		for (Token token : tokens) {
//			System.out.println(operators + ", " + operands);
//			if (token.is(Symbol.OPEN_PAREN)) {
//				operators.push(token);
//			} else if (token.getType() == TokenType.OPERATOR) {
//				while (!operators.isEmpty() && !operators.peek().is(Symbol.OPEN_PAREN) && operators.peek().asOperator().getPrecedence() >= token.asOperator().getPrecedence()) {
//					process(operands, operators);
//				}
//				operators.push(token);
//			} else if (token.is(Symbol.CLOSE_PAREN)) {
//				while (!operators.peek().is(Symbol.OPEN_PAREN)) {
//					process(operands, operators);
//				}
//				operators.pop();
//			} else {
//				if (token.isInt()) {
//					operands.push(token.asInt());
//				} else if (token.isDecimal()) {
//					operands.push(token.asDouble());
//				} else if (token.isString()) {
//					operands.push(token.asString());
//				} else {
//					operands.push(token);
//				}
//			}
//		}
//		while (!operators.isEmpty()) {
//			process(operands, operators);
//		}
//		//		System.out.println(operators + ", " + operands);
//		//		System.out.println("Evaluation Time: " + (System.nanoTime()  - start) / 1000000F + "ms");
//		if (operands.size() == 1) {
//			Object value = operands.pop();
//			if (value instanceof Integer) { // value.isNumber()) {
//				return (int) value;
//				//				double number = (double) value.asDouble();
//				//				if (number % 1 == 0) {
//				//					return (int) number;
//				//					//					return new Value(Type.INTEGER, (int) number);
//				//				} else {
//				//					return number;
//				//					//					return new Value(Type.DECIMAL, number);
//				//				}
//			} else if (value instanceof Double) {
//				return (double) value;
//			} else if (value instanceof String) {
//				//			 else if (value.isString()) {
//				return value; //value.asString();
//				//				return new Value(Type.STRING, value.asString());
//			} else if (value instanceof Boolean) { //value.isBoolean()) {
//				return (boolean) value; //value.asBoolean();
//				//				return new Value(Type.STRING, value.asBoolean());
//			} else {
//				//If token is a variable
//				Variable var = block.getVarData(String.valueOf(value));
//				if (var != null) {
//					return var.getValue();
//				} else {
//					throw new RuntimeException("Unknown type: " + value);
//				}
//			}
//		} else {
//			throw new RuntimeException("Expression could not be evaluated! Stack: " + operands + ", " + operators);
//		}
//	}
//
//	private Object process(Stack<Object> operands, Stack<Token> operators) {
//		//		System.out.println(operators + ", " + operands + "     <----");
//		Operator operator = operators.pop().asOperator();
//		if (operator.isUnary()) {
//			return null;
//		} else {
//			Object value2 = evaluateObject(operands.pop()); //evaluateToken(operands.pop());
//			Object value1 = evaluateObject(operands.pop()); //evaluateToken(operands.pop());
//
//			boolean string = value1 instanceof String || value2 instanceof String;
//			boolean integer = value1 instanceof Integer && value2 instanceof Integer;
//			//			boolean decimal = !string && !integer;
//
//			Object result;
//
//			System.out.println("VALUE: " + value1 + ", " + value2.getClass());
//
//			if (string) {
//				return "" + value1 + value2;
//			}
//
//			switch (operator) {
//			case PLUS:
//				if (integer) {
//					result = (int) value1 + (int) value2;
//				} else {
//					result = (double) value1 + (double) value2;
//				}
//				break;
//			case MINUS:
//				if (integer) {
//					result = (int) value1 - (int) value2;
//				} else {
//					result = (double) value1 - (double) value2;
//				}
//				break;
//			case MULTIPLY:
//				if (integer) {
//					result = (int) value1 * (int) value2;
//				} else {
//					result = (double) value1 * (double) value2;
//				}
//				break;
//			case DIVIDE:
//				if (integer) {
//					result = (int) value1 / (int) value2;
//				} else {
//					result = (double) value1 / (double) value2;
//				}
//				break;
//			default:
//				result = null;
//				break;
//			}
//
//			//			if (value1 instanceof String && operator == Operator.PLUS) {
//			//				result = new LiteralToken(((String) value1) + value2);
//			//			} else if (value1 instanceof Integer && value2 instanceof Integer) {
//			//				result = new LiteralToken(operator.apply((int) value1, (int) value2));
//			//			} else {
//			//				result = new LiteralToken(operator.apply(((Number) value1).doubleValue(), ((Number) value2).doubleValue()));
//			//			}
//
//			System.out.println("RESULT: " + result);
//			return operands.push(result);
//		}
//
//		//		Token result;
//		//
//		//		if (value1 instanceof String && operator == Operator.PLUS) {
//		//			result = new LiteralToken(((String) value1) + value2);
//		//		} else if (value1 instanceof Integer && value2 instanceof Integer) {
//		//			result = new LiteralToken(operator.apply((int) value1, (int) value2));
//		//		} else {
//		//			result = new LiteralToken(operator.apply(((Number) value1).doubleValue(), ((Number) value2).doubleValue()));
//		//		}
//		//		return operands.push(result);
//
//		//		Token post = operands.pop();
//		//		Token first = operands.pop();
//
//		//		if (value1 instanceof String) {
//		//			return operands.push(new LiteralToken(operator.apply((String) value1, String.valueOf(value2))));
//		//		} else {
//		//			return operands.push(new LiteralToken(operator.apply((double) value1, (double) value2)));
//		//		}
//	}
//
//	private Object evaluateObject(Object obj) {
//		if (obj instanceof Token) {
//			return block.getVar(((Token) obj).getText());
//		}
//		return obj;
//	}
//
//	private Object evaluateToken(Token token) {
//		if (token.isIdentifier()) {
//			return block.getVar(token.getText());
//		} else if (token.isString()) {
//			return token.asString();
//		} else if (token.isInt()) {
//			return token.asInt();
//		} else {
//			return token.asDouble();
//		}
//	}
//
//	@Override
//	public String toString() {
//		return tokens.stream()
//				.map((token) -> token.isString() ? "\"" + token.getText() + "\"" : String.valueOf(token))
//				.collect(Collectors.joining(" "));
//	}
//
//}
