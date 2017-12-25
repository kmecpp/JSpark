package com.kmecpp.jspark.compiler.parser.statement;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;

public class VariableDeclaration extends Statement {

	//	private final AbstractBlock block;

	private final Type type;
	private final String name;
	private final Expression expression;

	public VariableDeclaration(AbstractBlock block, Type type, String name, Expression expression) {
		super(block);
		//		this.block = block;
		this.type = type;
		this.name = name;
		this.expression = expression;
	}

	public AbstractBlock getBlock() {
		return block;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public void execute() {
		Object value;
		if (expression != null) {
			value = expression.evaluate();
		} else if (type.isPrimitive()) {
			value = type.getPrimitiveType().getDefaultValue();
		} else {
			value = null;
		}
		block.defineVariable(new Variable(type, name, value));
	}

	@Override
	public String toJavaCode() {
		return type.getName() + name + " = " + expression;
	}

}
