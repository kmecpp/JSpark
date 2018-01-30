package com.kmecpp.jspark.compiler.parser.statement;

import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;

public abstract class Statement {

	protected final AbstractBlock block;

	public Statement(AbstractBlock block) {
		this.block = block;
	}

	public boolean isBlock() {
		return false;
	}

	public abstract void execute();

	public abstract String toJavaCode();

}
