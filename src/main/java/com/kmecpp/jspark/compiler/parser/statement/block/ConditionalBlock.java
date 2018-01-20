package com.kmecpp.jspark.compiler.parser.statement.block;

import com.kmecpp.jspark.compiler.parser.statement.Statement;

public class ConditionalBlock extends Statement {

	public ConditionalBlock(AbstractBlock block) {
		super(block);
	}

	@Override
	public void execute() {
	}

	@Override
	public String toJavaCode() {
		return null;
	}

}
