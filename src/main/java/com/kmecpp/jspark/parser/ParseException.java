package com.kmecpp.jspark.parser;

import com.kmecpp.jspark.parser.statement.block.module.Module;
import com.kmecpp.jspark.tokenizer.Tokenizer;
import com.kmecpp.jspark.util.StringUtil;

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 3989009620206922448L;

	private Module module;
	private Tokenizer tokenizer;
	private String message;

	public ParseException(Parser parser, String message) {
		this.module = parser.getModule();
		this.tokenizer = parser.getTokenizer();
		this.message = message;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " at (" + module.getFileName() + ":" + tokenizer.getLine() + "): " + message
				+ "\n\t" + (tokenizer.getLine() - 1) + ": " + tokenizer.getPreviousLine()
				+ "\n\t" + tokenizer.getLine() + ": " + tokenizer.getCurrentLine()
				+ "\n\t" + StringUtil.repeat('-', 3 + tokenizer.getColumn()) + "^"
				+ "\n\t";
	}

}
