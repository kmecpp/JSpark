package com.kmecpp.jspark.parser;

import java.util.ArrayList;

public class CodeBlock extends Statement {

	private ArrayList<Statement> statements = new ArrayList<>();

	public void addStatement(Statement statement) {
		statements.add(statement);
	}

	public ArrayList<Statement> getStatements() {
		return statements;
	}

}
