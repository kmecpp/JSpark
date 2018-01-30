package com.kmecpp.jspark.compiler.parser.statement.block;

import com.kmecpp.jspark.compiler.parser.Expression;

public class Conditional extends AbstractBlock {

	private Expression expression;
	private Conditional negativeConditional;

	public Conditional(AbstractBlock block) {
		super(block);
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public Conditional getNegativeConditional() {
		return negativeConditional;
	}

	public void setNegativeConditional(Conditional negativeConditional) {
		this.negativeConditional = negativeConditional;
	}

	@Override
	public void execute() {
		if (expression == null || (boolean) expression.evaluate()) {
			super.execute();
		} else if (negativeConditional != null) {
			negativeConditional.execute();
		}
	}

	@Override
	public String toJavaCode() {
		if (expression == null) {
			return super.toJavaCode();
		}
		return "if (" + expression + ")"
				+ super.toJavaCode()
				+ (negativeConditional != null ? "else" + (negativeConditional.expression != null ? " " : "") + negativeConditional.toJavaCode() : "");
	}

}
