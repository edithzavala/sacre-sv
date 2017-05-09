package edu.autonomic.beta.controller.smartVehicleComponents;

import edu.autonomic.beta.controller.smartVehicleBehavior.ASensor;

/** 
* @author Edith Zavala
*/

public class SensorSVECG extends ASensor {

	private int hbpm;

	public Object getValue(String var) {
		if (var.equals("heartBeatsPerMinute"))
			return new Integer(hbpm);
		return null;
	}
	
	public void setHbpm(Object val){
		this.hbpm = Integer.parseInt(val.toString());
	}
}
