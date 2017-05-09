package edu.autonomic.beta.controller.managedElements.sv;

import java.util.ArrayList;

/** 
* @author Edith Zavala
*/

public class AlarmsManager {

	public void analyze(ArrayList<String> behOn, SmartVehicle sm) {
		if (!behOn.isEmpty()) {
			for (int i = 0; i < behOn.size(); i++) {
				for (int j = 0; j < sm.getBeh().size(); j++) {
					if (sm.getBeh().get(j).getBehaviorId().equals(behOn.get(i))) {
						switch (sm.getBeh().get(j).getBehaviorDescription()) {
						case "Activate Seat Vibration":
							if (!sm.getAlarmBeep().getState()
									&& !sm.getAlarmLED().getState()
									&& !sm.getLaneKeepingSupport()
									&& sm.getdSeat().isEnable()) {
								sm.getdSeat().setState(new Boolean(true));
							}
							break;
						case "Activate Sound/Light Alert":
							if (!sm.getLaneKeepingSupport()
									&& sm.getAlarmLED().isEnable()
									&& sm.getAlarmBeep().isEnable()) {
								sm.getdSeat().setState(new Boolean(false));
								sm.getAlarmLED().setState(new Boolean(true));
								sm.getAlarmBeep().setState(new Boolean(true));
							}
							break;
						case "Support Lane Keeping":
							if (sm.isEnableSLK()) {
								sm.getAlarmLED().setState(new Boolean(false));
								sm.getAlarmBeep().setState(new Boolean(false));
								sm.getdSeat().setState(new Boolean(false));
								sm.setLaneKeepingSupport(new Boolean(true));
							}
							break;
						}
					}
				}
			}
		} else {
			sm.getAlarmLED().setState(new Boolean(false));
			sm.getAlarmBeep().setState(new Boolean(false));
			sm.getdSeat().setState(new Boolean(false));
		}
	}
}
