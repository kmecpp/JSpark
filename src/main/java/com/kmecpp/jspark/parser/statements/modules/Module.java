package com.kmecpp.jspark.parser.statements.modules;

import java.util.ArrayList;

import com.kmecpp.jspark.parser.Import;
import com.kmecpp.jspark.parser.NamedBlock;

public class Module extends NamedBlock {

	private ArrayList<Import> imports = new ArrayList<>();

	public Module(String name) {
		super(name);
	}

	public ArrayList<Import> getImports() {
		return imports;
	}

	public void addImport(Import imprt) {
		imports.add(imprt);
	}

}
