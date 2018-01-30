package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Parameter;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.statements.ReturnStatement;

public class Method extends NamedBlock {

	private boolean mainMethod;
	private Parameter[] parameters;
	private Type returnType = Type.VOID;

	private Object result = null;

	public Method(AbstractBlock parent, String name, Parameter[] params) {
		super(name, parent);
		this.parameters = params;

		if (name.equals("main") && params.length == 0) {
			mainMethod = true;
		}
		for (Parameter parameter : params) {
			variables.put(parameter.getName(), new Variable(parameter.getType(), parameter.getName(), null));
		}
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
				if (!this.parameters[i].getType().equals(params[i])) {
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

	public Type getReturnType() {
		return returnType;
	}

	public boolean isMainMethod() {
		return mainMethod;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public void addStatement(Statement statement) {
		super.addStatement(statement);
		if (statement instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement) statement;
			returnType = returnStatement.getType();
			if (returnType != Type.VOID) {
				mainMethod = false;
			}
		}
	}

	public Variable invoke(ArrayList<Expression> params) {
		for (int i = 0; i < parameters.length; i++) {
			Variable variable = new Variable(parameters[i].getType(), parameters[i].getName(), params.get(i).evaluate());
			variables.put(variable.getName(), variable);
		}
		for (Statement statement : statements) {
			statement.execute();
			if (result != null) {
				return new Variable(returnType, result);
			}
		}
		if (returnType != Type.VOID) {
			throw new RuntimeException("Invalid return type for method: " + name);
		} else {
			return null;
		}
	}

	//	public Variable invoke(Variable... values) {
	//		for (Statement statement : statements) {
	//			statement.execute();
	//			if (result != null) {
	//				return new Variable(returnType, result);
	//			}
	//		}
	//		return null;
	//	}

	@Override
	public String toString() {
		ArrayList<String> vars = new ArrayList<>();
		for (Parameter param : parameters) {
			vars.add(param.getType().getFullName() + " " + param.getName());
		}
		return getName() + "(" + String.join(", ", vars) + ")";
	}

	@Override
	public String toJavaCode() {
		if (mainMethod) {
			return "public static void main(String[] args)" + super.toJavaCode();
		}
		return "public " + returnType + " " + name + "(" + Stream.of(parameters).map(String::valueOf).collect(Collectors.joining(",")) + ")" + super.toJavaCode();
	}

}
