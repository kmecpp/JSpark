package com.kmecpp.jspark.parser.statements.modules;

import java.util.ArrayList;

import com.kmecpp.jspark.parser.Statement;

public class Module extends Statement {

	private String name;

	public Module(String name) {
		super(new ArrayList<>());
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addStatement(Statement statement) {
		super.expressions.add(e)
	}

}
