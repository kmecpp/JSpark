package com.kmecpp.jspark.parser;

import java.nio.file.Path;
import java.util.ArrayList;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.parser.data.Parameter;
import com.kmecpp.jspark.parser.statement.Import;
import com.kmecpp.jspark.parser.statement.Statement;
import com.kmecpp.jspark.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.parser.statement.block.Method;
import com.kmecpp.jspark.parser.statement.block.module.Class;
import com.kmecpp.jspark.parser.statement.block.module.Module;
import com.kmecpp.jspark.parser.statement.block.module.Static;
import com.kmecpp.jspark.parser.statement.logic.MethodInvocation;
import com.kmecpp.jspark.tokenizer.Token;
import com.kmecpp.jspark.tokenizer.TokenType;
import com.kmecpp.jspark.tokenizer.Tokenizer;

public class Parser {

	private Tokenizer tokenizer;
	private Module module;

	public Parser(Path path, Tokenizer tokenizer) {
		this.tokenizer = tokenizer;

		Token keyword = tokenizer.read(TokenType.KEYWORD);
		module = keyword.is(Keyword.STATIC) ? new Static(path, tokenizer.readName())
				: keyword.is(Keyword.CLASS) ? new Class(path, tokenizer.readName()) : null;
		tokenizer.read(Symbol.OPEN_BRACE);
	}

	public Module parseModule() {
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			//			System.out.println("Parsing token: " + token);

			//KEYWORDS
			if (token.isKeyword()) {
				if (token.is(Keyword.IMPORT)) {
					StringBuilder importStr = new StringBuilder();
					while (!tokenizer.peekNext().is(Symbol.SEMICOLON)) {
						importStr.append(tokenizer.readName());
						importStr.append(tokenizer.read(Symbol.PERIOD));
					}
					int index = importStr.lastIndexOf(".");
					String className = importStr.substring(index == -1 ? 0 : index);

					module.addImport(new Import(importStr.toString(), className));
				}

				else if (token.is(Keyword.DEF)) {
					String name = tokenizer.readName();
					ArrayList<Parameter> params = new ArrayList<>();

					tokenizer.read(Symbol.OPEN_PAREN);
					while (!tokenizer.peekNext().is(Symbol.CLOSE_PAREN)) {
						params.add(new Parameter(tokenizer.readType(), tokenizer.readName()));
					}
					tokenizer.read(Symbol.CLOSE_PAREN);
					tokenizer.read(Symbol.OPEN_BRACE);

					Method method = new Method(module, name, params.toArray(new Parameter[params.size()]));
					method.addStatements(parseBlock(method));

					module.addMethod(method);
					//					module.addStatement(new Method(name, new ArrayList<>()));
				}
				//				if (token.is(Keyword.PUBLIC)) {
				//					module.addStatement(new Method(tokenizer.readName(), new ArrayList<>()));
				//				}

				//				if (token.is(Keyword.CLASS)) {
				//					module.addStatement(new Class(tokenizer.readName()));
				//					parseModule();
				//				} else if (token.is(Keyword.STATIC)) {
				//					module.addStatement(new Static(tokenizer.readName()));
				//					parseModule();
				//				}
			}

			else if (token.isLiteral()) {
				
			}

			//			//SYMBOLS
			//			else if (token.getType() == TokenType.SYMBOL) {
			//
			//			}
			//
			//			//OPERATORS
			//			else if (token.getType() == TokenType.OPERATOR) {
			//
			//			}
			//
			//			//			//LITERALS
			//			//			else if (token.getType() == TokenType.LITERAL) {
			//			//
			//			//			}

			else {
				System.err.println("Could not parse unknown " + (token == null ? "token: null" : token.getType() + " '" + token.getText() + "'"));
			}
		}

		return module;
	}

	private ArrayList<Statement> parseBlock(AbstractBlock parent) {
		ArrayList<Statement> statements = new ArrayList<>();
		while (!tokenizer.peekNext().is(Symbol.CLOSE_BRACE)) {
			Token token = tokenizer.next();

			if (token.getType() == TokenType.IDENTIFIER) {
				if (tokenizer.peekNext().is(Symbol.PERIOD)) {
					String target = token.getText();
					tokenizer.read(Symbol.PERIOD);
					String method = tokenizer.readName();
					tokenizer.read(Symbol.OPEN_PAREN);

					ArrayList<Expression> params = new ArrayList<>();
					while (!tokenizer.peekNext().is(Symbol.CLOSE_PAREN)) {
						ArrayList<Token> expression = new ArrayList<>();
						while (true) {
							expression.add(tokenizer.next());

							if (tokenizer.peekNext().is(Symbol.COMMMA)) {
								tokenizer.read(Symbol.COMMMA);
							} else {
								break;
							}

						}
						params.add(new Expression(expression));
					}
					tokenizer.read(Symbol.CLOSE_PAREN);
					System.out.println(new MethodInvocation(parent, target, method, params));

					statements.add(new MethodInvocation(parent, target, method, params));
				}

				else if (tokenizer.peekNext().is(Symbol.EQUALS)) {

				}
			}
		}
		return statements;

	}

}
