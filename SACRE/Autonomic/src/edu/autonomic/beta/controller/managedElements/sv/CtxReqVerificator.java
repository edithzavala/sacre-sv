package edu.autonomic.beta.controller.managedElements.sv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.autonomic.beta.controller.functions.Comparator;
import edu.autonomic.beta.controller.functions.OperationalizationSpliter;
import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class CtxReqVerificator {

	public static ArrayList<String> verify(HashMap<String, Object> ope,
			HashMap<String, String> srsVarsId,
			HashMap<String, Object> srsVarsValues) {

		ArrayList<String> output = new ArrayList<String>();
		Iterator<Map.Entry<String, Object>> it = ope.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();

			String[] operationalization = Spliter.split(
					(String) pair.getValue(), ",");

			String[] varsValues = Spliter.split(operationalization[3], " ");
			String behId = operationalization[1];

			boolean isOn = checkOpe(varsValues, srsVarsId, srsVarsValues);

			if (isOn) {
				output.add(behId);
			}
		}

		return output;
	}

	private static boolean checkOpe(String[] varsValues,
			HashMap<String, String> srsVarsId,
			HashMap<String, Object> srsVarsValues) {
		boolean isOn = true;
		for (int j = 0; j < varsValues.length; j++) {
			HashMap<String,String> info=  OperationalizationSpliter.split(varsValues[j]);
			Double varCurrentVal = new Double(Double.parseDouble((srsVarsValues
					.get(srsVarsId.get(info.get("varId")))).toString()));
			/* All relations are ANDs */
			isOn = isOn
					&& (Comparator.compare(varCurrentVal,
							new Double(Double.parseDouble(info.get("value"))), info.get("operator")));
		}
		return isOn;
	}
}
