package edu.autonomic.beta.controller.mapekImp.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class RulesProcessor {

	public static String[] process(String[] rules) {
		/*Ad-hoc solution for Weka response*/
		ArrayList<String> aux = new ArrayList<String>();
		String[] actions = null;
		HashMap<String, String> hm = new HashMap<String, String>();
		boolean applyAction = false;

		for (int i = 0; i < rules.length; i++) {
			String[] thenIf = Spliter.split(rules[i], ">");
			String then = thenIf[1];
			String condition = "";
			if (!thenIf[0].equals(" =")) {
				condition = thenIf[0].substring(0, (thenIf[0].length() - 2));
				if (condition.contains("and")) {
					String[] conditions = Spliter.split(condition, ")");
					for (int j = 0; j < conditions.length; j++) {
						String[] assign = Spliter.split(conditions[j], "=");
						String variable = assign[0].substring(
								assign[0].indexOf('(') + 1,
								assign[0].length() - 1);
						String value = assign[1].substring(1);
						hm.put(variable, value);
					}
				} else {
					String[] assign = Spliter.split(condition, "=");
					String variable = assign[0].substring(1, assign[0].length() - 1);
					String value = assign[1].substring(1, assign[1].length()-1);
					hm.put(variable, value);
				}
				String[] iaFacAndVal = Spliter.split(then, "=");
				String valueBoolean = iaFacAndVal[1];
				applyAction = valueBoolean.equals("0") ? false : true;

				if (applyAction) {
					Iterator<Map.Entry<String, String>> it = hm.entrySet()
							.iterator();
					while (it.hasNext()) {
						Map.Entry<String, String> pair = (Map.Entry<String, String>) it
								.next();
						String valueToact = "";
						switch (((String) pair.getValue()).charAt(0)) {
						case 'l':
							valueToact = "<"
									+ ((String) pair.getValue()).substring(1);
							break;
						case 'g':
							valueToact = ">"
									+ ((String) pair.getValue()).substring(1);
							break;
						default:
							valueToact = ((String) pair.getValue());
						}
						aux.add("UPDATE " + pair.getKey() + " " + valueToact);
					}
				} else {
					aux.add("NO_ACTIONS");
				}
			} else {
				aux.add("NO_ACTIONS");
			}
		}
		actions = new String[aux.size()];
		for (int i = 0; i < actions.length; i++) {
			actions[i] = aux.get(i);
		}
		return actions;
	}
}
