package edu.autonomic.beta.controller.functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/** 
* @author Edith Zavala
*/

public class AutonomicOuput {
	
	private static enum Mode {
		DISPLAY, FILE
	}

	public static enum Type {
		DEBUG, INFO, WARN, ERROR, FATAL
	}

	private static Mode m = Mode.DISPLAY;

	public static void print(Type type, String className, String message) {

//		/* print in file */
//		String line = message + ";" + System.currentTimeMillis();
//		OutputStream out = null;
//		FileConnection fconn = null;
//		try {
//			fconn = (FileConnection) Connector.open(
//					".../output.txt", Connector.READ_WRITE);
//			if (!fconn.exists())
//				fconn.create();
//
//			out = fconn.openOutputStream(Long.MAX_VALUE);
//			PrintStream ps = new PrintStream(out);
//			ps.println(line);
//			// out.write(line.getBytes());
//			// fconn.setHidden(false);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (fconn != null) {
//					fconn.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

		if (m == Mode.DISPLAY) {
			/* print in console */
			System.out.println(System.currentTimeMillis() + ":"
					+ type + ":" + className.substring(6) + ":" + message);
		}
	}
}
