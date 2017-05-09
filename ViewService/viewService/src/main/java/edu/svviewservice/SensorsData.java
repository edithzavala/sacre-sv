package edu.svviewservice;

import org.json.JSONObject;

/** 
* @author Edith Zavala
*/

public class SensorsData {
	
	private static JSONObject sensorsController = new JSONObject();
	private static JSONObject sensorsView = new JSONObject();

	
	public static JSONObject getSensorsController() {
		JSONObject copy;
		synchronized (SensorsData.sensorsController) {
			copy = new JSONObject(SensorsData.sensorsController.toString());	
		}
		return copy;
	}

	public static JSONObject getSensorsView() {
		JSONObject copy;
		synchronized (SensorsData.sensorsView) {
			copy = new JSONObject(SensorsData.sensorsView.toString());	
		}
		return copy;
	}
	
	public static void setSensors(JSONObject sensors) {
		String client = sensors.getString("client");
		sensors.remove("client");
		if(client.equals("controller")){
			SensorsData.sensorsController = sensors;	
		}else{
			SensorsData.sensorsView = sensors;	
		}	
	}
}
