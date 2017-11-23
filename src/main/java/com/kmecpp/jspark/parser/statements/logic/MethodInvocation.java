package com.kmecpp.jspark.parser.statements.logic;

import java.util.ArrayList;

import com.kmecpp.jspark.parser.Statement;
import com.kmecpp.jspark.parser.data.Value;

public class MethodInvocation extends Statement {

	private String target;
	private String method;
	private ArrayList<Value> params;

	public MethodInvocation(String target, String method, ArrayList<Value> params) {
		this.target = target;
		this.method = method;
		this.params = params;
	}

	public String getTarget() {
		return target;
	}

	public String getMethod() {
		return method;
	}

	public ArrayList<Value> getParams() {
		return params;
	}

}
