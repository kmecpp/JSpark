package com.kmecpp.jspark.compiler.parser.statement.statements;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;

public class VariableAssignment extends Statement {

	//	private final AbstractBlock block;
	private final Variable variable;
	private final Expression newValue;

	public VariableAssignment(AbstractBlock block, Variable variable, String expression) {
		this(block, variable, new Expression(block, expression));
	}

	public VariableAssignment(AbstractBlock block, Variable variable, Expression newValue) {
		super(block);
		//		this.block = block;
		this.variable = variable;
		this.newValue = newValue;
	}

	public AbstractBlock getBlock() {
		return block;
	}

	public Variable getVariable() {
		return variable;
	}

	public Expression getNewValue() {
		return newValue;
	}

	@Override
	public void execute() {
		variable.setValue(newValue.evaluate());
	}

	@Override
	public String toString() {
		return variable.getName() + " = " + newValue.toString();
	}

	@Override
	public String toJavaCode() {
		return toString() + ";";
	}

}
