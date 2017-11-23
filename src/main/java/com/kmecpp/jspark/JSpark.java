package com.kmecpp.jspark;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.kmecpp.jspark.parser.Parser;
import com.kmecpp.jspark.parser.statement.Statement;
import com.kmecpp.jspark.parser.statement.block.module.Module;
import com.kmecpp.jspark.runtime.Runtime;
import com.kmecpp.jspark.tokenizer.Tokenizer;
import com.kmecpp.jspark.util.FileUtil;

public class JSpark {

	private static Runtime runtime;

	public static void main(String[] args) throws IOException {
		// Tokenizer tokenizer = Tokenizer.tokenize("HelloWorld = \"shit\" 304 and
		// 3.40");
		// for (String line : IOUtil.readLines(new
		// File(JSpark.class.getResource("/example.jsk").getFile()))) {
		// System.out.println(line);
		// }
		System.out.println("Loading source files");
		//		runProgram(IOUtil.readString(JSpark.class.getResource("/example.jsk")));

		runProject(JSpark.class.getResource("/ExampleProject").getPath());

		// while (tokenizer.hasNextToken()) {
		// System.out.println(tokenizer.getNext());
		// }
	}

	public static Runtime getRuntime() {
		return runtime;
	}

	public static void runProject(String path) throws IOException {
		ArrayList<Module> modules = Files.walk(new File(path).toPath())
				.filter(Files::isRegularFile)
				.map((p) -> new Parser(new Tokenizer(FileUtil.readFile(p))).parseModule())
				.collect(Collectors.toCollection(ArrayList::new));

		(runtime = new Runtime(modules)).start();

		//			System.out.println(file.toAbsolutePath());
		//			Module module = new Parser(new Tokenizer(FileUtil.readFile(file))).parse();
		//			if (module instanceof Static && module.hasMethod("main")) {
		//				if (main == null) {
		//					main = module;
		//				} else {
		//					throw new IllegalStateException("Main method already defined in '" + main.getName() + "'");
		//				}
		//			}
		//			System.out.println(module);
		//		});
	}

	public static void runProgram(String program) {
		Tokenizer tokenizer = new Tokenizer(program);

		System.out.println("Token List: " + tokenizer.getTokenList());

		Module module = new Parser(new Tokenizer(program)).parseModule();
		System.out.println("Parsed program!");
		System.out.println();
		System.out.println(module.getName());
		for (Statement statement : module.getStatements()) {
			System.out.println("    " + statement);
		}

		System.out.println("Executing...");
		module.execute();
	}

}
