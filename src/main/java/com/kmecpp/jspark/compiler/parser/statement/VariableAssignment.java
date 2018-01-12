package com.kmecpp.jspark.compiler.parser.statement;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;

public class VariableAssignment extends Statement {

	//	private final AbstractBlock block;
	private final String variableName;
	private final Expression newValue;

	public VariableAssignment(AbstractBlock block, String variableName, Expression newValue) {
		super(block);
		//		this.block = block;
		this.variableName = variableName;
		this.newValue = newValue;
	}

	public AbstractBlock getBlock() {
		return block;
	}

	public String getVariableName() {
		return variableName;
	}

	public Expression getNewValue() {
		return newValue;
	}

	@Override
	public void execute() {
		block.getVariable(variableName).setValue(newValue.evaluate());
	}

	@Override
	public String toString() {
		return variableName + " = " + newValue.toString();
	}

	@Override
	public String toJavaCode() {
		return toString();
	}

}
