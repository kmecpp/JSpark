package com.kmecpp.jspark.parser.statement.block;

import java.util.ArrayList;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.data.Variable;

public class Method extends NamedBlock {

	private ArrayList<Variable> args;

	public Method(AbstractBlock parent, String name, ArrayList<Variable> args) {
		super(name, parent);
		this.args = args;
	}

	/**
	 * Tests whether the given Method name and parameter types matches with this
	 * Method's. This method will only return true if the Method name AND the
	 * Method parameters match exactly.
	 * 
	 * @param name
	 *            the name of the method
	 * @param params
	 *            the type parameters
	 * @return true if the methods are identical, otherwise false
	 */
	public boolean matches(String name, Type... params) {
		if (getName().equals(name)) {
			if (args.size() != params.length) {
				return false;
			}
			for (int i = 0; i < params.length; i++) {
				if (args.get(i).getType() != params[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
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
