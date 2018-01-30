package com.kmecpp.jspark.compiler.parser;

import java.util.ArrayList;
import java.util.LinkedList;

import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.compiler.parser.statement.block.AnonymousBlock;
import com.kmecpp.jspark.compiler.tokenizer.Token;
import com.kmecpp.jspark.language.AbstractToken;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Symbol;

public class ListParser {

	private final AbstractBlock block;
	private final LinkedList<Token> tokens;
	private final boolean comprehension;

	public ListParser(AbstractBlock block, LinkedList<Token> tokens, boolean comprehension) {
		this.block = block;
		this.tokens = tokens;
		this.comprehension = comprehension;
	}

	public Variable parse() {
		ArrayList<Object> list = new ArrayList<>();
		if (comprehension) { //[x : 10 if x % 2 == 0]
			AnonymousBlock listScope = new AnonymousBlock(block);
			ArrayList<Token> expressionTokens = new ArrayList<>();
			Expression listStart = null, listEnd = null;
			ArrayList<Token> conditionTokens = new ArrayList<>();
			Variable variable = null;

			int part = 0;
			while (!tokens.isEmpty()) {
				Token token = tokens.pop();

				if (part == 0) {
					if (token.is(Keyword.FOR)) {
						part++;
					} else {
						expressionTokens.add(token);
						if (token.isIdentifier() && !block.isVariableDefined(token.getText())) {
							if (listScope.getVariables().size() > 0) {
								error("List comprehension can only have one variable! Found two: " + listScope.getVariables().keySet().toArray()[0] + ", " + token.getText());
							}
							variable = new Variable(Type.INT, token.getText(), 0);
							listScope.getVariables().put(token.getText(), variable);
						}
					}

				} else if (part == 1) {
					//					if (token.is(Keyword.IF)) {
					//						part++;
					//					} else {
					if (token.isInt()) {
						listStart = new Expression(listScope, "0");
						read(Symbol.PERIOD);
						read(Symbol.PERIOD);
						listEnd = new Expression(listScope, token.asString());
					} else {
						listStart = readExpression(listScope, Symbol.COMMMA);
						listEnd = readExpression(listScope, Symbol.CLOSE_PAREN);
					}

					if (tokens.peek().is(Keyword.IF)) {
						tokens.pop();
						part++;
					} else if (tokens.size() > 0) {
						error("Unexpected token parsing list: '" + tokens.peek() + "'");
					}
				} else {
					conditionTokens.add(token);
				}
			}

			final Expression expression = new Expression(listScope, expressionTokens);
			//			final int max = (int) new Expression(block, rangeTokens).evaluate();
			final Expression condition = conditionTokens.isEmpty() ? null : new Expression(listScope, conditionTokens);
			for (int i = (int) listStart.evaluate(); i < (int) listEnd.evaluate(); i++) {
				variable.setValue(i);
				if (condition == null || (boolean) condition.evaluate()) {
					list.add(expression.evaluate());
				}
			}
		} else {
			while (hasNext()) {
				Token token = next();
				if (token.isInt()) {
					list.add(token.asInt());
				} else if (token.isDecimal()) {
					list.add(token.asDouble());
				}

				if (hasNext()) {
					read(Symbol.COMMMA);
				}
			}
		}

		//		if (tokens.size() > 0) {
		//			Token first = tokens.pop();
		//
		//			if (first.isIdentifier()) {
		//				read(Symbol.COLON);
		//				int tokens.pop().asInt();
		//			} else {
		//				list.add(first);
		//				while (!tokens.isEmpty()) {
		//					Token token = tokens.pop();
		//					System.out.print(token);
		//				}
		//			}
		//
		//			System.out.println(tokens);
		//
		//			System.out.println();
		//		}

		return new Variable(Type.LIST, list);
	}

	//		if (first.isIdentifier()) {
	//			tokens.get
	//			new Tokenizer
	//		}
	//		if (first.isDecimal()) {
	//			for (int i = 0; i < list.size(); i++) {
	//				Token token = tokens.get(i);
	//				list.add(token.asDouble());
	//				if (i < list.size() && tokens.get(i + 1).is(Symbol.COMMMA)) {
	//					i++;
	//				}
	//			}
	//		} else if (first.isInt()) {
	//			if (tokens.size() > 0) {
	//
	//			}
	//		} else {
	//
	//		}
	//
	//		for (Token token : tokens) {
	//			if (token.isDecimal()) {
	//
	//			}
	//		}

	public Token next() {
		return tokens.pop();
	}

	public boolean hasNext() {
		return !tokens.isEmpty();
	}

	public Expression readExpression(AbstractBlock block, AbstractToken end) {
		ArrayList<Token> expression = new ArrayList<>();
		Token token = null;
		while (!(token = next()).is(end)) {
			expression.add(token);
		}
		return new Expression(block, expression);
	}

	public void read(AbstractToken token) {
		if (!hasNext()) {
			error("Reached end of list. Cannot read token: " + token);
		} else {
			Token t = tokens.pop();
			if (!t.is(token)) {
				error("Error parsing token '" + t + "'! Expected: '" + token + "'");
			}
		}
	}

	public void error(String message) {
		throw new RuntimeException(message);
	}

}
