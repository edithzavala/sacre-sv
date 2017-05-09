package edu.autonomic.beta.controller.mapekBehavior;

import edu.autonomic.beta.controller.documents.IContextData;
import edu.autonomic.beta.controller.documents.ISymptom;
import edu.autonomic.beta.controller.mapek.IAnalyzer;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.mapek.IMonitor;

/** 
* @author Edith Zavala
*/

public abstract class AMonitor implements IMonitor{

	protected ISymptom symptoms; 
	
	public void setKB(IKB kb){}	
	
	public void setAnalyzer(IAnalyzer analyzer){}
	
	public void feedSensedContext(IContextData context){}
	
	protected abstract void startMonitoring();
	
	protected void buildSymptoms(){}
	
	
	
	
	
	
}
