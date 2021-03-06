package com.kmecpp.jspark.compiler.parser.statement.block.module;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kmecpp.jspark.JSpark;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Import;
import com.kmecpp.jspark.compiler.parser.statement.block.Method;
import com.kmecpp.jspark.compiler.parser.statement.block.NamedBlock;
import com.kmecpp.jspark.compiler.transpiler.Transpiler;

public abstract class Module extends NamedBlock {

	private Path path;
	private String packageName;
	private ArrayList<Import> imports = new ArrayList<>();
	//	private ArrayList<Field> fields = new ArrayList<>();
	private ArrayList<Method> methods = new ArrayList<>();

	public Module(Path path, String name) {
		super(name, null);
		this.path = path;

		Path directory = JSpark.getProjectPath().relativize(path).getParent();
		this.packageName = directory == null ? "" : directory.toString().replace(File.separator, ".");
	}

	public Path getPath() {
		return path;
	}

	public String getPackage() {
		return packageName;
	}

	public String getFileName() {
		return path.getFileName().toString();
	}

	public String getFullName() {
		return (packageName.isEmpty() ? "" : packageName + ".") + name;
	}

	public boolean isClass() {
		return this instanceof Class;
	}

	public boolean isStatic() {
		return this instanceof Static;
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

	public HashMap<String, Variable> getFields() {
		//		return fields;
		return getVariables();
	}

	//	public void addField(Field field) {
	//		fields.add(field);
	//	}

	public ArrayList<Method> getMethods() {
		return methods;
	}

	public void addMethod(Method method) {
		methods.add(method);
	}

	public Optional<Method> getMethod(String name, Type... params) {
		for (Method method : methods) {
			if (method.matches(name, (Type[]) params)) {
				return Optional.of(method);
			}
		}
		return Optional.empty();
	}

	public boolean hasMethod(String name, Type... params) {
		return getMethod(name, params).isPresent();
	}

	public void executeStaticMethod(String name, ArrayList<Variable> args) {
		Type[] types = new Type[args.size()];
		for (int i = 0; i < types.length; i++) {
			types[i] = args.get(i).getType();
		}
		Optional<Method> method = getMethod(name, types);
		if (method.isPresent()) {
			method.get().execute();
		} else {
			throw new IllegalArgumentException("Method does not exist: " + name + "(" + String.join(", ", Arrays.stream(types).map(Type::getFullName).collect(Collectors.toList())) + ")");
		}
	}

	@Override
	public String toJavaCode() {
		StringBuilder sb = new StringBuilder();

		sb.append("package " + packageName + ";");

		sb.append("public class " + name + "{");
		for (Variable field : variables.values()) {
			sb.append("private " + Transpiler.getJavaType(field.getType()) + " " + field.getName() + ";");
		}

		for (Method method : methods) {
			sb.append(method.toJavaCode());
			//			sb.append("public " + method.getReturnType() + " " + method.getName());
		}
		sb.append("}");
		return sb.toString();
	}

}
