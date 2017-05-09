package edu.autonomic.beta.controller.touchpoints;

import edu.autonomic.beta.controller.documents.IContextData;

/** 
* @author Edith Zavala
*/

public interface ISensors {
	public IContextData getSensedContext();
	public void startSensing();
}
