package com.kmecpp.jspark.compiler.tokenizer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.language.AbstractToken;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.PrimitiveType;
import com.kmecpp.jspark.language.Symbol;

public class Tokenizer {

	private char[] chars;
	private int current;

	//	private Token peekedToken;
	private Deque<Token> preprocessedTokens = new LinkedList<>();
	private Token currentToken;

	//These values are used for errors so if the user peeks the next token, the proper behavior is for them to change
	private int line = 1;
	private int lineStartIndex = 0; //This value is set to the index of every \n character

	public Tokenizer(String program) {
		this.chars = program.toCharArray();
	}

	public static Tokenizer tokenize(String str) {
		return new Tokenizer(str);
	}

	public Expression readExpression(AbstractBlock block) {
		return new Expression(block, readThrough(Symbol.SEMICOLON));
	}

	public ArrayList<Token> readThrough(AbstractToken token) {
		ArrayList<Token> tokens = new ArrayList<>();
		while (!peekNext().is(token)) {
			tokens.add(next());
		}
		System.out.println("LAST: " + read(token));
		return tokens;
	}

	public ArrayList<Token> readAll() {
		ArrayList<Token> tokens = new ArrayList<>();
		while (hasNext()) {
			tokens.add(next());
		}
		return tokens;
	}

	public ArrayList<String> getTokenList() {
		return new Tokenizer(new String(chars)).readAll().stream().map(Token::getText).collect(Collectors.toCollection(ArrayList::new));
	}

	public PrimitiveType readType() {
		return PrimitiveType.getPrimitiveType(read(TokenType.KEYWORD).getText());
	}

	public String readName() {
		return read(TokenType.IDENTIFIER).getText();
	}

	public Token read(TokenType type) {
		Token token = next();
		if (token.getType() == type) {
			return token;
		}
		throw invalidToken(token, type);
	}

	public Token read(AbstractToken text) {
		Token token = next();
		if (token.getText().equals(text.getString())) {
			return token;
		}
		throw invalidToken(token, text);
	}

	public boolean hasNext() {
		return peekNext() != null;
		//		while (current < chars.length && Character.isWhitespace(chars[current])) {
		//			current++;
		//		}
		//		return current < chars.length;
	}

	public Token peekNext() {
		//		if (peekedToken == null) {
		//			peekedToken = getNext();
		//		}
		//		return peekedToken;
		Token peekedToken = getNext();
		preprocessedTokens.push(peekedToken);
		return peekedToken;
	}

	public Token next() {
		return currentToken = getNext();
	}

	public Token getCurrentToken() {
		return currentToken;
	}

	/*
	 * Implementation
	 */
	private Token getNext() {
		if (!preprocessedTokens.isEmpty()) {
			return preprocessedTokens.pop();
		}
		//		if (peekedToken != null) {
		//			Token token = peekedToken;
		//			peekedToken = null;
		//			return token;
		//		}

		try {
			while (current < chars.length && Character.isWhitespace(chars[current])) {
				if (chars[current] == '\n') {
					newLine();
				}
				current++;
			}

			if (current >= chars.length) {
				return null;
			}

			char c = chars[current++];

			//Comments
			if (c == '/') {
				if (chars[current] == '/') {
					while (current < chars.length && chars[current++] != '\n');
					if (chars[current - 1] == '\n') {
						newLine();
					}
					return getNext();
				} else if (current < chars.length && chars[current] == '*') {
					int start = line;
					while (!(chars[current++] == '*' && chars[current] == '/')) {
						if (current == chars.length) {
							throw new RuntimeException("Multiline comment on line " + start + " was never closed!");
						}
					}
					current++;
					return getNext();
				}
			}

			//IDENTIFIERS
			if (Character.isLetter(c)) {
				StringBuilder sb = new StringBuilder(String.valueOf(c));

				while (current < chars.length && Character.isLetterOrDigit(chars[current])) {
					sb.append(chars[current]);
					current++;
				}

				String token = sb.toString();
				return new Token(Keyword.isKeyword(token) ? TokenType.KEYWORD : TokenType.IDENTIFIER, token);
			}

			//STRINGS
			else if (c == '"') {
				StringBuilder sb = new StringBuilder();
				while (current < chars.length && chars[current] != '"') {
					sb.append(chars[current++]);
				}
				current++;
				return new LiteralToken(sb.toString());
			}

			//NUMBERS
			else if ((c == '-' && Character.isDigit(c)) || Character.isDigit(c)) {
				StringBuilder sb = new StringBuilder(String.valueOf(c));
				while (current < chars.length && Character.isDigit(chars[current])) {
					sb.append(chars[current++]);
				}

				if (current < chars.length && chars[current] == '.') {
					sb.append(".");
					current++;
					while (current < chars.length && Character.isDigit(chars[current])) {
						sb.append(chars[current++]);
					}
					//TODO: E notation?					
					return new LiteralToken(Double.parseDouble(sb.toString()));
				} else {
					return new LiteralToken(Integer.parseInt(sb.toString()));
				}
			}

			//BOOLEANS
			else if (c == 't' && chars[current] == 'r' && chars[current + 1] == 'u' && chars[current + 2] == 'e' && chars[current + 3] == ' ') {
				return new LiteralToken(true);
			} else if (c == 'f' && chars[current] == 'a' && chars[current + 1] == 'l' && chars[current + 2] == 's' && chars[current + 3] == 'e' && chars[current + 4] == ' ') {
				return new LiteralToken(false);
			}

			//OPERATORS
			else if (Operator.isOperator(String.valueOf(c))) {
				String unary = c + peekNext().getText();
				if (Operator.isOperator(unary)) {
					next();
					return new Token(TokenType.OPERATOR, unary);
				} else {
					return new Token(TokenType.OPERATOR, String.valueOf(c));
				}
			}

			//SYMBOLS
			else if (Symbol.isSymbol(c)) {
				return new Token(TokenType.SYMBOL, String.valueOf(c));
			}

			else {
				throw new InvalidTokenException("Unknown token: '" + getCharDisplay(c) + "'");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("Unexpected end of file: '" + getCharDisplay(chars[chars.length - 1]) + "'", e);
		}
	}

	public String getCharDisplay(char c) {
		if (c == '\n') {
			return "\\n";
		} else if (c == '\t') {
			return "\\t";
		}
		return String.valueOf(c);
	}

	//	public char offset(int offset) {
	//		return chars[current + offset];
	//	}

	//	private void next() {
	//		current++;
	//	}

	//	private boolean hasNextChar() {
	//		return current < chars.length;
	//	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return current - lineStartIndex;
	}

	public String getCurrentLine() {
		StringBuilder sb = new StringBuilder();
		for (int i = lineStartIndex + 1; i < chars.length && chars[i] != '\n'; i++) {
			sb.append(chars[i]);
		}
		return sb.toString();
	}

	public String getContext(int lines, boolean showLineNumbers) {
		return getContext(lines, showLineNumbers, "");
	}

	public String getContext(int lines, boolean showLineNumbers, String linePrefix) {
		//Find the starting position
		int start = current;
		for (int i = current; i >= 0 && lines > 0; i--) {
			if (chars[i] == '\n') {
				lines--;
				if (lines == 0) {
					start = i + 1; //Don't include this newline
					break;
				}
			}
			if (i == 0) {
				start = i;
				break;
			}
		}

		//Find the ending position
		int end = current;
		for (int i = current; i < chars.length; i++) {
			if (chars[i] == '\n') {
				end = i;
				break;
			}
		}

		//Calculate line numbers if necessary
		if (showLineNumbers) {
			StringBuilder result = new StringBuilder();
			String[] linesArr = substring(start, end).split("\n");
			int currentLine = this.line - linesArr.length + 1;
			for (String line : linesArr) {
				result.append(linePrefix + currentLine + ": " + line);
				currentLine++;
			}
			return result.toString();
		} else {
			return substring(start, end);
		}
	}

	private String substring(int start, int end) {
		int length = end - start;
		char[] chars = new char[length];
		System.arraycopy(this.chars, start, chars, 0, length);
		return new String(chars);
	}

	private void newLine() {
		line++;
		lineStartIndex = current;
	}

	private static InvalidTokenException invalidToken(Token token, TokenType expected) {
		return new InvalidTokenException("Invalid token: '" + token.getText() + "' (" + token.getType() + ")! Expected " + expected);
	}

	private static InvalidTokenException invalidToken(Token token, AbstractToken expected) {
		return new InvalidTokenException("Invalid token: '" + token.getText() + "' (" + token.getType() + ")! Expected " + expected);
	}

	public static ArrayList<Token> parseTokens(String expression) {
		return new Tokenizer(expression).readAll();
	}

	//	public ArrayList<Token> tokenize() {
	//		ArrayList<Token> tokens = new ArrayList<>();
	//		for (String token : program.split(" ")) {
	//			tokens.add(new Token(token));
	//		}
	//		return tokens;
	//	}

}