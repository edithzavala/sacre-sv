package edu.autonomic.beta.controller.documentsImp.policies;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** 
* @author Edith Zavala
*/

public class KBPolicy {

	private long interReadingTime;
	private int minSymptoms;

	public KBPolicy(String fileName) {
		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.interReadingTime = Long.parseLong(prop
				.getProperty("interReadingTime"));
		this.minSymptoms =Integer.parseInt(prop.getProperty("minSymptoms"));
	}

	public long getInterReadingTime() {
		return this.interReadingTime;
	}

	public int getMinSymptoms() {
		return this.minSymptoms;
	}
}
