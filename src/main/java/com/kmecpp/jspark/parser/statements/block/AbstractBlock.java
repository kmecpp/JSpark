package com.kmecpp.jspark.parser.statements.block;

import java.util.ArrayList;

import com.kmecpp.jspark.parser.Statement;

public class AbstractBlock extends Statement {

	private ArrayList<Statement> statements = new ArrayList<>();

	//	public AbstractBlock() {
	//		this(new ArrayList<>());
	//	}

	public AbstractBlock(ArrayList<Statement> statements) {
		this.statements = statements;
	}

	public void addStatement(Statement statement) {
		statements.add(statement);
	}

	public ArrayList<Statement> getStatements() {
		return statements;
	}

}
