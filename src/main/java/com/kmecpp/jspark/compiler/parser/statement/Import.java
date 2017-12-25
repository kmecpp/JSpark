package com.kmecpp.jspark.compiler.parser.statement;

import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;

public class Import extends Statement {

	private final String fullName;
	private final String className;

	public Import(AbstractBlock block, String fullName, String className) {
		super(block);
		this.fullName = fullName;
		this.className = className;
	}

	public String getFullName() {
		return fullName;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public void execute() {
		//TODO: Add to namespace
	}

	@Override
	public String toJavaCode() {
		return "import " + fullName + ";";
	}

}
