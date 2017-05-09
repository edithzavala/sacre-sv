package edu.autonomic.beta.controller.smartVehicleBehavior;

/** 
* @author Edith Zavala
*/

public abstract class ASteeringWheel {
	protected double angle;
	
	public ASteeringWheel() {
		this.angle = 0;
	}
	
	public double getAngle(){
		return this.angle;
	}
	
	public void setAngle(Object newAngle){
		this.angle = Double.parseDouble(newAngle.toString());
	}
}
