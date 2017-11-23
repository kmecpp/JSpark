package com.kmecpp.jspark.runtime;

import java.util.HashMap;

import com.kmecpp.jspark.parser.statements.block.module.Module;
import com.kmecpp.jspark.parser.statements.block.module.Static;

public class Runtime {

	private HashMap<String, Module> modules = new HashMap<>();
	private Static main;
	
	
	public Runtime() {
		// TODO Auto-generated constructor stub
	}

	public void start() {
		main.getMethod("main").get().execute();
	}

}
