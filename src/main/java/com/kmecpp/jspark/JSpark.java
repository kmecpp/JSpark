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
	private static Path projectPath;

	public static void main(String[] args) throws IOException, URISyntaxException {
		// Tokenizer tokenizer = Tokenizer.tokenize("HelloWorld = \"shit\" 304 and
		// 3.40");
		// for (String line : IOUtil.readLines(new
		// File(JSpark.class.getResource("/example.jsk").getFile()))) {
		// System.out.println(line);
		// }
		System.out.println("Loading source files");
		//		runProgram(IOUtil.readString(JSpark.class.getResource("/example.jsk")));
		projectPath = Paths.get(JSpark.class.getResource("/ExampleProject").toURI());

		runProject(projectPath);
	}

	public static Runtime getRuntime() {
		return runtime;
	}

	public static Path getProjectPath() {
		return projectPath;
	}

	public static void runProject(Path path) throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("Compiling Project: " + path);
		//		ArrayList<Module> modules = new ArrayList<>();
		//		for (File file : FileUtil.getFiles(path.toFile())) {
		//			long s = System.nanoTime();
		//			Module module = new Parser(file.toPath()).parseModule();
		//			System.out.println("Package: " + module.getPackage());
		//			System.out.println("Parsed File: " + path.relativize(file.toPath()) + " (" + (System.nanoTime() - s) / 1000000F + "ms)");
		//			modules.add(module);
		//	}

		ArrayList<Module> modules = Files.walk(path)
				.filter(Files::isRegularFile)
				.map((p) -> {
					long s = System.nanoTime();
					Module module = new Parser(p).parseModule();
					System.out.println("Parsed Module: " + module.getFullName() + " (" + (System.nanoTime() - s) / 1000000F + "ms)");
					return module;
				})
				.collect(Collectors.toCollection(ArrayList::new));
		System.out.println("COMPILED SUCCESSFULLY! (" + (System.currentTimeMillis() - start) + "ms)");

		System.out.println();
		System.out.println("Running program...");
		long runStart = System.nanoTime();
		(runtime = new Runtime(modules)).start();
		System.out.println("_____________________________________");
		System.out.println("Runtime: " + (System.nanoTime() - runStart) / 1000000F + "ms");
	}

	public static void runProgram(Path path) {
		String program = FileUtil.readFile(path);
		Tokenizer tokenizer = new Tokenizer(program);

		System.out.println("Token List: " + tokenizer.getTokenList());

		Module module = new Parser(path).parseModule();
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
