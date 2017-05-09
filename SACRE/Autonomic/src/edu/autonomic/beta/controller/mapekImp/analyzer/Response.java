package edu.autonomic.beta.controller.mapekImp.analyzer;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/** 
* @author Edith Zavala
*/

public class Response {

	private String[] responseVars;

	public Response(String[] responseVars) {
		this.responseVars = responseVars;
	}

	public String processResponse(String response) throws JSONException {
		/* Split rules and transform to actions */
		JSONObject json = new JSONObject(response);
		JSONObject output = new JSONObject();
		if(!json.isNull("REMOVE")){
			String variable = json.getString("REMOVE");
			output.put("REMOVE",variable);
			return output.toString();
		}
		output.put("crId", json.get("crId"));
		JSONObject result = new JSONObject(json.getString("result"));
		for (int i = 0; i < this.responseVars.length; i++) {
			String var = this.responseVars[i];
			if (var.equals("Rules")) {
				JSONArray ja = (JSONArray) result.get(var);
				String[] input = new String[ja.length()];
				for (int j = 0; j < input.length; j++) {
					input[j] = ja.get(j).toString();
				}
				String[] actionsToDo = RulesProcessor.process(input);
				JSONArray jaOut = new JSONArray();
				for(int k = 0; k<actionsToDo.length;k++){
					jaOut.put(actionsToDo[k]);
				}
				output.put("Actions", jaOut);
			} else {
				output.put(var, result.get(var));
			}
		}
		return output.toString();
	}
}
