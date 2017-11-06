package com.kmecpp.jspark;

import com.kmecpp.jspark.tokenizer.Tokenizer;

public class JSpark {

	public static void main(String[] args) {
		Tokenizer tokenizer = Tokenizer.tokenize("HelloWorld = \"shit\" 304 and 3.40");

		while (tokenizer.hasNextToken()) {
			System.out.println(tokenizer.getNext());
		}

		//		for (String s : tokenizer.getTokens()) {
		//			System.out.println(s);
		//		}

		//		long start = System.currentTimeMillis();
		//		String test = "HelloBitches!";
		//		for (int i = 0; i < 10000; i++) {
		//			for (int j = 0; j < test.length(); j++) {
		//				char c = test.charAt(j);
		//				if (Character.isLetterOrDigit(c)) {
		//					continue;
		//				}
		//				//System.out.println("FUCK DICKS!");
		//			}
		//		}
		//		System.out.println("Time Taken: " + (System.currentTimeMillis() - start) + "ms");
	}

}
