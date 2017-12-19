package com.kmecpp.jspark.compiler;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

import com.kmecpp.jspark.compiler.parser.Parser;
import com.kmecpp.jspark.compiler.parser.statement.block.Method;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Static;

public class Compiler {

	private HashMap<String, Module> modules = new HashMap<>();;
	private Method main;

	public Method getMain() {
		return main;
	}

	public HashMap<String, Module> getModules() {
		return modules;
	}

	public void parseModule(Path path) {
		long s = System.nanoTime();
		Module module = new Parser(path).parseModule();
		Optional<Method> main = module.getMethod("main");
		if (module instanceof Static && main.isPresent()) {
			if (main.isPresent()) {
				this.main = main.get();
			} else {
				throw new IllegalStateException("Main method already defined in '" + this.main.getModule().getName() + "'");
			}
		}
		System.out.println("Parsed Module: " + module.getFullName() + " (" + (System.nanoTime() - s) / 1000000F + "ms)");
		modules.put(module.getFullName(), module);
	}

}
