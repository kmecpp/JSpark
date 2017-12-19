package com.kmecpp.jspark.compiler.transpiler;

import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;

public class JavaTranspiler {

	private Module module;

	public JavaTranspiler(Module module) {
		this.module = module;
	}

	public String transpile() {
		StringBuilder sb = new StringBuilder();

		sb.append("package " + module.getPackage());

		sb.append("public class " + module.getName() + "{");
		for (Variable field : module.getFields()) {
			sb.append("private " + getJavaType(field.getType()));
		}
		sb.append("}");

		return sb.toString();
	}

	private static String getJavaType(Type type) {
		if (type.isPrimitive()) {
			switch (type.getPrimitiveType()) {
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
		return type.getFullName();
	}

	public Module getModule() {
		return module;
	}

}
