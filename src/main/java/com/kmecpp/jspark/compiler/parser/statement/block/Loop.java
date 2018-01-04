package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.stream.Collectors;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.VariableDeclaration;

public class Loop extends AbstractBlock {

	private VariableDeclaration initialization;
	private Expression termination;
	private AnonymousBlock increment;

	public Loop(AbstractBlock parent) {//, Expression termination, ArrayList<Statement> increment) {
		super(parent);
		//		this.initialization = initialization;
		//		this.termination = termination;
		//		this.increment = increment;
	}

	public void setInitialization(VariableDeclaration initialization) {
		this.initialization = initialization;
	}

	public void setTermination(Expression termination) {
		this.termination = termination;
	}

	public void setIncrement(AnonymousBlock increment) {
		this.increment = increment;
	}

	public VariableDeclaration getInitialization() {
		return initialization;
	}

	public Expression getTermination() {
		return termination;
	}

	public AnonymousBlock getIncrement() {
		return increment;
	}

	@Override
	public void execute() {
		initialization.execute();
		while ((boolean) termination.evaluate()) {
			super.execute();
			for (Statement assignment : increment.getStatements()) {
				assignment.execute();
			}
		}
	}

	@Override
	public String toJavaCode() {
		return "for(" + initialization.toJavaCode() + ";" + termination + ";" + increment.toJavaCode() + ") {"
				+ String.join(";", statements.stream().map(String::valueOf).collect(Collectors.toList()))
				+ "}";
	}

}
