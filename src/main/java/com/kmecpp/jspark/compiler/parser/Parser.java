package com.kmecpp.jspark.compiler.parser;

import java.nio.file.Path;
import java.util.ArrayList;

import com.kmecpp.jspark.compiler.parser.data.Parameter;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.statement.Import;
import com.kmecpp.jspark.compiler.parser.statement.MethodInvocation;
import com.kmecpp.jspark.compiler.parser.statement.UnaryStatement;
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
import com.kmecpp.jspark.compiler.tokenizer.TokenType;
import com.kmecpp.jspark.compiler.tokenizer.Tokenizer;
import com.kmecpp.jspark.language.AbstractToken;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.util.FileUtil;
import com.kmecpp.jspark.util.StringUtil;

public class Parser {

	private Path path;

	private Tokenizer tokenizer;
	private Module module;

	public Parser(Path path) {
		this.path = path;
	}

	/*
	 * PARSE TOP LEVEL MODULE CODE
	 */
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
					parseVariableDeclaration(module); //Parse Fields
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
					error("Unexpected keyword in class definition: " + token.getText());
				}
			}

			else if (token.is(Symbol.CLOSE_BRACE)) {
				if (tokenizer.hasNext()) {
					error(token, "Encountered unexpected closing brace!");
				}
			}

			else {
				error("Unexpected " + token.getType().toString().toLowerCase() + " in class definition '" + token.getText() + "'");
				//				error("Could not parse unknown " + (token == null ? "token: null" : token.getType() + " '" + token.getText() + "'"));
			}
		}
		//		System.out.println(module.getFields());
		return module;
	}

	/*
	 * 
	 * GENERIC BLOCK PARSING
	 * 
	 */
	private <T extends AbstractBlock> T parseStatements(T block) {
		while (true) {
			Token token = tokenizer.next();

			if (token.isPrimitiveType()) {
				parseVariableDeclaration(block);
			}

			else if (token.isIdentifier()) {
				Token nextToken = tokenizer.peekNext();
				//Method invocations
				if (nextToken.is(Symbol.PERIOD)) {
					block.addStatement(parseMethodInvocation(block));
				}

				else if (nextToken.isOperator()) {
					Operator operator = nextToken.asOperator();

					if (!block.containsVariable(token.getText())) {
						error(token, "Variable '" + token.getText() + "' is not defined!");
					}

					//Unary operators
					if (operator.isUnary()) {
						block.addStatement(parseUnaryStatement(block));
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

				//				else if (tokenizer.peekNext().isIdentifier()) {
				//					parseVariableDeclaration(block);
				//				}

				else {
					error(token, "Unknown identifier while parsing block: '" + token + "'");
				}
			}

			else if (token.isKeyword()) {
				//LOOPS
				if (token.is(Keyword.FOR) || token.is(Keyword.WHILE)) {
					Loop loop = new Loop(block);

					if (token.is(Keyword.FOR)) {
						if (tokenizer.peekNext().isInt()) {
							if (loop.containsVariable("i")) {
								error(token, "Default loop variable 'i' is already defined!");
							}

							loop.setInitialization(new AnonymousBlock(loop, new VariableDeclaration(loop, Type.INT, "i", "0")));
							loop.setTermination(new Expression(loop, "i < " + tokenizer.next().asInt()));
							loop.setIncrement(new AnonymousBlock(loop, new VariableAssignment(loop, "i", "i + 1")));
							tokenizer.read(Symbol.OPEN_BRACE);
						} else {
							tokenizer.read(Symbol.OPEN_PAREN);
							tokenizer.next();

							loop.setInitialization(parseVariableDeclaration(new AnonymousBlock(loop)));

							//							loop.setInitialization(parseVariableDeclaration(loop));
							loop.setTermination(tokenizer.readExpression(loop, Symbol.SEMICOLON));
							//							AnonymousBlock increment = ;
							loop.setIncrement(parseStatements(new AnonymousBlock(loop)));
						}
					} else {
						loop.setTermination(tokenizer.readExpression(loop, Symbol.OPEN_BRACE));
					}
					parseStatements(loop);
					block.addStatement(loop);
					//					tokenizer.read(Symbol.CLOSE_BRACE);
				}

				else if (token.is(Keyword.IF)) {
					//TODO
				}

				else {
					error("Unexpected keyword: '" + token.getText() + "'");
				}
			}

			else if (token.isOperator()) {
				block.addStatement(parseUnaryStatement(block));
			}

			else {
				return block;
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
	public <T extends AbstractBlock> T parseVariableDeclaration(T block) {
		Type type = Type.getType(tokenizer.getCurrentToken());
		String name = tokenizer.readName();

		if (block.containsVariable(name)) {
			error(tokenizer.getCurrentToken(), "Variable '" + name + "' already defined!");
		}

		if (tokenizer.peekNext().is(Operator.ASSIGN)) {
			tokenizer.next();
			Expression expression = tokenizer.readExpression(block, Symbol.SEMICOLON);
			//			block.addStatement(new VariableDeclaration(block, type, name, expression));

			block.addStatement(new VariableDeclaration(block, type, name, expression));
		} else {
			tokenizer.read(Symbol.SEMICOLON);
			//			block.addStatement(new VariableDeclaration(block, type, name, null));
			block.addStatement(new VariableDeclaration(block, type, name, (Expression) null));
		}
		return block;
	}

	public UnaryStatement parseUnaryStatement(AbstractBlock block) {
		Token token = tokenizer.getCurrentToken();

		UnaryStatement statement;
		if (token.isIdentifier()) {
			statement = new UnaryStatement(block, tokenizer.getCurrentToken().getText(), tokenizer.read(TokenType.OPERATOR).asOperator());
		} else if (token.isOperator() && token.asOperator().isUnary()) {
			statement = new UnaryStatement(block, tokenizer.read(TokenType.IDENTIFIER).getText(), token.asOperator());
		} else {
			error(token, "Expected unary statement!");
			return null;
		}
		tokenizer.read(Symbol.SEMICOLON);
		return statement;
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
			error(tokenizer.getCurrentToken(), e.getMessage());
		}
	}

	private void error(String message) {
		error(tokenizer.getCurrentToken(), message);
	}

	private void error(Token token, String message) {
		System.err.println("Parsing exception" + " at (" + (module == null ? path.getFileName() : module.getFileName()) + ":" + tokenizer.getLine() + "): " + message
				+ tokenizer.getContext(token)
				//				+ tokenizer.getContext(3, true, "\n\t")
				+ "\n\t" + StringUtil.repeat('-', getErrorStart(token)) + "^"
				+ "\n\t");

		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stack.length; i++) {
			StackTraceElement element = stack[i];
			//			System.out.println(element);
			if (!element.getMethodName().equals("error")) {
				System.err.println(element);
				System.exit(1);
			}
		}
		throw new RuntimeException();
	}

	private int getErrorStart(Token token) {
		String line = token.getLineText(tokenizer);
		System.out.println(line.length());
		return 3
				+ (line.startsWith("\t") ? -4 : 0)
				+ line.substring(0, token.getColumn(tokenizer) + (token.getText().length() / 2)).replace("\t", "        ").length();
		//				+ line.substring(0, tokenizer.getColumn()).replace("\t", "        ").length()
		//				- tokenizer.getCurrentToken().getText().length();
	}

}
