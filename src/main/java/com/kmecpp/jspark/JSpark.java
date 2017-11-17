package com.kmecpp.jspark;

import java.io.IOException;

import com.kmecpp.jlib.utils.IOUtil;
import com.kmecpp.jspark.parser.Parser;
import com.kmecpp.jspark.parser.Statement;
import com.kmecpp.jspark.parser.statements.modules.Module;
import com.kmecpp.jspark.tokenizer.Tokenizer;

public class JSpark {

	public static void main(String[] args) throws IOException {
		// Tokenizer tokenizer = Tokenizer.tokenize("HelloWorld = \"shit\" 304 and
		// 3.40");
		// for (String line : IOUtil.readLines(new
		// File(JSpark.class.getResource("/example.jsk").getFile()))) {
		// System.out.println(line);
		// }
		System.out.println("Loading source files");
		runProgram(IOUtil.readString(JSpark.class.getResource("/example.jsk")));

		// while (tokenizer.hasNextToken()) {
		// System.out.println(tokenizer.getNext());
		// }
	}

	public static void runProgram(String program) {
		Tokenizer tokenizer = new Tokenizer(program);

		System.out.println("Token List: " + tokenizer.getTokenList());

		Module module = new Parser(new Tokenizer(program)).parse();
		System.out.println("Parsed program!");
		System.out.println(module.getStatements().size());
		System.out.println(module.getName());
		for (Statement statement : module.getStatements()) {
			System.out.println(statement);
		}

		System.out.println("Executing...");
		module.execute();
	}

}
