package com.kmecpp.jspark.parser.statements.block;

public class NamedBlock extends AbstractBlock {

	private String name;

	public NamedBlock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
