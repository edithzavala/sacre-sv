package edu.autonomic.beta.controller.documentsImp.svContext;

import java.util.HashMap;

import edu.autonomic.beta.controller.documents.IElementContext;

/** 
* @author Edith Zavala
*/

public class SensorsContext implements IElementContext{

	private String type;
	private HashMap<String, Object> sensors;

	public SensorsContext() {
		sensors = new HashMap<String, Object>();
		type = "SensorsContext";
	}

	@Override
	public String getType() {
		return this.type;
	}

	public HashMap<String, Object> getSensors() {
		return sensors;
	}

	public void setSensors(HashMap<String, Object> sensors) {
		this.sensors = sensors;
	}
}
