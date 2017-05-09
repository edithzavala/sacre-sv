package edu.autonomic.beta.controller.mapekBehavior;

import edu.autonomic.beta.controller.documents.IRequestForChange;
import edu.autonomic.beta.controller.documents.ISymptom;
import edu.autonomic.beta.controller.mapek.IAnalyzer;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.mapek.IPlanner;

/** 
* @author Edith Zavala
*/

public abstract class AAnalyzer implements IAnalyzer {

	protected IRequestForChange reqForChange;

	public void setKB(IKB kb) {
	}

	public void setPlanner(IPlanner planner) {
	}

	public void analyzeSymptoms(ISymptom symptoms) {
	}

	protected abstract void startAnalyzing(ISymptom symptom);

	protected void buildRequestForChange() {
	}
}
