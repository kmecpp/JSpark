package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.ArrayList;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Variable;

public class Loop extends AbstractBlock {

	private ArrayList<Variable> variables;
	private Expression test;
	private Runnable iterate;

	public Loop(AbstractBlock parent, ArrayList<Variable> variables, Expression test, Runnable iterate) {
		super(parent);
		this.variables = variables;
		this.test = test;
		this.iterate = iterate;
	}

}
