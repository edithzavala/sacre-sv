package edu.autonomic.beta.controller.managedElements.sv;

/** 
* @author Edith Zavala
*/

public class CurveManager {

	public boolean analyze(SmartVehicle sm) {
		Double newAngle = null;
		Boolean hasChanged = new Boolean(false);

		if (sm.getLaneLeftCamera().getFrontalDistance() < 10
				|| sm.getLaneRightCamera().getFrontalDistance() < 10) {
			newAngle = new Double(5 * (Math.max(
					Math.abs(sm.getLaneLeftCamera().getDeviationAngle()),
					Math.abs(sm.getLaneRightCamera().getDeviationAngle()))));

			if (sm.getSpeed() > 15) {
				sm.brake(new Double(Math.max(3.0, sm.getSpeed() * 0.10)));
			} 

			hasChanged = new Boolean(true);
		} else if (sm.getSwheel().getAngle() != 0) {
			double delta = (Math.min(5, new Double(5 * (Math.max(
					Math.abs(sm.getLaneLeftCamera().getDeviationAngle()),
					Math.abs(sm.getLaneRightCamera().getDeviationAngle()))))));

			if (sm.getSwheel().getAngle() < 0) {
				newAngle = delta;
			} else if (sm.getSwheel().getAngle() > 0) {
				newAngle =- delta;
			}
			hasChanged = new Boolean(true);
		}

		if (hasChanged) {
			sm.getSwheel().setAngle(newAngle);
			return hasChanged.booleanValue();
		} else {
			return hasChanged.booleanValue();
		}
	}
}
