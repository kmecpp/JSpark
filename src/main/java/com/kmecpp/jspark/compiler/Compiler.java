package com.kmecpp.jspark.compiler;

import com.kmecpp.jspark.parser.statement.block.module.Module;

public class Compiler {

	private Module module;

	public Compiler(Module module) {
		this.module = module;
	}

	public Module getModule() {
		return module;
	}

}
