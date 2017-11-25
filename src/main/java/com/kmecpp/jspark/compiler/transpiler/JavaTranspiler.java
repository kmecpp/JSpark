package com.kmecpp.jspark.compiler.transpiler;

import com.kmecpp.jspark.parser.statement.block.module.Module;

public class JavaTranspiler {

	private Module module;

	public JavaTranspiler(Module module) {
		this.module = module;
	}

	public String transpile() {
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	public Module getModule() {
		return module;
	}

}
