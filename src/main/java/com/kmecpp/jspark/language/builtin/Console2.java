package com.kmecpp.jspark.language.builtin;

import com.kmecpp.jspark.compiler.parser.data.Parameter;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.Method;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Class;
import com.kmecpp.jspark.language.PrimitiveType;

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
			super(instance, "println", new Parameter[] { new Parameter(PrimitiveType.OBJECT, "obj") });
		}

		@Override
		public Variable invoke(Variable... values) {
			return null;
		}

	}

	public static void println(Object obj) {
		System.out.println(obj);
	}

}
