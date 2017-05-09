package edu.autonomic.beta.controller.touchpointsBehavior;

import edu.autonomic.beta.controller.managedElements.IManagedElement;
import edu.autonomic.beta.controller.touchpoints.IEffectors;

/** 
* @author Edith Zavala
*/

public abstract class AEffectors implements IEffectors {

	public void effectAction(String[] commands) {
	}

	protected abstract void startEffecting(String[] commands);

	public void setManagedElement(IManagedElement me) {
	}
}
