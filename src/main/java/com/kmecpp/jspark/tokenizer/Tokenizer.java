package com.kmecpp.jspark.tokenizer;

import java.util.LinkedList;

public class Tokenizer {

	private final LinkedList<String> tokens = new LinkedList<>();

	private Tokenizer(String str) {

		for (String token : str.split(" ")) {
			tokens.add(token.trim());
		}
	}

	public Token getNext() {
		return new Token(tokens.pop());
	}

	public boolean hasNextToken() {
		return !tokens.isEmpty();
	}

	public static Tokenizer tokenize(String str) {
		return new Tokenizer(str);
	}

	public LinkedList<String> getTokens() {
		return tokens;
	}

}
