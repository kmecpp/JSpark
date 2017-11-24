package com.kmecpp.jspark.language;

public interface AbstractToken {

	public String getString();

	public default boolean is(String text) {
		return getString().equals(text);
	}

}
