package com.kmecpp.jspark.parser.statement.block;

import java.util.ArrayList;
import java.util.HashMap;

import com.kmecpp.jspark.parser.data.Variable;
import com.kmecpp.jspark.parser.statement.Statement;
import com.kmecpp.jspark.parser.statement.block.module.Module;

public class AbstractBlock extends Statement {

	private final AbstractBlock parent;
	private final ArrayList<Statement> statements;
	private final HashMap<String, Variable> variables;

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

	public Variable getVariable(String name) {
		Variable var = variables.get(name);
		if (var == null && parent != null) {
			return parent.getVariable(name); //This handles variable scope
		}
		return var;
	}

	public void defineVariable(Variable variable) {
		variables.put(variable.getName(), variable);
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
