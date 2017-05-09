package edu.autonomic.beta.controller.smartVehicleComponents;

import edu.autonomic.beta.controller.smartVehicleBehavior.ASeat;

/** 
* @author Edith Zavala
*/

public class DriverSeat extends ASeat{
	boolean vibrating;
	boolean enable;
	
	public DriverSeat() {
		this.vibrating = false;
		this.enable = true;
	}
	
	public boolean isVibrating(){
		return this.vibrating;
	}
	
	public void setState(Object state){
		this.vibrating = Boolean.parseBoolean(state.toString());
	}
	
	public void setEnable(Object enable){
		this.enable = Boolean.parseBoolean(enable.toString());
	}
	
	public boolean isEnable(){
		return this.enable;
	}
}
