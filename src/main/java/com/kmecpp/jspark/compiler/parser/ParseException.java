package com.kmecpp.jspark.compiler.parser;

import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;
import com.kmecpp.jspark.compiler.tokenizer.Tokenizer;
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
		try {
			return getClass().getSimpleName() + " at (" + module.getFileName() + ":" + tokenizer.getLine() + "): " + message
					+ tokenizer.getContext(3, true, "\n\t")
					+ "\n\t" + StringUtil.repeat('-', getErrorStart()) + "^"
					+ "\n\t";
		} catch (Exception e) {
			System.err.println("Error in exception handler!");
			e.printStackTrace();
			throw new Error(e);
		}

	}

	private int getErrorStart() {
		String line = tokenizer.getCurrentLine();
		return 3
				+ (line.startsWith("\t") ? -4 : 0)
				+ line.substring(0, tokenizer.getColumn()).replace("\t", "        ").length()
				- tokenizer.getCurrentToken().getText().length();
	}

}
