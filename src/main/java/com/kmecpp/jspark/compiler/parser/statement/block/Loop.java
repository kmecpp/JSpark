package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.ArrayList;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.statement.Statement;

public class Loop extends AbstractBlock {

	//	private VariableDeclaration initialization;
	private Expression termination;
	private ArrayList<Statement> increment;

	public Loop(AbstractBlock parent) {//, Expression termination, ArrayList<Statement> increment) {
		super(parent);
		//		this.initialization = initialization;
		//		this.termination = termination;
		//		this.increment = increment;
	}

	public void setTermination(Expression termination) {
		this.termination = termination;
	}

	public void setIncrement(ArrayList<Statement> increment) {
		this.increment = increment;
	}

	//	public VariableDeclaration getInitialization() {
	//		return initialization;
	//	}

	public Expression getTermination() {
		return termination;
	}

	public ArrayList<Statement> getIncrement() {
		return increment;
	}

	@Override
	public void execute() {
		//		initialization.execute();
		while ((boolean) termination.evaluate()) {
			super.execute();
			for (Statement assignment : increment) {
				assignment.execute();
			}
		}
	}

	@Override
	public String toJavaCode() {
		StringBuilder sb = new StringBuilder();
		return "for(" + "";
	}

}
