package com.kmecpp.jspark.compiler.parser;

import java.nio.file.Path;
import java.util.ArrayList;

import com.kmecpp.jspark.compiler.parser.data.Parameter;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Import;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.compiler.parser.statement.block.AnonymousBlock;
import com.kmecpp.jspark.compiler.parser.statement.block.Conditional;
import com.kmecpp.jspark.compiler.parser.statement.block.Loop;
import com.kmecpp.jspark.compiler.parser.statement.block.Method;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Class;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Static;
import com.kmecpp.jspark.compiler.parser.statement.statements.MethodInvocation;
import com.kmecpp.jspark.compiler.parser.statement.statements.ReturnStatement;
import com.kmecpp.jspark.compiler.parser.statement.statements.UnaryStatement;
import com.kmecpp.jspark.compiler.parser.statement.statements.VariableAssignment;
import com.kmecpp.jspark.compiler.parser.statement.statements.VariableDeclaration;
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
				block.addStatement(parseVariableDeclaration(block));
			}

			else if (token.isIdentifier()) {
				Token nextToken = tokenizer.peekNext();
				//Method invocations
				if (nextToken.is(Symbol.PERIOD) || nextToken.is(Symbol.OPEN_PAREN)) {
					block.addStatement(parseMethodInvocation(block));
				}

				else if (nextToken.isOperator()) {
					Operator operator = nextToken.asOperator();

					Variable variable = block.getVariable(token.getText());

					if (variable == null) {
						error(token, "Variable '" + token.getText() + "' is not defined!");
					}

					//Unary operators
					if (operator.isUnary()) {
						block.addStatement(parseUnaryStatement(block));
					}

					//Variable assignment
					else if (operator.isAssignment()) {
						tokenizer.next();
						block.addStatement(new VariableAssignment(block, variable, tokenizer.readExpression(block, Symbol.SEMICOLON)));
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
						if (tokenizer.peekNext().is(Symbol.OPEN_PAREN)) {
							tokenizer.read(Symbol.OPEN_PAREN);
							tokenizer.next();

							loop.setInitialization(parseVariableDeclaration(loop));

							//							loop.setInitialization(parseVariableDeclaration(loop));
							loop.setTermination(tokenizer.readExpression(loop, Symbol.SEMICOLON));
							//							AnonymousBlock increment = ;
							loop.setIncrement(parseStatements(new AnonymousBlock(loop)));
						} else {
							if (loop.isVariableDefined("i")) {
								error(token, "Default loop variable 'i' is already defined!");
							}

							Variable variable = new Variable(Type.INT, "i", null);
							loop.setInitialization(new VariableDeclaration(loop, variable, "0"));
							StringBuilder sb = new StringBuilder("i < ");
							for (Token t : tokenizer.readThrough(Symbol.OPEN_BRACE)) {
								sb.append(t.toString());
							}
							loop.setTermination(new Expression(loop, sb.toString()));
							loop.setIncrement(new AnonymousBlock(loop, new VariableAssignment(loop, variable, "i + 1")));
						}
					} else {
						loop.setTermination(tokenizer.readExpression(loop, Symbol.OPEN_BRACE));
					}
					parseStatements(loop);
					block.addStatement(loop);
					//					tokenizer.read(Symbol.CLOSE_BRACE);
				}

				else if (token.is(Keyword.IF)) {
					block.addStatement(parseConditional(block));
				}

				else if (token.is(Keyword.RETURN)) {
					block.addStatement(new ReturnStatement((Method) block, tokenizer.readExpression(block, Symbol.SEMICOLON)));
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
		String target;
		String method;

		if (tokenizer.peekNext().is(Symbol.PERIOD)) {
			target = tokenizer.getCurrentToken().getText();
			tokenizer.read(Symbol.PERIOD);
			method = tokenizer.readName();
		} else {
			target = "this";
			method = tokenizer.getCurrentToken().getText();
		}

		tokenizer.read(Symbol.OPEN_PAREN);
		ArrayList<Expression> params = new ArrayList<>();
		while (!tokenizer.getCurrentToken().is(Symbol.CLOSE_PAREN)) {
			params.add(tokenizer.readExpression(block, Symbol.COMMMA));
		}
		tokenizer.ignore(Symbol.SEMICOLON);
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

		if (block.isVariableDefined(name)) {
			error(tokenizer.getCurrentToken(), "Variable '" + name + "' already defined!");
		}

		//TODO: Multiple variable declarations
		//		HashMap<Variable, Expression> declarations = new HashMap<>();
		//		ArrayList<String> currentExpression = new ArrayList<>();
		//		while (true) {
		//			Token token = tokenizer.next();
		//
		//			if (token.is(Symbol.COMMMA) || token.is(Symbol.SEMICOLON)) {
		//				if (token.is(Symbol.SEMICOLON)) {
		//					break;
		//				}
		//			} else {
		//				currentExpression.add(token);
		//			}
		//		}
		//		declarations.put(new Variable(), currentExpression);
		//		tokenizer.read(Symbol.SEMICOLON);

		VariableDeclaration variableDeclaration;
		Variable variable = new Variable(type, name, null);
		if (tokenizer.peekNext().is(Operator.ASSIGN)) {
			tokenizer.read(Operator.ASSIGN);
			variableDeclaration = new VariableDeclaration(block, variable, tokenizer.readExpression(block, Symbol.SEMICOLON));
		} else {
			tokenizer.read(Symbol.SEMICOLON);
			variableDeclaration = new VariableDeclaration(block, variable, (Expression) null);
		}
		//		block.addStatement(variableDeclaration);;
		return variableDeclaration;
	}

	public Conditional parseConditional(AbstractBlock block) {
		Conditional conditional = new Conditional(block);

		//Read if statement
		tokenizer.read(Symbol.OPEN_PAREN);
		conditional.setExpression(tokenizer.readExpression(conditional, Symbol.CLOSE_PAREN));
		tokenizer.read(Symbol.OPEN_BRACE);
		parseStatements(conditional);

		//Check for elif/else
		if (tokenizer.peekNext().is(Keyword.ELSE)) {
			tokenizer.read(Keyword.ELSE);

			if (tokenizer.peekNext().is(Keyword.IF)) {
				tokenizer.read(Keyword.IF);
				conditional.setNegativeConditional(parseConditional(block));
			} else {
				tokenizer.read(Symbol.OPEN_BRACE);
				conditional.setNegativeConditional(parseStatements(new Conditional(block)));
			}
		}

		return conditional;
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
		tokenizer.ignore(Symbol.SEMICOLON);
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
