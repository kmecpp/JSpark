package com.kmecpp.jspark.tokenizer;

import com.kmecpp.jspark.language.Keyword;

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

	public String readName() {
		return read(TokenType.IDENTIFIER).getToken();
	}

	public Token read(TokenType type) {
		Token token = getNext();
		if (token.getType() == type) {
			return token;
		}
		throw invalidToken(token, type);
	}

	public Token getNext() {
		while (Character.isWhitespace(chars[current])) {
			current++;
		}

		char c = chars[current];

		if (Character.isLetter(c)) {
			StringBuilder sb = new StringBuilder();
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
			while (chars[++current] != '"') { //Skips closing quote because ++ is in the condition
				sb.append(chars[current]);
			}
			current++;
			return new Token(sb.toString(), TokenType.LITERAL);
		}

		//Comments
		else if (c == '/' && offset(1) == '/') {
			System.out.println("Skipping comment!");
			while (chars[++current] != '\n');
		}

		else {
			System.err.println("Unknown token: '" + c + "'");
		}

		current++;
		return null;
		//		return new Token(tokens.pop());
	}

	public char offset(int offset) {
		return chars[current + offset];
	}

	private void next() {
		current++;
	}

	public boolean hasNext() {
		return current < chars.length;
	}

	private static InvalidTokenException invalidToken(Token token, TokenType expected) {
		return new InvalidTokenException("Invalid token: '" + token.getToken() + "' (" + token.getType() + ")! Expected " + expected);
	}

	//	public ArrayList<Token> tokenize() {
	//		ArrayList<Token> tokens = new ArrayList<>();
	//		for (String token : program.split(" ")) {
	//			tokens.add(new Token(token));
	//		}
	//		return tokens;
	//	}

}
