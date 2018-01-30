package com.kmecpp.jspark.compiler.transpiler;

import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
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

		int forLoopBind = 0;
		StringBuilder indent = new StringBuilder();
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);

			if (c == 'f' && code.charAt(i + 1) == 'o' && code.charAt(i + 2) == 'r' && !Character.isLetter(code.charAt(i + 3))) {
				forLoopBind = 2;
			}

			//			if (c == 'd' && code.charAt(i + 1) == 'e' && code.charAt(i + 2) == 'f' && code.charAt(i + 2) == ' ') {
			//				sb.append("\n" + indent);
			//			}

			if (c == '{') {
				sb.append(" " + c);
				indent.append('\t');
				sb.append("\n" + indent);
			} else if (c == '}') {
				indent.deleteCharAt(indent.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
				sb.append(c + "\n" + indent);
			} else if (c == ';') {
				sb.append(c);

				if (forLoopBind <= 0) {
					sb.append("\n" + indent);
				} else {
					forLoopBind--;
				}
			} else {
				sb.append(c);
			}

		}
		return sb.toString();
	}

	public static String toJavaCode(Statement statement) {
		StringBuilder sb = new StringBuilder();
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
