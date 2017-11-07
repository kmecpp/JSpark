package com.kmecpp.jspark.tokenizer;

public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = 827845429612644570L;

	private String message;

	public InvalidTokenException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
