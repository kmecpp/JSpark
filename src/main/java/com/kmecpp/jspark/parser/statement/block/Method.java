package com.kmecpp.jspark.parser.statement.block;

import java.util.ArrayList;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.data.Parameter;
import com.kmecpp.jspark.parser.data.Value;
import com.kmecpp.jspark.parser.statement.Statement;

public class Method extends NamedBlock {

	private Parameter[] parameters;

	public Method(AbstractBlock parent, String name, Parameter[] params) {
		super(name, parent);
		this.parameters = params;
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
			if (this.parameters.length != params.length) {
				return false;
			}
			for (int i = 0; i < params.length; i++) {
				if (this.parameters[i].getType() != params[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	@Override
	public final void execute() {
		invoke();
	}

	public Value invoke(Value... values) {
		for (Statement statement : statements) {
			statement.execute();
		}
		return null;
	}

	@Override
	public String toString() {
		ArrayList<String> vars = new ArrayList<>();
		for (Parameter param : parameters) {
			vars.add(param.getType().getIdentifier() + " " + param.getName());
		}
		return getName() + "(" + String.join(", ", vars) + ")";
	}

}
