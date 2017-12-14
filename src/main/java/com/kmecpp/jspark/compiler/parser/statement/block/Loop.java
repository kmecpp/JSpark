package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.ArrayList;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Variable;

public class Loop extends AbstractBlock {

	private Expression test;

	public Loop(AbstractBlock parent, ArrayList<Variable> variables, Expression test) {
		super(parent);
		for (Variable variable : variables) {
			this.defineVariable(variable);
		}
		this.test = test;
	}

	@Override
	public void execute() {
		while ((boolean) test.evaluate()) {
			super.execute();
		}
	}

}
