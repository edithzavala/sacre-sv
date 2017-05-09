package edu.autonomic.beta.controller.smartVehicleComponents;

import edu.autonomic.beta.controller.smartVehicleBehavior.ASensor;

/** 
* @author Edith Zavala
*/

public class SensorSVLaneCamera extends ASensor {
	
	private double lateralDistance;
	private double frontalDistance;
	private double deviationAngle;

	public double getLateralDistance() {
		return lateralDistance;
	}

	public void setLateralDistance(Object lateralDistanceRight) {
		this.lateralDistance = Double.parseDouble(lateralDistanceRight.toString());
	}

	public double getFrontalDistance() {
		return frontalDistance;
	}

	public void setFrontalDistance(Object frontalDistance) {
		this.frontalDistance =  Double.parseDouble(frontalDistance.toString());
	}

	public double getDeviationAngle() {
		return deviationAngle;
	}

	public void setDeviationAngle(Object deviationAngle) {
		this.deviationAngle = Double.parseDouble(deviationAngle.toString());
	}

	@Override
	public Object getValue(String var) {
		switch (var) {
			case "lateralDistance":
				return new Double(this.lateralDistance);
			case "frontalDistance":
				return new Double(this.frontalDistance);
			case "deviationAngle":
				return new Double(this.deviationAngle);
		}
		return null;
	}
}
