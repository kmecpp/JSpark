package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;

public class AbstractBlock extends Statement {

	protected final AbstractBlock parent;
	protected final ArrayList<Statement> statements;
	protected final HashMap<String, Variable> variables;

	//	public AbstractBlock() {
	//		this(new ArrayList<>());
	//	}

	public AbstractBlock(AbstractBlock parent) {
		this.parent = parent;
		this.statements = new ArrayList<>();
		this.variables = new HashMap<>();
	}

	public AbstractBlock getParent() {
		return parent;
	}

	public Module getModule() {
		return parent == null ? (Module) this : parent.getModule();
	}

	public Collection<Variable> getVars() {
		return variables.values();
	}

	//	public HashMap<String, Object> getVariables() {
	//		return variables;
	//	}

	public Variable getVariable(String variableName) {
		Variable var = variables.get(variableName);
		if (var == null && parent != null) {
			return parent.getVariable(variableName); //This handles variable scope
		}
		return var;
	}

	public Variable defineVariable(Variable variable) {
		variables.put(variable.getName(), variable);
		return variable;
	}

	public void addStatements(ArrayList<Statement> statements) {
		this.statements.addAll(statements);
	}

	public ArrayList<Statement> getStatements() {
		return statements;
	}

	@Override
	public void execute() {
		for (Statement statement : statements) {
			statement.execute();
		}
	}

}
