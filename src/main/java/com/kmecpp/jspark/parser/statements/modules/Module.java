package com.kmecpp.jspark.parser.statements.modules;

import com.kmecpp.jspark.parser.CodeBlock;

public class Module extends CodeBlock {

	private String name;

	public Module(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
