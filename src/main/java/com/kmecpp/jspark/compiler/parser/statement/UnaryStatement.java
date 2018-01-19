package com.kmecpp.jspark.compiler.parser.statement;

import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.language.Operator;

public class UnaryStatement extends Statement {

	private final String variableName;
	private final Operator unaryOperator;

	public UnaryStatement(AbstractBlock block, String variableName, Operator unaryOperator) {
		super(block);
		if (!unaryOperator.isUnary()) {
			throw new IllegalArgumentException("Not a unary operator: " + unaryOperator);
		}
		this.variableName = variableName;
		this.unaryOperator = unaryOperator;
	}

	public Operator getUnaryOperator() {
		return unaryOperator;
	}

	@Override
	public void execute() {
		Variable variable = block.getVariable(variableName);
		if (variable.isInteger()) {
			variable.setValue(unaryOperator.applyInt(variable));
		} else {
			variable.setValue(unaryOperator.applyDouble(variable));
		}
	}

	@Override
	public String toJavaCode() {
		return variableName + unaryOperator.getString() + ";";
	}

}
