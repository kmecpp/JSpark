package com.kmecpp.jspark.runtime;

import java.util.HashMap;

import com.kmecpp.jspark.compiler.Compiler;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;

public class Interpreter {

	private Compiler compiler;
	private HashMap<String, Module> modules;

	public Interpreter(Compiler compiler) {
		this.compiler = compiler;
		this.modules = compiler.getModules();
	}

	public Module getModule(String fullName) {
		return modules.get(fullName);
	}

	public void startProgram() {
		compiler.getMain().execute();
	}

	//	private HashMap<String, Module> modules;
	//	private Static main;
	//
	//	public Runtime(ArrayList<Module> modules) {
	//		this.modules = new HashMap<>();
	//
	//		for (Module module : modules) {
	//			if (module instanceof Static && module.hasMethod("main")) {
	//				if (main == null) {
	//					main = (Static) module;
	//				} else {
	//					throw new IllegalStateException("Main method already defined in '" + main.getName() + "'");
	//				}
	//			}
	//			this.modules.put(module.getName(), module);
	//		}
	//	}
	//
	//	public Module getModule(String name) {
	//		return modules.get(name);
	//	}
	//
	//	public void start() {
	//		if (main == null) {
	//			throw new IllegalStateException("No main method defined!");
	//		}
	//
	//		main.getMethod("main").get().invoke();
	//	}

}
