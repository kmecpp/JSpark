package com.kmecpp.jspark.compiler.parser.statement.statements;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.Method;

public class ReturnStatement extends Statement {

	private Expression expression;;

	public ReturnStatement(Method block, Expression expression) {
		super(block);
		this.expression = expression;
	}

	public Type getType() {
		if (expression == null) {
			return Type.VOID;
		}
		return expression.getResultType();
	}

	@Override
	public void execute() {
		Method method = (Method) this.block;
		method.setResult(expression.evaluate());
	}

	@Override
	public String toJavaCode() {
		return "return " + expression;
	}

}
