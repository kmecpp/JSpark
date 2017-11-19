package com.kmecpp.jspark.parser;

public class NamedBlock extends AbstractBlock {

	private String name;

	public NamedBlock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
