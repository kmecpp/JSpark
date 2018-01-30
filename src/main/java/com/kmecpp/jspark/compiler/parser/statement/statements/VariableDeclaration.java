package com.kmecpp.jspark.compiler.parser.statement.statements;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;

public class VariableDeclaration extends Statement {

	private final Variable variable;
	private final Expression expression;

	public VariableDeclaration(AbstractBlock block, Variable variable, String expression) {
		this(block, variable, new Expression(block, expression));
	}

	public VariableDeclaration(AbstractBlock block, Variable variable, Expression expression) {
		super(block);
		this.variable = variable;
		this.expression = expression;
		block.getVariables().put(variable.getName(), variable);
	}

	//	public VariableDeclaration(AbstractBlock block, Type type, String name, Expression expression) {
	//		super(block);
	//		//		this.block = block;
	//		this.type = type;
	//		this.name = name;
	//		this.expression = expression;
	//
	//		//		block.defineVariable(type, name, expression)
	//	}

	public AbstractBlock getBlock() {
		return block;
	}

	public String getName() {
		return variable.getName();
	}

	public Type getType() {
		return variable.getType();
	}

	public Variable getVariable() {
		return variable;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public void execute() {
		//		Object value;
		if (expression != null) {
			variable.setValue(expression.evaluate());
		} else if (variable.getType().isPrimitive()) {
			variable.setValue(variable.getType().getPrimitiveType().getDefaultValue());
		} else {
		}

		//		block.defineVariable(new Variable(type, name, value));
	}

	@Override
	public String toString() {
		return variable.toString();
		//		return variable.getType().getName() + " " + variable.getName() + " = " + expression;
	}

	@Override
	public String toJavaCode() {
		return toString();
	}

}
