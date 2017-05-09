package edu.autonomic.beta.controller.smartVehicleBehavior;

import edu.autonomic.beta.controller.managedElements.IManagedElement;

/** 
* @author Edith Zavala
*/

public abstract class ASmartVehicle implements IManagedElement {

	protected double speed;
	protected double accelerationRate;
	protected int numberOfWheels;

	public abstract void accelerate(Double accelerationRate);

	public abstract void brake(Double decelerationRate);
}
