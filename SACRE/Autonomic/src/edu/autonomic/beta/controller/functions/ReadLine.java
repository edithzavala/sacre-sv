package edu.autonomic.beta.controller.functions;

import java.io.IOException;
import java.io.InputStreamReader;

public class ReadLine {
	/*
	 * From: http://developer.nokia.com/community/wiki/
	 * Reading_a_text_file_line_by_line_in_Java_ME
	 */
	public static String read(InputStreamReader reader) throws IOException {
		// Test whether the end of file has been reached. If so, return null.
		int readChar = reader.read();
		if (readChar == -1) {
			return null;
		}
		StringBuffer string = new StringBuffer("");
		// Read until end of file or new line
		while (readChar != -1 && readChar != '\n') {
			// Append the read character to the string. Some operating systems
			// such as Microsoft Windows prepend newline character ('\n') with
			// carriage return ('\r'). This is part of the newline character
			// and therefore an exception that should not be appended to the
			// string.
			if (readChar != '\r') {
				string.append((char) readChar);
			}
			// Read the next character
			readChar = reader.read();
		}
		return string.toString();
	}
}
