package com.kmecpp.jspark.compiler.parser.statement;

import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;

public class Instantiation extends Statement {

	private Variable variable;
	private Module module;

	public Instantiation(Variable variable, Module module) {
		this.variable = variable;
		this.module = module;
	}

	public Variable getVariable() {
		return variable;
	}

	public Module getModule() {
		return module;
	}

	@Override
	public void execute() {
	}

}
