package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.stream.Collectors;

public class AnonymousBlock extends AbstractBlock {

	public AnonymousBlock(AbstractBlock parent) {
		super(parent);
	}

	@Override
	public String toString() {
		return String.join(", ", statements.stream().map(String::valueOf).collect(Collectors.toList()));
	}

}
