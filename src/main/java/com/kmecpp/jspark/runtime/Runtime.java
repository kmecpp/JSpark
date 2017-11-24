package com.kmecpp.jspark.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import com.kmecpp.jspark.parser.statement.block.module.Module;
import com.kmecpp.jspark.parser.statement.block.module.Static;

public class Runtime {

	private HashMap<String, Module> modules;
	private Static main;

	public Runtime(ArrayList<Module> modules) {
		this.modules = new HashMap<>();

		for (Module module : modules) {
			if (module instanceof Static && module.hasMethod("main")) {
				if (main == null) {
					main = (Static) module;
				} else {
					throw new IllegalStateException("Main method already defined in '" + main.getName() + "'");
				}
			}
			this.modules.put(module.getName(), module);
		}
	}

	public Module getModule(String name) {
		return modules.get(name);
	}

	public void start() {
		if (main == null) {
			throw new IllegalStateException("No main method defined!");
		}

		System.out.println();
		System.out.println("Running program...");
		main.getMethod("main").get().execute();
	}

}
