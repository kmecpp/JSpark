package com.kmecpp.jspark.parser;

import java.util.ArrayList;

public class Statement {

	private ArrayList<Expression> expressions;

	public Statement(Expression expression) {
		this.expressions = new ArrayList<>();
		this.expressions.add(expression);
	}

	public Statement(ArrayList<Expression> expressions) {
		this.expressions = expressions;
	}

	public void execute() {
		for (Expression expression : expressions) {
			expression.evaluate();
		}
	}

}
