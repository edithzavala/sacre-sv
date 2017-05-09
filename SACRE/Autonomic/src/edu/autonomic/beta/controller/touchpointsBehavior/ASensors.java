package edu.autonomic.beta.controller.touchpointsBehavior;

import edu.autonomic.beta.controller.documents.IContextData;
import edu.autonomic.beta.controller.mapek.IMonitor;
import edu.autonomic.beta.controller.touchpoints.ISensors;

/** 
* @author Edith Zavala
*/

public abstract class ASensors implements ISensors {

	public void setMonitor(IMonitor monitor) {
	}

	public abstract IContextData getSensedContext();

	public void startSensing() {
	}

}
