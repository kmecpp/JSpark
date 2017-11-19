package com.kmecpp.jspark.parser;

public class Import {

	private final String string;
	private final String className;

	public Import(String string, String className) {
		this.string = string;
		this.className = className;
	}

	public String getString() {
		return string;
	}

	public String getClassName() {
		return className;
	}

}
