package edu.autonomic.beta.controller.documentsImp.sensorsData;

import java.util.HashMap;

import edu.autonomic.beta.controller.documents.IContext;
import edu.autonomic.beta.controller.documents.IElementContext;
import edu.autonomic.beta.controller.documentsImp.svContext.BehaviorsContext;

/** 
* @author Edith Zavala
*/

public class BehaviorData implements IContext {

	private String type;
	private HashMap<String, Object> hm;
	
	public BehaviorData(){
		hm = new HashMap<String, Object>();
		type = "BehaviorData";
	}
	
	public String getType(){
		return this.type;
	}

	@Override
	public void setContext(IElementContext currentValues) {
		hm = ((BehaviorsContext)currentValues).getBehaviors();
		
	}

	@Override
	public HashMap<String, Object> getContext() {
		return this.hm;
	}
}
