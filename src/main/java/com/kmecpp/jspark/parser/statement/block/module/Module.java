package com.kmecpp.jspark.parser.statement.block.module;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kmecpp.jspark.language.Type;
import com.kmecpp.jspark.parser.data.Value;
import com.kmecpp.jspark.parser.statement.Import;
import com.kmecpp.jspark.parser.statement.block.Method;
import com.kmecpp.jspark.parser.statement.block.NamedBlock;

public abstract class Module extends NamedBlock {

	private Path path;
	private ArrayList<Import> imports = new ArrayList<>();
	private ArrayList<Field> fields = new ArrayList<>();
	private ArrayList<Method> methods = new ArrayList<>();

	public Module(Path path, String name) {
		super(name, null);
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

	public String getFileName() {
		return path.getFileName().toString();
	}

	public ArrayList<Import> getImports() {
		return imports;
	}

	public void addImport(Import imprt) {
		imports.add(imprt);
	}

	public Optional<Import> getImport(String className) {
		for (Import imprt : imports) {
			if (imprt.getClassName().equals(className)) {
				return Optional.of(imprt);
			}
		}
		return Optional.empty();
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void addField(Field field) {
		fields.add(field);
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

	public void executeStaticMethod(String name, ArrayList<Value> args) {
		Type[] types = new Type[args.size()];
		for (int i = 0; i < types.length; i++) {
			types[i] = args.get(i).getType();
		}
		Optional<Method> method = getMethod(name, types);
		if (method.isPresent()) {
			method.get().execute();
		} else {
			throw new IllegalArgumentException("Method does not exist: " + name + "(" + String.join(", ", Arrays.stream(types).map(Type::getIdentifier).collect(Collectors.toList())) + ")");
		}
	}

}
