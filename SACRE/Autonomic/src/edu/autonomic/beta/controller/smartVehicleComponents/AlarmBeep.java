package edu.autonomic.beta.controller.smartVehicleComponents;

import edu.autonomic.beta.controller.smartVehicleBehavior.ABeep;

/** 
* @author Edith Zavala
*/

public class AlarmBeep extends ABeep {

	boolean enable;
	
	public AlarmBeep() {
		this.state = false;
		this.enable = true;
	}

	public boolean getState() {
		return this.state;
	}

	public void setState(Object state){
		this.state = Boolean.parseBoolean(state.toString());
	}
	
	public void setEnable(Object enable){
		this.enable = Boolean.parseBoolean(enable.toString());
	}
	
	public boolean isEnable(){
		return this.enable;
	}
}
