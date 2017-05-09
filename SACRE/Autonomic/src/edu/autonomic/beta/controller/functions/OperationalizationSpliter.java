package edu.autonomic.beta.controller.functions;

import java.util.HashMap;

/** 
* @author Edith Zavala
*/

public class OperationalizationSpliter {

	public static HashMap<String, String> split(String ope) {
		HashMap<String, String> output = new HashMap<String, String>();
		String varId = ope.substring(0, 4);
		String checkOpe = ope.substring(5, 6);
		String operator = "";
		String value = "";
		if (checkOpe.equals("=")) {
			operator = ope.substring(4, 6);
			value = ope.substring(6);
		} else {
			operator = ope.substring(4, 5);
			value = ope.substring(5);
		}

		output.put("varId", varId);
		output.put("operator", operator);
		output.put("value", value);
		return output;
	}
}
