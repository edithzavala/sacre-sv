package edu.autonomic.beta.controller.mapekImp.planner;

import java.util.HashMap;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import edu.autonomic.beta.controller.documentsImp.policies.PlannerPolicy;

/** 
* @author Edith Zavala
*/

public class PoliciesReviser {

	private HashMap<String, Double> varsTresholdsMax;
	private HashMap<String, Double> varsTresholdsMin;
	private String[] vars;

	public PoliciesReviser(PlannerPolicy pp) {
		this.vars = pp.getVars();
		this.varsTresholdsMax = pp.getVarsTresholdsMax();
		this.varsTresholdsMin = pp.getVarsTresholdsMin();
	}

	public boolean revise(String data) throws JSONException {
		boolean approved = true;
		JSONObject json = new JSONObject(data);
		if (json.isNull("REMOVE")) {
			for (int i = 0; i < vars.length; i++) {
				double value = Double
						.parseDouble((json.get(vars[i]).toString()));
				double valueMax = varsTresholdsMax.get(vars[i]).doubleValue();
				double valueMin = varsTresholdsMin.get(vars[i]).doubleValue();
				if (value > valueMax || value < valueMin) {
					approved = false;
				}
			}
		}
		return approved;
	}

	public String[] getActions(String data) throws JSONException {
		JSONObject json = new JSONObject(data);
		String[] out;
		if (json.isNull("REMOVE")) {
			JSONArray ja = (JSONArray) json.get("Actions");
			out = new String[ja.length()];
			for (int i = 0; i < out.length; i++) {
				out[i] = json.getString("crId") + " " + ja.get(i).toString();
			}
		} else {
			out = new String[1];
			out[0] = "REMOVE "+json.getString("REMOVE");
		}
		return out;
	}
}
