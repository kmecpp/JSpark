package com.kmecpp.jspark.parser;

import java.util.ArrayList;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.parser.statements.modules.Class;
import com.kmecpp.jspark.parser.statements.modules.Method;
import com.kmecpp.jspark.parser.statements.modules.Module;
import com.kmecpp.jspark.parser.statements.modules.Static;
import com.kmecpp.jspark.tokenizer.Token;
import com.kmecpp.jspark.tokenizer.TokenType;
import com.kmecpp.jspark.tokenizer.Tokenizer;

public class Parser {

	private Tokenizer tokenizer;
	private Module module;

	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;

		Token keyword = tokenizer.read(TokenType.KEYWORD);
		module = keyword.is(Keyword.STATIC) ? new Static(tokenizer.readName())
				: keyword.is(Keyword.CLASS) ? new Class(tokenizer.readName()) : null;
	}

	public Module parse() {
		while (tokenizer.hasNext()) {
			Token token = tokenizer.getNext();
			System.out.println("Parsing token: " + token);

			//IDENTIFIERS
			if (token.getType() == TokenType.IDENTIFIER) {

			}

			//KEYWORDS
			else if (token.getType() == TokenType.KEYWORD) {
				if (token.is(Keyword.PUBLIC)) {
					module.addStatement(new Method(tokenizer.readName(), new ArrayList<>()));
				}

				if (token.is(Keyword.CLASS)) {
					module.addStatement(new Class(tokenizer.readName()));
					parse();
				} else if (token.is(Keyword.STATIC)) {
					module.addStatement(new Static(tokenizer.readName()));
					parse();
				}
			}

			//SYMBOLS
			else if (token.getType() == TokenType.SYMBOL) {

			}

			//OPERATORS
			else if (token.getType() == TokenType.OPERATOR) {

			}

			//LITERALS
			else if (token.getType() == TokenType.LITERAL) {

			}

			else {
				System.err.println("Could not parse unknown " + (token == null ? "token: null" : token.getType() + " '" + token.getText() + "'"));
			}
		}

		return module;
	}

}
