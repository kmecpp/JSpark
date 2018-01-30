package com.kmecpp.jspark.language.builtin;

import com.kmecpp.jspark.compiler.parser.data.Parameter;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.statement.block.Method;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Class;

public class Console2 extends Class {

	private static final Console2 instance = new Console2("Console");

	public Console2(String name) {
		super(null, name);

		addMethod(new Println());
	}

	public static Console2 getInstance() {
		return instance;
	}

	private class Println extends Method {

		public Println() {
			super(instance, "println", new Parameter[] { new Parameter(Type.STRING, "obj") });
		}

	}

	public static void println(Object obj) {
		System.out.println(obj);
	}

}
