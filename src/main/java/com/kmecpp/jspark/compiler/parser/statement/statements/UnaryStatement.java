package com.kmecpp.jspark.compiler.parser.statement.statements;

import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.language.Operator;

public class UnaryStatement extends Statement {

	private final Variable variable;
	private final Operator unaryOperator;

	public UnaryStatement(AbstractBlock block, Variable variable, Operator unaryOperator) {
		super(block);
		if (!unaryOperator.isUnary()) {
			throw new IllegalArgumentException("Not a unary operator: " + unaryOperator);
		}
		this.variable = variable;
		this.unaryOperator = unaryOperator;
	}

	public Operator getUnaryOperator() {
		return unaryOperator;
	}

	@Override
	public void execute() {
		if (variable.isInteger()) {
			variable.setValue(unaryOperator.applyInt(variable));
		} else {
			variable.setValue(unaryOperator.applyDouble(variable));
		}
	}

	@Override
	public String toJavaCode() {
		return variable.getName() + unaryOperator.getString();
	}

}
