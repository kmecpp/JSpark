package com.kmecpp.jspark.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Path;

public class FileUtil {

	public static String readFile(Path path) {
		try {
			return readStream(path.toUri().toURL().openStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * High performance read from an {@link InputStream} into a String
	 * 
	 * @param inputStream
	 *            the input stream from which to read
	 * @return the string read from the reader
	 * @throws IOException
	 *             if an IOException occurs
	 */
	private static String readStream(InputStream inputStream) throws IOException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		StringWriter sw = new StringWriter();
		char[] buffer = new char[4096];
		int pos = 0;
		while ((pos = reader.read(buffer)) != -1) {
			sw.write(buffer, 0, pos);
		}
		return sw.toString();
	}

}
