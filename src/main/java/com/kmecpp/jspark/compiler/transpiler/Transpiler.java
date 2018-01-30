package com.kmecpp.jspark.compiler.transpiler;

import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;

public class Transpiler {

	private Module module;

	public Transpiler(Module module) {
		this.module = module;
	}

	public String getJavaCode() {
		return module.toJavaCode();
	}

	public String getFormattedJava() {
		StringBuilder sb = new StringBuilder();
		String code = getJavaCode();

		StringBuilder indent = new StringBuilder();
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);

			if (c == '{') {
				sb.append(" " + c);
				indent.append('\t');
				sb.append("\n" + indent);
			} else if (c == '}') {
				indent.deleteCharAt(indent.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
				sb.append(c + "\n" + indent);
			} else if (c == ';') {
				sb.append(c + "\n" + indent);
			} else {
				sb.append(c);
			}

		}
		return sb.toString();
	}

	public static String getJavaType(Type type) {
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
