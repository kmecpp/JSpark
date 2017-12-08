package com.kmecpp.jspark.compiler.transpiler;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.statement.block.module.Field;
import com.kmecpp.jspark.parser.statement.block.module.Module;

public class JavaTranspiler {

	private Module module;

	public JavaTranspiler(Module module) {
		this.module = module;
	}

	public String transpile() {
		StringBuilder sb = new StringBuilder();

		sb.append("package " + module.getFileName());

		sb.append("public class " + module.getName() + "{");
		for (Field field : module.getFields()) {
			sb.append("private " + getJavaType(field.getType()));
		}
		sb.append("}");

		return sb.toString();
	}

	private static String getJavaType(Type type) {
		switch (type) {
		case BOOLEAN:
			return "boolean";
		case DECIMAL:
			return "double";
		case INTEGER:
			return "int";
		case STRING:
			return "String";
		default:
			throw new IllegalArgumentException("Invalid type! " + type);
		}
	}

	public Module getModule() {
		return module;
	}

}
