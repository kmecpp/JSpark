package com.kmecpp.jspark.parser.statements.modules;

import java.util.ArrayList;

import com.kmecpp.jspark.parser.statements.Variable;

public class Method extends Module {

	private ArrayList<Variable> args;

	public Method(String name, ArrayList<Variable> args) {
		super(name);
		this.args = args;
	}

	public ArrayList<Variable> getArgs() {
		return args;
	}

}
