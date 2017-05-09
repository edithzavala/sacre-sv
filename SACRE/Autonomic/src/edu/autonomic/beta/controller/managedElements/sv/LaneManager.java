package edu.autonomic.beta.controller.managedElements.sv;

/** 
* @author Edith Zavala
*/

public class LaneManager {

	public boolean analyze(SmartVehicle sm) {
		if (sm.getLaneLeftCamera().getLateralDistance() < 0.20
				|| sm.getLaneRightCamera().getLateralDistance() < 0.20) {
			sm.brake(new Double(1.0));
			sm.getSwheel().setAngle(
					new Double(5 * (Math.max(Math.abs(sm.getLaneLeftCamera()
							.getDeviationAngle()), Math.abs(sm
							.getLaneRightCamera().getDeviationAngle())))));
			return true;
		} else {
			return false;
		}
	}
}
