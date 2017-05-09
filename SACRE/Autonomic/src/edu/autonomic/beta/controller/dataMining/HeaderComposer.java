package edu.autonomic.beta.controller.dataMining;

import java.util.HashMap;
import java.util.List;

import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

//Ad-hoc solution (Weka)
public class HeaderComposer {

	private String header;
	HashMap<String, String> completeH;
	private int attr;

	public HeaderComposer(HashMap<String, String> header) {
		this.completeH = header;
		this.header = "@relation " + header.get("relation") + "\n\n";
		String[] attr = Spliter.split(header.get("attribute"), ",");
		this.attr = attr.length;
		for (int i = 0; i < attr.length; i++) {
			this.header = this.header + "@attribute " + attr[i] + " "
					+ header.get(attr[i]) + "\n";
		}
		this.header = this.header + "\n@data";
	}

	public void modify(List<String> variables) {
		if (variables.size() != attr) {
			this.header = "";
			this.header = "@relation " + completeH.get("relation") + "\n\n";
			String[] attr = Spliter.split(completeH.get("attribute"), ",");
			this.attr = attr.length;
			for (int i = 0; i < attr.length; i++) {
				if (variables.contains(attr[i]) || i==4) { //4=IAFactorPosition
					this.header = this.header + "@attribute " + attr[i] + " "
							+ completeH.get(attr[i]) + "\n";
				}
			}
			this.header = this.header + "\n@data";
		}
	}
	
	public String getHeader() {
		return this.header;
	}

}
