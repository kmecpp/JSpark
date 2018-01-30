package com.kmecpp.jspark.compiler.parser.statement.block;

import java.util.ArrayList;
import java.util.HashMap;

import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;
import com.kmecpp.jspark.compiler.tokenizer.Token;

public abstract class AbstractBlock extends Statement {

	protected final ArrayList<Statement> statements = new ArrayList<>();
	//	protected final HashSet<String> variableNames = new HashSet<>();

	//Runtime
	protected final HashMap<String, Variable> variables = new HashMap<>();

	public AbstractBlock(AbstractBlock parent) {
		super(parent);
	}

	public AbstractBlock getParentBlock() {
		return block;
	}

	@Override
	public boolean isBlock() {
		return true;
	}

	public Module getModule() {
		return block == null ? (Module) this : block.getModule();
	}

	public HashMap<String, Variable> getVariables() {
		return variables;
	}

	//	public HashMap<String, Object> getVariables() {
	//		return variables;
	//	}

	public boolean isVariableDefined(String variableName) {
		return getVariable(variableName) != null;
		//		return variables.containsKey(variableName);
	}

	public Variable getVariable(Token token) {
		return getVariable(token.getText());
	}

	public Variable getVariable(String variableName) {
		Variable var = variables.get(variableName);
		if (var == null && block != null) {
			return block.getVariable(variableName); //This handles variable scope
		}
		return var;
	}

	public String getAvailableVariableName(String prefix) {
		String name = prefix;
		if (isVariableDefined(prefix)) {
			int i = 0;
			while (isVariableDefined(name + prefix)) {
				i++;
			}
			return prefix + i;
		} else {
			return prefix;
		}
	}

	//	public Object getValue(String variableName) {
	//		return getVariable(variableName).getExpression();
	//	}

	//	public Variable defineVariable(Variable variable) {
	//		variables.put(variable.getName(), variable);
	//		System.out.println("DEFINITING: " + variables);
	//		return variable;
	//	}

	public void addStatement(Statement statement) {
		//		if (statement instanceof VariableDeclaration) {
		//			VariableDeclaration variableDeclaration = (VariableDeclaration) statement;
		//			variables.put(variableDeclaration.getName(), variableDeclaration.getVariable());
		//		}
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
		StringBuilder sb = new StringBuilder("{");
		for (Statement statement : statements) {
			sb.append(statement.toJavaCode() + (statement.isBlock() ? "" : ";"));
		}
		sb.append("}");
		return sb.toString();
	}

}
