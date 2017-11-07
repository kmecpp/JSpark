package com.kmecpp.jspark;

import java.io.IOException;
import java.util.ArrayList;

import com.kmecpp.jlib.utils.IOUtil;
import com.kmecpp.jspark.parser.Parser;
import com.kmecpp.jspark.parser.Statement;
import com.kmecpp.jspark.tokenizer.Tokenizer;

public class JSpark {

	public static void main(String[] args) throws IOException {
		//		Tokenizer tokenizer = Tokenizer.tokenize("HelloWorld = \"shit\" 304 and 3.40");
		//		for (String line : IOUtil.readLines(new File(JSpark.class.getResource("/example.jsk").getFile()))) {
		//			System.out.println(line);
		//		}
		System.out.println("Loading source files");
		runProgram(IOUtil.readString(JSpark.class.getResource("/example.jsk")));

		//		while (tokenizer.hasNextToken()) {
		//			System.out.println(tokenizer.getNext());
		//		}
	}

	public static void runProgram(String program) {
		//		System.out.println("Lexing");
		//		ArrayList<Token> tokens = new Tokenizer(program).tokenize();

		ArrayList<Statement> statements = new Parser(new Tokenizer(program)).parse();

		for (Statement statement : statements) {
			statement.execute();
		}
	}

}
