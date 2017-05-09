package edu.autonomic.beta.controller.managedElements.sv;

import java.util.ArrayList;

/** 
* @author Edith Zavala
*/

public class SmartVehicleUpdate {

	public static AlarmsManager alarmsM = new AlarmsManager();
	public static LaneManager laneM = new LaneManager();
	public static CurveManager curveM = new CurveManager();

	public static void updateState(SmartVehicle sm) {

		ArrayList<String> behOn = CtxReqVerificator.verify(sm.getOpe(),
		sm.getSrsVarsId(), sm.getSrsVarsValues());
		alarmsM.analyze(behOn, sm);

		if (sm.getLaneKeepingSupport()) {
			if (sm.getLaneLeftCamera().getDeviationAngle() != 0) {
				if (sm.getLaneLeftCamera().getDeviationAngle() == -1) {
					if (sm.getSpeed() > 0) {
						sm.brake(sm.getSpeed());
					} else if (sm.getSpeed() < 0) {
						sm.accelerate(sm.getSpeed() * (-1));
					} else {
						sm.brake(0.0);
						sm.accelerate(0.0);
					}
				} else {
					if (sm.getSpeed() > 20) {
						sm.brake(2.0);
					} else {
						sm.brake(0.0);
						sm.accelerate(0.0);
					}
					sm.getSwheel().setAngle(
					sm.getLaneLeftCamera().getDeviationAngle()* (-1));
				}
			} else {
				if (sm.getSpeed() > 30.0) {
					sm.brake(1.0);
				} else if (sm.getSpeed() < 30.0) {
					sm.accelerate(1.0);
				} else {
					sm.brake(0.0);
					sm.accelerate(0.0);
				}
				sm.getSwheel().setAngle(0.0);
			}
		}
	}
}
