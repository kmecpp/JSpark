package com.kmecpp.jspark.parser;

import java.util.ArrayList;

import com.kmecpp.jspark.language.Keyword;
import com.kmecpp.jspark.parser.statements.modules.Class;
import com.kmecpp.jspark.parser.statements.modules.Module;
import com.kmecpp.jspark.tokenizer.Token;
import com.kmecpp.jspark.tokenizer.TokenType;
import com.kmecpp.jspark.tokenizer.Tokenizer;

public class Parser {

	private Tokenizer tokenizer;
	private Module module;

	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	public ArrayList<Statement> parse() {
		ArrayList<Statement> statements = new ArrayList<>();

		while (tokenizer.hasNext()) {
			Token token = tokenizer.getNext();

			//IDENTIFIERS
			if (token.getType() == TokenType.IDENTIFIER) {

			}

			//KEYWORDS
			else if (token.getType() == TokenType.KEYWORD) {
				if (token.is(Keyword.CLASS)) {
					Token name = tokenizer.read(TokenType.IDENTIFIER);
					if (module == null) {
						module = new Class(name);
					} else {
						module.addStatement();
					}
					module = module != null ? module : new Class(name);
					parse();
				} else if (token.is(Keyword.STATIC)) {

				}
			}

			//OPERATORS
			else if (token.getType() == TokenType.OPERATOR) {

			}

			//LITERALS
			else if (token.getType() == TokenType.LITERAL) {

			}
		}

		return statements;
	}

}
