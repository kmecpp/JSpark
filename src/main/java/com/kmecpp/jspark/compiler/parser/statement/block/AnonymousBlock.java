package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.stream.Collectors;

import com.kmecpp.jspark.compiler.parser.statement.Statement;

public class AnonymousBlock extends AbstractBlock {

	public AnonymousBlock(AbstractBlock parent) {
		super(parent);
	}

	public AnonymousBlock(AbstractBlock parent, Statement... statements) {
		super(parent);
		for (Statement statement : statements) {
			this.statements.add(statement);
		}
	}

	@Override
	public String toString() {
		return String.join(", ", statements.stream().map(String::valueOf).collect(Collectors.toList()));
	}

}
