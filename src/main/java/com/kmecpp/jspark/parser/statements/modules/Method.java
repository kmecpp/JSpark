package com.kmecpp.jspark.parser.statements.modules;

import java.util.ArrayList;

import com.kmecpp.jspark.parser.NamedBlock;
import com.kmecpp.jspark.parser.statements.Variable;

public class Method extends NamedBlock {

	private ArrayList<Variable> args;

	public Method(String name, ArrayList<Variable> args) {
		super(name);
		this.args = args;
	}

	public ArrayList<Variable> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		ArrayList<String> vars = new ArrayList<>();
		for (Variable var : args) {
			vars.add(var.toString());
		}
		return getName() + "(" + String.join(", ", vars) + ")";
	}

}
