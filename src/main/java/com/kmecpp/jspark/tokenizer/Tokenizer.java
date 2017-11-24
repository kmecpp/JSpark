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

	public Tokenizer(String program) {
		this.chars = program.toCharArray();

		//		for (String token : program.split(" ")) {
		//			tokens.add(token.trim());
		//		}
	}

	public static Tokenizer tokenize(String str) {
		return new Tokenizer(str);
	}

	public ArrayList<Token> readAll() {
		ArrayList<Token> tokens = new ArrayList<>();
		while (this.hasNext()) {
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

	public Token next() {
		return getNext();
	}

	public Token peekNext() {
		return lastToken = getNext();
	}

	public boolean hasNext() {
		try {
			peekNext();
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	private Token getNext() {
		if (lastToken != null) {
			Token token = lastToken;
			lastToken = null;
			return token;
		}

		try {
			while (Character.isWhitespace(chars[current])) {
				current++;
			}

			char c = chars[current++];

			//Comments
			if (c == '/') {
				if (chars[current] == '/') {
					while (chars[current++] != '\n');
					return getNext();
				} else if (chars[current] == '*') {
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
				return new Token(token, Keyword.isKeyword(token) ? TokenType.KEYWORD : TokenType.IDENTIFIER);
			}

			//STRINGS
			else if (c == '"') {
				StringBuilder sb = new StringBuilder();
				while (chars[current] != '"') {
					sb.append(chars[current++]);
				}
				current++;
				return new Token("\"" + sb + "\"", TokenType.STRING_LITERAL);
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
					return new Token(sb.toString(), TokenType.DECIMAL_LITERAL);
				} else {
					return new Token(sb.toString(), TokenType.INTEGER_LITERAL);
				}
			}

			//BOOLEANS
			else if (c == 't' && chars[current] == 'r' && chars[current + 1] == 'u' && chars[current + 2] == 'e' && chars[current + 3] == ' ') {
				return new Token(String.valueOf(true), TokenType.BOOLEAN_LITERAL);
			} else if (c == 'f' && chars[current] == 'a' && chars[current + 1] == 'l' && chars[current + 2] == 's' && chars[current + 3] == 'e' && chars[current + 4] == ' ') {
				return new Token(String.valueOf(false), TokenType.BOOLEAN_LITERAL);
			}

			//OPERATORS
			else if (Operator.isOperator(String.valueOf(c))) {
				return new Token(String.valueOf(c), TokenType.OPERATOR);
			}

			//SYMBOLS
			else if (Symbol.isSymbol(c)) {
				return new Token(String.valueOf(c), TokenType.SYMBOL);
			}

			else {
				throw new InvalidTokenException("Unknown token: '" + c + "'");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("Unexpected end of file: '" + chars[chars.length - 1] + "'", e);
		}
	}

	public char offset(int offset) {
		return chars[current + offset];
	}

	//	private void next() {
	//		current++;
	//	}

	//	private boolean hasNextChar() {
	//		return current < chars.length;
	//	}

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
