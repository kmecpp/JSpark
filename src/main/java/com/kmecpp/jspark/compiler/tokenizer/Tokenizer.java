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
	private int lineStartIndex; //This value is set to the index of every \n character

	public Tokenizer(String program) {
		this.chars = program.toCharArray();
	}

	public static Tokenizer tokenize(String str) {
		return new Tokenizer(str);
	}

	public Expression readExpression(AbstractBlock block, AbstractToken end) {
		return new Expression(block, readThrough(end));
	}

	public ArrayList<Token> readThrough(AbstractToken token) {
		ArrayList<Token> tokens = new ArrayList<>();
		int param = 0;
		while (!peekNext().is(token)) {
			Token t = next();
			if (t.is(Symbol.OPEN_PAREN)) {
				param++;
			} else if (t.is(Symbol.CLOSE_PAREN)) {
				if (--param < 0) {
					return tokens;
				}
			}

			tokens.add(t);
		}
		read(token);
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
		//		int index = current;
		Token token = next();
		if (token.getType() == type) {
			return token;
		}
		throw invalidToken(token, "type: " + type);
	}

	public Token read(AbstractToken text) {
		//		int index = current;
		Token token = next();
		if (token.getText().equals(text.getString())) {
			return token;
		}

		throw invalidToken(token, "'" + text.getString() + "'");
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
		Token peekedToken = getNextImpl();
		preprocessedTokens.push(peekedToken);
		return peekedToken;
	}

	public Token next() {
		return currentToken = getNextImpl();
	}

	public Token getCurrentToken() {
		return currentToken;
	}

	/*
	 * Implementation
	 */
	private Token getNextImpl() {
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

			int tokenStart = current;
			char c = chars[current++];

			//Comments
			if (c == '/') {
				if (chars[current] == '/') {
					while (current < chars.length && chars[current++] != '\n');
					if (chars[current - 1] == '\n') {
						newLine();
					}
					return getNextImpl();
				} else if (current < chars.length && chars[current] == '*') {
					int start = line;
					while (!(chars[current++] == '*' && chars[current] == '/')) {
						if (current == chars.length) {
							throw new RuntimeException("Multiline comment on line " + start + " was never closed!");
						}
					}
					current++;
					return getNextImpl();
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
				return new Token(tokenStart, Keyword.isKeyword(token) ? TokenType.KEYWORD : TokenType.IDENTIFIER, token);
			}

			//STRINGS
			else if (c == '"') {
				StringBuilder sb = new StringBuilder();
				while (current < chars.length && chars[current] != '"') {
					sb.append(chars[current++]);
				}
				current++;
				return new LiteralToken(tokenStart, sb.toString());
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
					return new LiteralToken(tokenStart, Double.parseDouble(sb.toString()));
				} else {
					return new LiteralToken(tokenStart, Integer.parseInt(sb.toString()));
				}
			}

			//BOOLEANS
			else if (c == 't' && chars[current] == 'r' && chars[current + 1] == 'u' && chars[current + 2] == 'e' && chars[current + 3] == ' ') {
				return new LiteralToken(tokenStart, true);
			} else if (c == 'f' && chars[current] == 'a' && chars[current + 1] == 'l' && chars[current + 2] == 's' && chars[current + 3] == 'e' && chars[current + 4] == ' ') {
				return new LiteralToken(tokenStart, false);
			}

			//SYMBOLS
			else if (Symbol.isSymbol(c)) {
				return new Token(tokenStart, TokenType.SYMBOL, String.valueOf(c));
			}

			//OPERATORS
			else {
				Operator operator = Operator.fromString(String.valueOf(c));
				if (operator != null) {
					String fullOperator = operator.getString();
					while (current < chars.length && Operator.isOperator(fullOperator + chars[current])) {
						fullOperator += chars[current++];
					}
					return new OperatorToken(tokenStart, Operator.fromString(fullOperator));
				}

				else {
					throw new InvalidTokenException("Unknown token: '" + getCharDisplay(c) + "'");
				}
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

	public int getLineStartIndex() {
		return lineStartIndex;
	}

	/**
	 * Calculates the line number of the given index
	 * 
	 * @param index
	 *            the index to find the line number of
	 * @return the line number of the line which contains the given index.
	 */
	public int getLine(int index) {
		int lineNumber = line;
		for (int i = lineStartIndex; i > 0 && i > index; i--) {
			if (chars[i] == '\n') {
				lineNumber--;
			}
		}
		return lineNumber;
	}

	public String getLineText(int index) {
		StringBuilder sb = new StringBuilder();

		int start = index;
		while (true) {
			if (chars[start] == '\n') {
				break;
			}
			start--;
		}

		int end = index;
		while (true) {
			if (chars[end] == '\n') {
				break;
			}
			end++;
		}

		for (int i = start + 1; i < end; i++) {
			sb.append(chars[i]);
		}
		return sb.toString();
	}

	public int getColumn(int index) {
		int lineStartIndex = 0;
		for (int i = index; i > 0; i--) {
			if (chars[i] == '\n') {
				lineStartIndex = i;
				break;
			}
		}
		return index - lineStartIndex;
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

	//	public String getContext(int lines, boolean showLineNumbers) {
	//		return getContext(lines, showLineNumbers, "");
	//	}

	public String getContext(Token token) {
		final int tokenIndex = token.getIndex();

		//Find the starting position
		int start = tokenIndex;
		for (int i = tokenIndex, lines = 3; i >= 0 && lines > 0; i--) {
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
		int end = tokenIndex;
		for (int i = tokenIndex; i < chars.length; i++) {
			if (chars[i] == '\n') {
				end = i;
				break;
			}
		}

		//Add file line numbers to display
		StringBuilder result = new StringBuilder();
		String[] linesArr = substring(start, end).split("\n");
		int currentLine = getLine(tokenIndex) - linesArr.length + 1;
		for (String line : linesArr) {
			result.append("\n\t" + currentLine + ": " + line);
			currentLine++;
		}
		return result.toString();
	}

	//	public String getContext(int lines, boolean showLineNumbers, String linePrefix) {
	//		//Find the starting position
	//		int start = current;
	//		for (int i = current; i >= 0 && lines > 0; i--) {
	//			if (chars[i] == '\n') {
	//				lines--;
	//				if (lines == 0) {
	//					start = i + 1; //Don't include this newline
	//					break;
	//				}
	//			}
	//			if (i == 0) {
	//				start = i;
	//				break;
	//			}
	//		}
	//
	//		//Find the ending position
	//		int end = current;
	//		for (int i = current; i < chars.length; i++) {
	//			if (chars[i] == '\n') {
	//				end = i;
	//				break;
	//			}
	//		}
	//
	//		//Calculate line numbers if necessary
	//		if (showLineNumbers) {
	//			StringBuilder result = new StringBuilder();
	//			String[] linesArr = substring(start, end).split("\n");
	//			int currentLine = this.line - linesArr.length + 1;
	//			for (String line : linesArr) {
	//				result.append(linePrefix + currentLine + ": " + line);
	//				currentLine++;
	//			}
	//			return result.toString();
	//		} else {
	//			return substring(start, end);
	//		}
	//	}
	//
	//	public String getDisplay() {
	//		String line = getCurrentLine();
	//		int start = 3 + (line.startsWith("\t") ? -4 : 0) + line.substring(0, getColumn()).replace("\t", "        ").length() - getCurrentToken().getText().length();
	//		return getContext(3, true, "\n\t") + "\n\t" + StringUtil.repeat('-', start) + "^";
	//	}

	//	public String getDisplay(int index) {
	//		StringBuilder sb = new StringBuilder();
	//		System.out.println("YYYPYP!");
	//		for (int i = index, c = 0; i > 0 && c < 2; i--) {
	//			if (chars[i] == '\n') {
	//				c++;
	//			}
	//			sb.append(chars[i]);
	//		}
	//		sb.reverse();
	//		for (int i = index; chars[i] != '\n'; i++) {
	//			sb.append(chars[i]);
	//		}
	//		sb.append(getCurrentLine());
	//		System.out.println(sb);
	//
	//		String line = getCurrentLine();
	//		int start = 3 + (line.startsWith("\t") ? -4 : 0) + line.substring(0, getColumn()).replace("\t", "        ").length() - getCurrentToken().getText().length();
	//		return getContext(3, true, "\n\t") + "\n\t" + StringUtil.repeat('-', start) + "^";
	//	}

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

	private RuntimeException invalidToken(Token token, String expected) {
		return new InvalidTokenException("Invalid token: '" + token.getText() + "' (" + token.getType() + ")! Expected " + expected + getContext(token));
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
