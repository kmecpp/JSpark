package com.kmecpp.jspark.parser.statement.block;

public abstract class NamedBlock extends AbstractBlock {

	private String name;

	//	public NamedBlock(String name) {
	//		this.name = name;
	//	}

	public NamedBlock(String name, AbstractBlock parent) {
		super(parent);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
