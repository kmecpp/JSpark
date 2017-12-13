package com.kmecpp.jspark.util.java;

import java.util.Arrays;

import javax.tools.ToolProvider;

public class JavaCompiler {

	public static final javax.tools.JavaCompiler JAVAC = ToolProvider.getSystemJavaCompiler();

	//	private static int methodCounter;
	//
	//	/**
	//	 * TODO: Should this method take in a parent class parameter so it can trace
	//	 * the method call?
	//	 * 
	//	 * @param methodBody
	//	 *            the Java method body to compile
	//	 * @return A runnable that can be executed to run the given Java code
	//	 * @throws Exception
	//	 *             if an exception is thrown
	//	 */
	//	public static Runnable compileMethod(String methodBody) throws Exception {
	//		
	//		//		String className = "JavaCode" + methodCounter++;
	//		//		String classSource = "public class " + className + "{"
	//		//				+ "public static void execute(){"
	//		//				+ methodBody
	//		//				+ "}"
	//		//				+ "}";
	//		//		Class<?> cls = compileClass(className, classSource);
	//		//		cls.getMethod("execute", parameterTypes)
	//		//		return new Runnable() {
	//		//
	//		//			@Override
	//		//			public void run() {
	//		//			}
	//		//		};
	//	}

	/**
	 * Compiles the class but detects the class name instead of having to have
	 * it provided
	 * 
	 * @param source
	 *            the class source code
	 * @return the compiled class
	 * @throws Exception
	 *             if an exception is thrown
	 */
	public static Class<?> compileClass(String source) throws Exception {
		int start = source.indexOf("class");
		source = source.substring(start);
		StringBuilder name = new StringBuilder();

		for (int i = 0;; i++) {
			char c = source.charAt(i);
			if (name.length() > 0 && Character.isWhitespace(c)) {
				return compileClass(name.toString(), source);
			}
		}
	}

	public static Class<?> compileClass(String className, String source) throws Exception {
		JavaSource sourceCode = new JavaSource(className, source);
		FileManager fileManager = new FileManager(className);
		JAVAC.getTask(null, fileManager, null, null, null, Arrays.asList(sourceCode)).call();
		return fileManager.loadClass(className);
	}

	public static byte[] getBytecode(String className, String source) {
		FileManager fileManager = new FileManager(className);
		JAVAC.getTask(null, fileManager, null, null, null, Arrays.asList(new JavaSource(className, source))).call();
		return fileManager.getBytecode(className);
	}

}
