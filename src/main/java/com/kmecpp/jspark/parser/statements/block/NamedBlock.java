package com.kmecpp.jspark.parser.statements.block;

import java.util.ArrayList;

import com.kmecpp.jspark.parser.Statement;

public abstract class NamedBlock extends AbstractBlock {

	private String name;

	//	public NamedBlock(String name) {
	//		this.name = name;
	//	}

	public NamedBlock(String name, ArrayList<Statement> statements) {
		super(statements);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
