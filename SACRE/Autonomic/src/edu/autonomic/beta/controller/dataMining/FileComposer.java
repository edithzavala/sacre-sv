package edu.autonomic.beta.controller.dataMining;

/** 
* @author Edith Zavala
*/

public class FileComposer {

	private String record;
	private String[] attr;
	private String[] values;

	public FileComposer(String[] attr) {
		this.attr = attr;
		this.values = new String[attr.length];
		this.record = "";
	}

	public void composeRecord(String attrName, String value) {
		for (int i = 0; i < attr.length; i++) {
			if (attr[i].equals(attrName)) {
				values[i] = value;
			}
		}
	}

	private void fillRecord() {
			this.record = values[0];
			for (int i = 1; i < values.length; i++) {
				if (values[i] != null) {
					this.record = this.record + "," + values[i];
				}
			}
	}

	public String getRecord() {
		fillRecord();
		return ("\n" + this.record);
	}

	public void clean() {
		this.record = "";
		for (int i = 0; i < values.length; i++) {
				values[i] = null;
		}
	}
}
