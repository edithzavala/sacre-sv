package edu.autonomic.beta.controller.smartVehicleComponents;

import edu.autonomic.beta.controller.smartVehicleBehavior.ALED;

/** 
* @author Edith Zavala
*/

public class AlarmLED extends ALED{

	boolean enable;
	
	public AlarmLED() {
		this.frequency = 0;
		this.luminosity=0;
		this.state = false;
		this.enable = true;
	}
	
	public double getFrequency(){
		return this.frequency;
	}
	
	public double getLuminosity(){
		return this.luminosity;
	}
	
	public boolean getState(){
		return this.state;
	}
	
	public void setFrequency(Double freq){
		 this.frequency = freq.doubleValue();
	}
	
	public void setLuminosity(Double lum){
		 this.luminosity = lum.doubleValue();
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
