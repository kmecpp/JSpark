package com.kmecpp.jspark.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import com.kmecpp.jspark.parser.statements.block.module.Module;
import com.kmecpp.jspark.parser.statements.block.module.Static;

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

	public void start() {
		if (main == null) {
			throw new IllegalStateException("No main method defined!");
		}
		main.getMethod("main").get().execute();
	}

}
