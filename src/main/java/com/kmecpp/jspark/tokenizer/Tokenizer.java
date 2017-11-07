package com.kmecpp.jspark.tokenizer;

import java.util.LinkedList;

public class Tokenizer {

	private final LinkedList<String> tokens = new LinkedList<>();

	public Tokenizer(String program) {
		for (String token : program.split(" ")) {
			tokens.add(token.trim());
		}
	}

	public static Tokenizer tokenize(String str) {
		return new Tokenizer(str);
	}

	public Token read(TokenType type) {
		Token token = getNext();
		if (token.getType() == type) {
			return token;
		}
		throw new InvalidTokenException("Invalid token type: '" + token.getType() + "'! Expected: " + type);
	}

	public Token getNext() {
		return new Token(tokens.pop());
	}

	public boolean hasNext() {
		return !tokens.isEmpty();
	}

	//	public ArrayList<Token> tokenize() {
	//		ArrayList<Token> tokens = new ArrayList<>();
	//		for (String token : program.split(" ")) {
	//			tokens.add(new Token(token));
	//		}
	//		return tokens;
	//	}

}
