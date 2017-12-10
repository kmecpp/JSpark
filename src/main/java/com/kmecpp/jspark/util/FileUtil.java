package com.kmecpp.jspark.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileUtil {

	private static final int BUFFER_SIZE = 8192;

	/**
	 * Recursively gets all the files in the given directory and in all of its
	 * sub directories. If the given directory is a file, this method will
	 * return an ArrayList containing solely the path to that file
	 * 
	 * @param file
	 *            the file or directory to search
	 * @return an ArrayList of all the files in the given directory
	 */
	public static ArrayList<File> getFiles(File file) {
		return getFiles(file, new ArrayList<>());
	}

	private static ArrayList<File> getFiles(File file, ArrayList<File> files) {
		if (file.isFile()) {
			files.add(file);
		} else {
			for (File f : file.listFiles()) {
				getFiles(f, files);
			}
		}
		return files;
	}

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
		char[] buffer = new char[BUFFER_SIZE];
		int pos = 0;
		while ((pos = reader.read(buffer)) != -1) {
			sw.write(buffer, 0, pos);
		}
		return sw.toString();
	}

}
