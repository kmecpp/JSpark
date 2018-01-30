package com.kmecpp.jspark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.kmecpp.jspark.compiler.Compiler;
import com.kmecpp.jspark.compiler.parser.Parser;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;
import com.kmecpp.jspark.compiler.tokenizer.Tokenizer;
import com.kmecpp.jspark.compiler.transpiler.Transpiler;
import com.kmecpp.jspark.runtime.Interpreter;
import com.kmecpp.jspark.util.FileUtil;

public class JSpark {

	private static Compiler compiler;
	private static Interpreter runtime;
	private static Path projectPath;

	/*
	 * 
	 * TODO:
	 * Boolean expressions
	 * Conditionals
	 * Objects
	 * Multiple classes
	 * 
	 * Present Features:
	 * 
	 * - Class structure
	 * - Method syntax, invocations
	 * - Conditionals
	 * - Variables/fields
	 * - Complex expression parsing
	 * - Trivial for loops, and normal ones. While too?
	 * - Flexible syntax, putting a complex expression into a conditional
	 * - Error message contexts
	 * - List comprehensions
	 * - Transpiler: works with classes, fields, methods, print outs, list comprehensions, for loops;
	 * 
	 * @formatter:off
	 * Main.jsk
	 * 
	 * 		def main() { Console.println("Hello World!"); }
	 * 
	 * Compiler:
	 * 
	 * 		AST:
	 * 			Module: Main
	 * 				method: main:
	 * 					Console.println("Hello World");
	 * 
	 * 		Jpark Library:
	 * 			Compiles all classes to 
	 * 
	 * Interpreter:
	 * 
	 * 		
	 * 
	 * Transpiler:
	 * 
	 * 		AST -> JAVA
	 * 		JAVAC: JAVA -> JAR (bytecode)
	 * @formatter:on
	 */

	public static void main(String[] args) throws IOException, URISyntaxException {
		//		Profiler profiler = new Profiler();
		//		profiler.start();
		System.out.println("Loading source files");

		//		runProgram(IOUtil.readString(JSpark.class.getResource("/example.jsk")));
		projectPath = Paths.get(JSpark.class.getResource("/ExampleProject").toURI());

		runProject(projectPath);
		//		profiler.displayResults();

		//		displayJavaCode();
		//		System.out.println(Arrays.toString(seriesUp(5)));
	}

	//	public static int[] seriesUp(int n) {
	//		int[] result = new int[n * (n + 1) / 2];
	//		//		for (int i = 0, j = 1; i < result.length; result[i] = i % j, i = , j = (j == i ? j + 1 : j)) {
	//		//			System.out.println(j);
	//		//		}
	//		return result;
	//		//		int pos = 0;
	//		//		int i = 1;
	//		//		while (i <= n + 1) {
	//		//			for (int j = 1; j < i; j++)
	//		//				result[pos++] = j;
	//		//			i++;
	//		//		}
	//		//		return result;
	//	}

	public static void displayJavaCode() {
		for (Module module : runtime.getModules()) {
			Transpiler transpiler = new Transpiler(module);
			String javaCode = transpiler.getFormattedJava();

			System.out.println("Compiling module " + module.getFullName() + " to Java code.");
			System.out.println(javaCode);
		}
	}

	public static void runProject(Path path) throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("Compiling Project: " + path);

		//		float parseTime = 0;
		//		ArrayList<Module> modules = new ArrayList<>();
		//		for (File file : FileUtil.getFiles(path.toFile())) {
		//			long s = System.nanoTime();
		//			Module module = new Parser(file.toPath()).parseModule();
		//
		//			float time = (System.nanoTime() - s) / 1000000F;
		//			parseTime += time;
		//			System.out.println("Parsed Module: " + module.getFullName() + " (" + time + "ms)");
		//
		//			modules.add(module);
		//		}

		compiler = new Compiler();
		Files.walk(path).filter(Files::isRegularFile).forEach(compiler::parseModule);

		//		ArrayList<Module> modules = Files.walk(path)
		//				.filter(Files::isRegularFile)
		//				.map((p) -> {
		//					long s = System.nanoTime();
		//					Module module = new Parser(p).parseModule();
		//					System.out.println("Parsed Module: " + module.getFullName() + " (" + (System.nanoTime() - s) / 1000000F + "ms)");
		//					return module;
		//				})
		//				.collect(Collectors.toCollection(ArrayList::new));
		System.out.println("COMPILED SUCCESSFULLY! (" + (System.currentTimeMillis() - start + "ms)"));

		System.out.println();
		System.out.println("Running program...");
		long runStart = System.nanoTime();
		(runtime = new Interpreter(compiler)).startProgram();
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

	public static Interpreter getRuntime() {
		return runtime;
	}

	public static Compiler getCompiler() {
		return compiler;
	}

	public static Path getProjectPath() {
		return projectPath;
	}

}
