package edu.autonomic.beta.controller.mapekBehavior;

import edu.autonomic.beta.controller.documents.IChangePlan;
import edu.autonomic.beta.controller.mapek.IExecuter;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.touchpoints.IEffectors;

/** 
* @author Edith Zavala
*/

public abstract class AExecuter implements IExecuter{

	public void setEffector(IEffectors effector){}
	
	public void setKB(IKB kb){}	
	
	public void executeChangePlan(IChangePlan changePlan){}
	
	protected abstract void startExecuting(IChangePlan changePlan);
}
