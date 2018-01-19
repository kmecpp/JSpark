package com.kmecpp.jspark.compiler.parser;

import java.nio.file.Path;
import java.util.ArrayList;

import com.kmecpp.jspark.compiler.parser.data.Parameter;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.statement.Import;
import com.kmecpp.jspark.compiler.parser.statement.MethodInvocation;
import com.kmecpp.jspark.compiler.parser.statement.VariableAssignment;
import com.kmecpp.jspark.compiler.parser.statement.VariableDeclaration;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.compiler.parser.statement.block.AnonymousBlock;
import com.kmecpp.jspark.compiler.parser.statement.block.Loop;
import com.kmecpp.jspark.compiler.parser.statement.block.Method;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Class;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Static;
import com.kmecpp.jspark.compiler.tokenizer.InvalidTokenException;
import com.kmecpp.jspark.compiler.tokenizer.Token;
import com.kmecpp.jspark.compiler.tokenizer.Tokenizer;
import com.kmecpp.jspark.language.AbstractToken;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.util.FileUtil;

public class Parser {

	private Path path;

	private Tokenizer tokenizer;
	private Module module;

	public Parser(Path path) {
		this.path = path;
	}

	public Module parseModule() {
		this.tokenizer = new Tokenizer(FileUtil.readFile(path));
		Token moduleType = tokenizer.next();
		if (moduleType.is(Keyword.STATIC)) {
			module = new Static(path, tokenizer.readName());
		} else if (moduleType.is(Keyword.CLASS)) {
			module = new Class(path, tokenizer.readName());
		} else {
			error("Invalid class definition!");
		}
		tokenizer.read(Symbol.OPEN_BRACE);
		//		Token keyword = tokenizer.read(TokenType.KEYWORD);
		//		module = keyword.is(Keyword.STATIC) ? new Static(path, tokenizer.readName())
		//				: keyword.is(Keyword.CLASS) ? new Class(path, tokenizer.readName()) : null;

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

					module.addImport(new Import(module, importStr.toString(), className));
				}

				else if (token.isPrimitiveType()) {
					module.addStatement(parseVariableDeclaration(module)); //Parse Fields
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
					parseStatements(method);

					module.addMethod(method);
					//					module.addStatement(new Method(name, new ArrayList<>()));
				}

				else {
					error("Unexpected keyword: " + token.getText());
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

	private void parseStatements(AbstractBlock block) {
		//		int open = 0;
		//		ArrayList<Statement> statements = new ArrayList<>();
		//		int lastSize = -1;
		while (true) {
			//			if(statements.size() > lastSize) {
			//				lastSize = statements.size();
			//			}else {
			//				System.out.println(statements);
			//				return statements;
			//			}

			Token token = tokenizer.next();

			//new stream().invoke(a -> new stream().)
			//TODO: Does this even work?
			//			if (token.is(Symbol.OPEN_PAREN) || token.is(Symbol.OPEN_BRACE)) {
			//				open++;
			//			} else if (token.is(Symbol.CLOSE_PAREN) || token.is(Symbol.CLOSE_BRACE)) {
			//				if (--open < 0) {
			//					System.out.println("BREAKKK!!");
			//					break;
			//				}
			//			}

			if (token.isPrimitiveType()) {
				block.addStatement(parseVariableDeclaration(block));
			}

			else if (token.isIdentifier()) {
				Token nextToken = tokenizer.peekNext();
				//Method invocations
				if (nextToken.is(Symbol.PERIOD)) {
					block.addStatement(parseMethodInvocation(block));
				}

				else if (nextToken.isOperator()) {
					Operator operator = nextToken.asOperator();

					//Unary operators
					if (operator.isUnary()) {
						block.addStatement(new VariableAssignment(block, token.getText(), new Expression(block, token.getText() + operator.getString())));
						tokenizer.next();
						tokenizer.read(Symbol.SEMICOLON);
						//						block.addStatement(new VariableAssignment(block, token.getText(), new Expression(block, token.getText() + (operator == Operator.INCREMENT ? "+1" : "-1"))));
					}

					//Variable assignment
					else if (operator.isAssignment()) {
						tokenizer.next();
						block.addStatement(new VariableAssignment(block, token.getText(), tokenizer.readExpression(block, Symbol.SEMICOLON)));
						System.out.println(block.getStatements().get(block.getStatements().size() - 1));
						System.out.println("Assign!");
					}

					else {
						System.err.println("Unexpected operator: " + operator);
					}
				}

				else if (tokenizer.peekNext().isIdentifier()) {
					block.addStatement(parseVariableDeclaration(block));
				}

				else {
					System.err.println("Unknown identifier while parsing block: '" + token + "'");
				}
			}

			else if (token.isKeyword()) {
				//LOOPS
				if (token.is(Keyword.FOR) || token.is(Keyword.WHILE)) {
					Loop loop = new Loop(block);

					if (token.is(Keyword.FOR)) {
						if (tokenizer.peekNext().isInt()) {
							loop.setInitialization(new VariableDeclaration(loop, Type.INT, "i", new Expression(loop, "0")));
							loop.setTermination(new Expression(loop, "i < " + tokenizer.next().asInt()));
							AnonymousBlock increment = new AnonymousBlock(loop);
							increment.addStatement(new VariableAssignment(loop, "i", new Expression(loop, "i + 1")));
							loop.setIncrement(increment);
							tokenizer.read(Symbol.OPEN_BRACE);
						} else {
							tokenizer.read(Symbol.OPEN_PAREN);
							tokenizer.next();

							loop.setInitialization(parseVariableDeclaration(loop));
							loop.setTermination(tokenizer.readExpression(loop, Symbol.SEMICOLON));
							AnonymousBlock increment = new AnonymousBlock(loop);
							parseStatements(increment);
							loop.setIncrement(increment);
						}
					} else {
						loop.setTermination(tokenizer.readExpression(loop, Symbol.OPEN_BRACE));
					}
					parseStatements(loop);
					block.addStatement(loop);
					tokenizer.read(Symbol.CLOSE_BRACE);
				} else {
					System.err.println("Unknown keyword: " + token.getText());
				}
			}

			else {
				return;
				//				error("Invalid start of statement: '" + token + "'");
			}
		}
		//		tokenizer.read(Symbol.CLOSE_BRACE);
		//				return statements;
	}

	public MethodInvocation parseMethodInvocation(AbstractBlock block) {
		String target = tokenizer.getCurrentToken().getText();
		tokenizer.read(Symbol.PERIOD);
		String method = tokenizer.readName();
		tokenizer.read(Symbol.OPEN_PAREN);

		ArrayList<Expression> params = new ArrayList<>();
		while (!tokenizer.getCurrentToken().is(Symbol.CLOSE_PAREN)) {
			params.add(tokenizer.readExpression(block, Symbol.COMMMA));
			//			ArrayList<Token> expression = new ArrayList<>();
			//			while (true) {
			//				expression.add(tokenizer.next());
			//
			//				if (tokenizer.peekNext().is(Symbol.COMMMA)) {
			//					tokenizer.read(Symbol.COMMMA);
			//				} else {
			//					break;
			//				}
			//
			//			}
			//			params.add(new Expression(block, expression));
		}
		//		tokenizer.read(Symbol.CLOSE_PAREN);
		tokenizer.read(Symbol.SEMICOLON);
		//		System.out.println(new MethodInvocation(block, target, method, params));

		return new MethodInvocation(block, target, method, params);
	}

	/*
	 * int i = 3 + this.getChicken().deathCount();
	 * 
	 * Parser:
	 * var i = 3 +
	 * Invoke this.getChicken();
	 * Invoke {result}.deathCount();
	 * 
	 * AST:
	 * var = {
	 * name: i
	 * expression = 3 + this.getChicken().deathCount();
	 * }
	 * 
	 * Runtime: var = var.getExpression().evaluate();
	 * 
	 */
	public VariableDeclaration parseVariableDeclaration(AbstractBlock block) {
		Type type = Type.getType(tokenizer.getCurrentToken());
		String name = tokenizer.readName();
		if (tokenizer.peekNext().is(Operator.ASSIGN)) {
			tokenizer.next();
			Expression expression = tokenizer.readExpression(block, Symbol.SEMICOLON);
			//			block.addStatement(new VariableDeclaration(block, type, name, expression));
			return new VariableDeclaration(block, type, name, expression);
		} else {
			tokenizer.read(Symbol.SEMICOLON);
			//			block.addStatement(new VariableDeclaration(block, type, name, null));
			return new VariableDeclaration(block, type, name, null);

		}
	}

	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	public Path getPath() {
		return path;
	}

	public Module getModule() {
		return module;
	}

	public void read(AbstractToken token) {
		try {
			tokenizer.read(token);
		} catch (InvalidTokenException e) {
			error(e.getMessage());
		}
	}

	private void error(String message) {
		throw new ParseException(this, message);
	}

}
