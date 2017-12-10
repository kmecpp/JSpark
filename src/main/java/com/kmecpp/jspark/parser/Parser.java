package com.kmecpp.jspark.parser;

import java.nio.file.Path;
import java.util.ArrayList;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.data.Parameter;
import com.kmecpp.jspark.parser.data.Variable;
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
import com.kmecpp.jspark.util.FileUtil;

public class Parser {

	private Tokenizer tokenizer;
	private Module module;

	public Parser(Path path) {
		this.tokenizer = new Tokenizer(FileUtil.readFile(path));

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

				else if (token.isType()) {
					readVariableDeclaration(module); //Parse Fields
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

			else if (token.is(Symbol.CLOSE_BRACE)) {
				if (tokenizer.hasNext()) {
					error("Encounted extra closing brace: " + token);
				}
			}

			else {
				error("Could not parse unknown " + (token == null ? "token: null" : token.getType() + " '" + token.getText() + "'"));
			}
		}
		//		System.out.println(module.getFields());
		return module;
	}

	private ArrayList<Statement> parseBlock(AbstractBlock parentBlock) {
		ArrayList<Statement> statements = new ArrayList<>();
		while (!tokenizer.peekNext().is(Symbol.CLOSE_BRACE)) {
			Token token = tokenizer.next();

			if (token.isType()) {
				readVariableDeclaration(parentBlock);
			}

			if (token.getType() == TokenType.IDENTIFIER) {
				//Method invocations
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
						params.add(new Expression(parentBlock, expression));
					}
					tokenizer.read(Symbol.CLOSE_PAREN);
					System.out.println(new MethodInvocation(parentBlock, target, method, params));

					statements.add(new MethodInvocation(parentBlock, target, method, params));
				}

				//Variable assignment
				else if (tokenizer.peekNext().is(Symbol.EQUALS)) {

				}
			}
		}
		tokenizer.read(Symbol.CLOSE_BRACE);
		return statements;
	}

	public Variable readVariableDeclaration(AbstractBlock block) {
		Type type = tokenizer.getCurrentToken().getPrimitiveType();
		String name = tokenizer.readName();
		Expression expression = null;
		System.out.println("Reading Variable DEC: " + name);
		System.out.println("Block: " + block);

		if (tokenizer.peekNext().is(Symbol.EQUALS)) {
			tokenizer.read(Symbol.EQUALS);

			ArrayList<Token> expressionTokens = new ArrayList<>();
			while (!tokenizer.peekNext().is(Symbol.SEMICOLON)) {
				expressionTokens.add(tokenizer.next());
			}
			tokenizer.read(Symbol.SEMICOLON);
			expression = new Expression(block, expressionTokens);
			return block.defineVariable(new Variable(type, name, expression.evaluate()));
		} else {
			tokenizer.read(Symbol.SEMICOLON);
			return block.defineVariable(new Variable(type, name, type.getDefaultValue()));
		}
		//		return new Variable(type, name, expression.evaluate());
	}

	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	public Module getModule() {
		return module;
	}

	private void error(String message) {
		throw new ParseException(this, message);
	}

}
