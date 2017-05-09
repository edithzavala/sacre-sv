package edu.autonomic.beta.controller.smartVehicleComponents;

import edu.autonomic.beta.controller.smartVehicleBehavior.ASensor;

/** 
* @author Edith Zavala
*/

public class SensorSVSunVisorCamera extends ASensor {
	private int faceposition;
	private double eyesState;
	
	public void setFaceposition(Object val) {
		this.faceposition = Integer.parseInt(val.toString());
	}

	public void setEyesState(Object val) {
		this.eyesState = Double.parseDouble(val.toString());
	}

	@Override
	public Object getValue(String var) {
		if(var.equals("eyesState")){
			return new Double(this.eyesState);
		}else if(var.equals("facePosition")){
			return new Integer(this.faceposition);
		}
		return null;
	}
}
