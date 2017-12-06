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
		//				+ "\n\t" + (tokenizer.getLine() - 2) + ": " + tokenizer.getLineText(-2)
		//				+ "\n\t" + (tokenizer.getLine() - 1) + ": " + tokenizer.getLineText(-1)
		//				+ "\n\t" + tokenizer.getLine() + ": " + tokenizer.getLineText(0)
				+ tokenizer.getContext(3, true, "\n\t")
				+ "\n\t" + StringUtil.repeat('-', 6 + tokenizer.getColumn() - tokenizer.getCurrentToken().getText().length()) + "^"
				+ "\n\t";
	}

}
