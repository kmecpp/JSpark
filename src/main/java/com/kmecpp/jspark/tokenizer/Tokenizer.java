package com.kmecpp.jspark.tokenizer;

import java.util.ArrayList;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.language.TokenText;
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

	public ArrayList<String> getTokenList() {
		ArrayList<String> tokens = new ArrayList<>();
		while (hasNext()) {
			tokens.add(next().getText());
		}
		return tokens;
	}

	public Type readType() {
		return Type.fromString(read(TokenType.KEYWORD).getText());
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

	public Token read(TokenText text) {
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
		int start = current;
		Token token = getNext();
		current = start;
		return token;
	}

	private Token getNext() {
		if (lastToken != null) {
			Token token = lastToken;
			lastToken = null;
			return token;
		}

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
			while (chars[current] != '"') { //Skips closing quote because ++ is in the condition
				sb.append(chars[current++]);
			}
			current++;
			return new Token(sb.toString(), TokenType.LITERAL);
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
	}

	public char offset(int offset) {
		return chars[current + offset];
	}

	//	private void next() {
	//		current++;
	//	}

	public boolean hasNext() {
		return current < chars.length;
	}

	private static InvalidTokenException invalidToken(Token token, TokenType expected) {
		return new InvalidTokenException("Invalid token: '" + token.getText() + "' (" + token.getType() + ")! Expected " + expected);
	}

	private static InvalidTokenException invalidToken(Token token, TokenText expected) {
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
