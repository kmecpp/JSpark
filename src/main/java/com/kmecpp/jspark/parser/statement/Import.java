package com.kmecpp.jspark.parser.statement;

public class Import {

	private final String fullName;
	private final String className;

	public Import(String fullName, String className) {
		this.fullName = fullName;
		this.className = className;
	}

	public String getFullName() {
		return fullName;
	}

	public String getClassName() {
		return className;
	}

}
