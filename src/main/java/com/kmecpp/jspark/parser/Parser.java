package com.kmecpp.jspark.parser;

import java.util.ArrayList;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.parser.data.Value;
import com.kmecpp.jspark.parser.data.Variable;
import com.kmecpp.jspark.parser.statements.block.Method;
import com.kmecpp.jspark.parser.statements.block.module.Class;
import com.kmecpp.jspark.parser.statements.block.module.Module;
import com.kmecpp.jspark.parser.statements.block.module.Static;
import com.kmecpp.jspark.parser.statements.logic.MethodInvocation;
import com.kmecpp.jspark.tokenizer.Token;
import com.kmecpp.jspark.tokenizer.TokenType;
import com.kmecpp.jspark.tokenizer.Tokenizer;

public class Parser {

	private Tokenizer tokenizer;
	private Module module;

	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;

		Token keyword = tokenizer.read(TokenType.KEYWORD);
		module = keyword.is(Keyword.STATIC) ? new Static(tokenizer.readName())
				: keyword.is(Keyword.CLASS) ? new Class(tokenizer.readName()) : null;
		tokenizer.read(Symbol.OPEN_BRACE);
	}

	public Module parseModule() {
		System.out.println("Parsing module: " + module.getName());
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			System.out.println("Parsing token: " + token);

			//KEYWORDS
			if (token.getType() == TokenType.KEYWORD) {
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
					ArrayList<Variable> params = new ArrayList<>();

					tokenizer.read(Symbol.OPEN_PAREN);
					while (!tokenizer.peekNext().is(Symbol.CLOSE_PAREN)) {
						params.add(new Variable(tokenizer.readType(), tokenizer.readName()));
					}
					tokenizer.read(Symbol.CLOSE_PAREN);
					tokenizer.read(Symbol.OPEN_BRACE);

					module.addMethod(new Method(name, params, parseBlock()));
					//					module.addStatement(new Method(name, new ArrayList<>()));
				}
				//				if (token.is(Keyword.PUBLIC)) {
				//					module.addStatement(new Method(tokenizer.readName(), new ArrayList<>()));
				//				}

				if (token.is(Keyword.CLASS)) {
					module.addStatement(new Class(tokenizer.readName()));
					parseModule();
				} else if (token.is(Keyword.STATIC)) {
					module.addStatement(new Static(tokenizer.readName()));
					parseModule();
				}
			}

			//SYMBOLS
			else if (token.getType() == TokenType.SYMBOL) {

			}

			//OPERATORS
			else if (token.getType() == TokenType.OPERATOR) {

			}

			//LITERALS
			else if (token.getType() == TokenType.LITERAL) {

			}

			else {
				System.err.println("Could not parse unknown " + (token == null ? "token: null" : token.getType() + " '" + token.getText() + "'"));
			}
		}

		return module;
	}

	private ArrayList<Statement> parseBlock() {
		ArrayList<Statement> statements = new ArrayList<>();
		while (!tokenizer.peekNext().is(Symbol.CLOSE_BRACE)) {
			Token token = tokenizer.next();

			if (token.getType() == TokenType.IDENTIFIER) {
				if (tokenizer.peekNext().is(Symbol.PERIOD)) {
					String target = token.getText();
					tokenizer.read(Symbol.PERIOD);
					String method = tokenizer.readName();
					tokenizer.read(Symbol.OPEN_PAREN);

					ArrayList<Value> params = new ArrayList<>();
					while (!tokenizer.peekNext().is(Symbol.CLOSE_PAREN)) {
						params.add(new Variable(tokenizer.readType(), tokenizer.readName()));
					}
					tokenizer.read(Symbol.CLOSE_PAREN);

					new MethodInvocation(target, method, params);
				}

				else if (tokenizer.peekNext().is(Symbol.EQUALS)) {

				}
			}
		}
		return statements;

	}

}
