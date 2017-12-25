package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;

public abstract class AbstractBlock extends Statement {

	//	protected final AbstractBlock parent;
	protected final ArrayList<Statement> statements;
	protected final HashMap<String, Variable> variables;

	//	public AbstractBlock() {
	//		this(new ArrayList<>());
	//	}

	public AbstractBlock(AbstractBlock parent) {
		super(parent);
		//		this.parent = parent;
		this.statements = new ArrayList<>();
		this.variables = new HashMap<>();
	}

	public AbstractBlock getParentBlock() {
		return block;
	}

	public Module getModule() {
		return block == null ? (Module) this : block.getModule();
	}

	public Collection<Variable> getVars() {
		return variables.values();
	}

	//	public HashMap<String, Object> getVariables() {
	//		return variables;
	//	}

	public Variable getVarData(String variableName) {
		Variable var = variables.get(variableName);
		if (var == null && block != null) {
			return block.getVarData(variableName); //This handles variable scope
		}
		return var;
	}

	public Object getVar(String variableName) {
		return getVarData(variableName).getValue();
	}

	public Variable defineVariable(Variable variable) {
		variables.put(variable.getName(), variable);
		return variable;
	}

	public void addStatements(ArrayList<Statement> statements) {
		this.statements.addAll(statements);
	}

	public void addStatement(Statement statement) {
		this.statements.add(statement);
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

	@Override
	public String toJavaCode() {
		StringBuilder sb = new StringBuilder();
		for (Statement statement : statements) {
			sb.append(statement);
		}
		return sb.toString();
	}

}
