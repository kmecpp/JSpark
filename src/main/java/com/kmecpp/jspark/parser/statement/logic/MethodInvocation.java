package com.kmecpp.jspark.parser.statement.logic;

import java.util.ArrayList;

import com.kmecpp.jspark.JSpark;
import com.kmecpp.jspark.parser.Expression;
import com.kmecpp.jspark.parser.data.Variable;
import com.kmecpp.jspark.parser.statement.Statement;
import com.kmecpp.jspark.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.parser.statement.block.module.Module;

public class MethodInvocation extends Statement {

	private AbstractBlock parent;

	private String target;
	private String method;
	private ArrayList<Expression> params;

	public MethodInvocation(AbstractBlock parent, String target, String method, ArrayList<Expression> params) {
		this.parent = parent;
		this.target = target;
		this.method = method;
		this.params = params;
	}

	public AbstractBlock getParent() {
		return parent;
	}

	public String getTarget() {
		return target;
	}

	public String getMethod() {
		return method;
	}

	public ArrayList<Expression> getParams() {
		return params;
	}

	@Override
	public void execute() {
		Variable target = parent.getVariable(this.target);
		if (target == null) {
			Module module = JSpark.getRuntime().getModule(this.target, parent.getModule());
			
		}
	}

}
