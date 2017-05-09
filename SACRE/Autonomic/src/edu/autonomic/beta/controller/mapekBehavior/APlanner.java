package edu.autonomic.beta.controller.mapekBehavior;

import java.util.ArrayList;

import edu.autonomic.beta.controller.documents.IChangePlan;
import edu.autonomic.beta.controller.documents.IRequestForChange;
import edu.autonomic.beta.controller.mapek.IExecuter;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.mapek.IPlanner;

/** 
* @author Edith Zavala
*/

public abstract class APlanner implements IPlanner {

	protected IChangePlan changePlan;

	public void setKB(IKB kb) {
	}

	public void setExecuter(IExecuter executer) {
	}

	public void buildChangePlan(ArrayList<String> actions) {
	}

	protected abstract void startPlanning(IRequestForChange reqForChange);
}
