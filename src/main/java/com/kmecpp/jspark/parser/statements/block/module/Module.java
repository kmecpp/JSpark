package com.kmecpp.jspark.parser.statements.block.module;

import java.util.ArrayList;
import java.util.Optional;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.Import;
import com.kmecpp.jspark.parser.statements.block.Method;
import com.kmecpp.jspark.parser.statements.block.NamedBlock;

public abstract class Module extends NamedBlock {

	private ArrayList<Import> imports = new ArrayList<>();
	private ArrayList<Method> methods = new ArrayList<>();

	public Module(String name) {
		super(name, new ArrayList<>());
	}

	public ArrayList<Method> getMethods() {
		return methods;
	}

	public void addMethod(Method method) {
		methods.add(method);
	}

	public Optional<Method> getMethod(String name, Type... params) {
		for (Method method : methods) {
			if (method.matches(name, params)) {
				return Optional.of(method);
			}
		}
		return Optional.empty();
	}

	public boolean hasMethod(String name, Type... params) {
		return getMethod(name, params).isPresent();
	}

	public ArrayList<Import> getImports() {
		return imports;
	}

	public void addImport(Import imprt) {
		imports.add(imprt);
	}

}
