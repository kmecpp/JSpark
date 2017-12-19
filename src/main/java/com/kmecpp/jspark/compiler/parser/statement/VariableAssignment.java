package com.kmecpp.jspark.compiler.parser.statement;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;

public class VariableAssignment extends Statement {

	private final AbstractBlock block;
	private final String variableName;
	private final Expression newValue;

	public VariableAssignment(AbstractBlock block, String variableName, Expression newValue) {
		this.block = block;
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
		block.getVarData(variableName).setValue(newValue.evaluate());
	}

}
