package com.kmecpp.jspark.tokenizer;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.kmecpp.jspark.language.AbstractToken;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.language.Type;

public class Tokenizer {

	private char[] chars;
	private int current;

	private Token lastToken;

	//These values are used for errors so if the user peeks the next token, the proper behavior is for them to change
	private int line = 1;
	private int lineStartIndex = 0;

	public Tokenizer(String program) {
		this.chars = program.toCharArray();
	}

	public static Tokenizer tokenize(String str) {
		return new Tokenizer(str);
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

	public Type readType() {
		return Type.getPrimitiveType(read(TokenType.KEYWORD).getText());
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
		if (lastToken == null) {
			lastToken = getNext();
		}
		return lastToken;
	}

	public Token next() {
		return getNext();
	}

	private Token getNext() {
		if (lastToken != null) {
			Token token = lastToken;
			lastToken = null;
			return token;
		}

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
					while (!(chars[current++] == '*' && chars[current] == '/'));
					current++;
					return getNext();
				}
			}

			//IDENTIFIERS
			if (Character.isLetter(c)) {
				StringBuilder sb = new StringBuilder(String.valueOf(c));

				while (Character.isLetterOrDigit(chars[current])) {
					sb.append(chars[current]);
					current++;
				}

				String token = sb.toString();
				return new Token(Keyword.isKeyword(token) ? TokenType.KEYWORD : TokenType.IDENTIFIER, token);
			}

			//STRINGS
			else if (c == '"') {
				StringBuilder sb = new StringBuilder();
				while (chars[current] != '"') {
					sb.append(chars[current++]);
				}
				current++;
				return new LiteralToken(sb.toString());
			}

			//NUMBERS
			else if ((c == '-' && Character.isDigit(c)) || Character.isDigit(c)) {
				StringBuilder sb = new StringBuilder(String.valueOf(c));
				while (Character.isDigit(chars[current])) {
					sb.append(chars[current++]);
				}

				if (chars[current] == '.') {
					sb.append(".");
					current++;
					while (Character.isDigit(chars[current])) {
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
				return new Token(TokenType.OPERATOR, String.valueOf(c));
			}

			//SYMBOLS
			else if (Symbol.isSymbol(c)) {
				return new Token(TokenType.SYMBOL, String.valueOf(c));
			}

			else {
				throw new InvalidTokenException("Unknown token: '" + c + "'");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("Unexpected end of file: '" + chars[chars.length - 1] + "'", e);
		}
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

	//	public String getPreviousLine() {
	//		int start;
	//		for (start = lineStartIndex - 1; start > 0 && chars[--start] != '\n';);
	//		return substring(start + 1, lineStartIndex - 1);
	//	}

	public String getCurrentLine() {
		return getContext(0);
		//		System.out.println("TEXT: " + getLineText(0));
		//
		//		StringBuilder sb = new StringBuilder();
		//		//		for (int i = lineStartIndex; i < chars.length && chars[i] != '\n'; i++) {
		//		//			sb.append(chars[i]);
		//		//		}
		//		return sb.toString();
	}

	//	public String getLineText(int lineOffset) {
	//		if (lineOffset < 0) {
	//			int start;
	//			for (start = lineStartIndex - 1; start > 0 && lineOffset < 0; start--) {
	//				if (chars[start] != '\n') {
	//
	//				}
	//			}
	//			return substring(start + 1, lineStartIndex - 1);
	//		}
	//
	//		else if (lineOffset == 0) {
	//			StringBuilder sb = new StringBuilder();
	//			for (int i = lineStartIndex + 1; i < chars.length && chars[i] != '\n'; i++) {
	//				sb.append(chars[i]);
	//			}
	//			return sb.toString();
	//		}
	//
	//		else {
	//			for (int start = lineStartIndex, end = lineStartIndex; end < chars.length && lineOffset >= 0; end++) {
	//				if (chars[end] == '\n') {
	//					lineOffset--;
	//					if (lineOffset == 1) {
	//						start = end;
	//					}
	//					System.out.println(lineOffset);
	//					if (lineOffset == 0) {
	//						System.out.println("Yo");
	//						return substring(start, end);
	//					}
	//				}
	//			}
	//			return null;
	//		}
	//
	//		//		int i = 0;
	//		//		if (lineOffset < 0) {
	//		//			
	//		//		}else {
	//		//			while(true) {
	//		//				if(chars[i++] == '\n') {
	//		//					lineOffset--;
	//		//				}
	//		//				if(lineOffset > 0) {
	//		//					return substring(lineStartIndex, 
	//		//				}
	//		//			}
	//		//		}
	//	}

	public static void main(String[] args) {
		Tokenizer t = new Tokenizer(
				"Line 1 \nLine 2 \nLine 3 \nLine 4 \nLine 5");
		System.out.println(t.next());
		System.out.println(t.next());
		System.out.println(t.next());
		System.out.println(t.next());
		System.out.println(t.next());
		System.out.println(t.next());
		System.out.println(t.next());
		System.out.println(t.next());
		System.out.println("Get Context: ");
		System.out.println(t.getContext(3));
	}

	public String getContext(int lines) {
		int start = current;
		for (int i = current; i > 0;) {
			if (--i <= 0 || (chars[i] == '\n' && lines-- <= 1)) {
				start = i;
			}
		}

		int end = current;
		for (int i = current; i < chars.length; i++) {
			if (chars[i] == '\n') {
				end = i;
				break;
			}
		}
		System.out.println(start + ", " + end);
		return substring(start, end);
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

	//	public ArrayList<Token> tokenize() {
	//		ArrayList<Token> tokens = new ArrayList<>();
	//		for (String token : program.split(" ")) {
	//			tokens.add(new Token(token));
	//		}
	//		return tokens;
	//	}

}
