package com.kmecpp.jspark.parser.statement.logic;

import java.util.ArrayList;
import java.util.Optional;

import com.kmecpp.jspark.JSpark;
import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.language.builtin.Console;
import com.kmecpp.jspark.parser.Expression;
import com.kmecpp.jspark.parser.data.Value;
import com.kmecpp.jspark.parser.statement.Import;
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
		Value target = Keyword.THIS.is(this.target) ? new Value(Type.OBJECT, parent.getModule()) : parent.getVariable(this.target);

		if (target == null) {
			Optional<Import> imprt = parent.getModule().getImport(this.target);
			if (imprt.isPresent()) {
				Module module = JSpark.getRuntime().getModule(imprt.get().getClassName());
				module.execute();
			} else if (this.target.equals("Console")) {
				if (this.method.equals("println")) {
					Console.println(params.get(0).evaluate().getValue());
				}
			} else {

			}

		}
	}

}
