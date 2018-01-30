package com.kmecpp.jspark.compiler.parser.statement.block;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.statements.VariableDeclaration;

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
		if (initialization != null) {
			initialization.execute();
		}
		while ((boolean) termination.evaluate()) {
			super.execute();
			if (increment != null) {
				for (Statement assignment : increment.getStatements()) {
					assignment.execute();
				}
			}
		}
	}

	@Override
	public String toJavaCode() {
		StringBuilder statements = new StringBuilder();
		for (Statement statement : increment.getStatements()) {
			statements.append(statement.toJavaCode() + ", ");
		}
		statements.delete(statements.length() - 2, statements.length());

		return "for(" + initialization.toJavaCode() + "; " + termination + "; " + statements + ")" + super.toJavaCode();
	}

}
