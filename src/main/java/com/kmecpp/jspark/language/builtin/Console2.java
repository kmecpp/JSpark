package com.kmecpp.jspark.language.builtin;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.data.Parameter;
import com.kmecpp.jspark.parser.data.Value;
import com.kmecpp.jspark.parser.statement.block.Method;
import com.kmecpp.jspark.parser.statement.block.module.Class;

public class Console2 extends Class {

	private static final Console2 instance = new Console2("Console");

	public Console2(String name) {
		super(name);

		addMethod(new Println());
	}

	public static Console2 getInstance() {
		return instance;
	}

	private class Println extends Method {

		public Println() {
			super(instance, "println", new Parameter[] { new Parameter(Type.OBJECT, "obj") });
		}

		@Override
		public Value invoke(Value... values) {
			return null;
		}

	}

	public static void println(Object obj) {
		System.out.println(obj);
	}

}
