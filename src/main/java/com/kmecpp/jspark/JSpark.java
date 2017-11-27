package com.kmecpp.jspark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	public static void main(String[] args) throws IOException, URISyntaxException {
		// Tokenizer tokenizer = Tokenizer.tokenize("HelloWorld = \"shit\" 304 and
		// 3.40");
		// for (String line : IOUtil.readLines(new
		// File(JSpark.class.getResource("/example.jsk").getFile()))) {
		// System.out.println(line);
		// }
		System.out.println("Loading source files");
		//		runProgram(IOUtil.readString(JSpark.class.getResource("/example.jsk")));

		runProject(Paths.get(JSpark.class.getResource("/ExampleProject").toURI()));

	}

	public static Runtime getRuntime() {
		return runtime;
	}

	public static void runProject(Path path) throws IOException {
		long start = System.currentTimeMillis();
		ArrayList<Module> modules = Files.walk(path)
				.filter(Files::isRegularFile)
				.map((p) -> {
					long s = System.nanoTime();
					Module module = new Parser(p, new Tokenizer(FileUtil.readFile(p))).parseModule();
					System.out.println("Parsed File: " + module.getName() + " (" + (System.nanoTime() - s) / 1000000F + "ms)");
					return module;
				})
				.collect(Collectors.toCollection(ArrayList::new));
		System.out.println("Compile Time: " + (System.currentTimeMillis() - start) + "ms");

		System.out.println();
		System.out.println("Running program...");
		(runtime = new Runtime(modules)).start();
	}

	public static void runProgram(Path path) {
		String program = FileUtil.readFile(path);
		Tokenizer tokenizer = new Tokenizer(program);

		System.out.println("Token List: " + tokenizer.getTokenList());

		Module module = new Parser(path, new Tokenizer(program)).parseModule();
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
