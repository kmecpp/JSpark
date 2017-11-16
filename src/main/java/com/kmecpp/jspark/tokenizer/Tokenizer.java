package com.kmecpp.jspark.tokenizer;

import java.util.ArrayList;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Operator;
import com.kmecpp.jspark.language.Symbol;
import com.kmecpp.jspark.language.TokenText;

public class Tokenizer {

	private char[] chars;
	private int current;

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
			tokens.add(getNext().getText());
		}
		return tokens;
	}

	public String readName() {
		return read(TokenType.IDENTIFIER).getText();
	}

	public Token read(TokenType type) {
		Token token = getNext();
		if (token.getType() == type) {
			return token;
		}
		throw invalidToken(token, type);
	}

	public Token read(TokenText text) {
		Token token = getNext();
		if (token.getText().equals(text.getString())) {
			return token;
		}
		throw invalidToken(token, text);
	}

	public Token getNext() {
		while (Character.isWhitespace(chars[current])) {
			current++;
		}

		char c = chars[current++];

		//Comments
		if (c == '/') {
			if (chars[current] == '/') {
				while (chars[current++] != '\n');
			} else if (chars[current] == '*') {
				while (!(chars[current++] == '*' && chars[current] == '/'));
			}
			return getNext();
		}

		else if (Character.isLetter(c)) {
			StringBuilder sb = new StringBuilder(String.valueOf(c));
			while (Character.isLetterOrDigit(chars[current])) {
				sb.append(chars[current]);
				current++;
			}

			String token = sb.toString();
			return new Token(token, Keyword.isKeyword(token) ? TokenType.KEYWORD : TokenType.IDENTIFIER);
		}

		//Strings
		else if (c == '"') {
			StringBuilder sb = new StringBuilder();
			while (chars[current] != '"') { //Skips closing quote because ++ is in the condition
				sb.append(chars[current++]);
			}
			current++;
			return new Token(sb.toString(), TokenType.LITERAL);
		} else if (Operator.isOperator(String.valueOf(c))) {
			return new Token(String.valueOf(c), TokenType.OPERATOR);
		} else if (Symbol.isSymbol(c)) {
			return new Token(String.valueOf(c), TokenType.SYMBOL);
		}

		else {
			System.err.println("Unknown token: '" + c + "'");
			return null;
		}

		//		return new Token(tokens.pop());
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
