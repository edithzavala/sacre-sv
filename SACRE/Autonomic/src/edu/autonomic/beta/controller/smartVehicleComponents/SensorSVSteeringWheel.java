package edu.autonomic.beta.controller.smartVehicleComponents;

import edu.autonomic.beta.controller.smartVehicleBehavior.ASensor;

/** 
* @author Edith Zavala
*/

public class SensorSVSteeringWheel extends ASensor {

	private int handsOn;
	
	public void setHandsOn(Object val) {
		this.handsOn = Integer.parseInt(val.toString());
	}

	@Override
	public Object getValue(String var) {
		if(var.equals("handsOnSteeringWheel")){
			return new Integer(this.handsOn);
		}
		return null;
	}
}
