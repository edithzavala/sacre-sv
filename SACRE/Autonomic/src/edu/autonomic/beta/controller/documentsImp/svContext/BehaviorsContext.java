package edu.autonomic.beta.controller.documentsImp.svContext;

import java.util.HashMap;

import edu.autonomic.beta.controller.documents.IElementContext;

/** 
* @author Edith Zavala
*/

public class BehaviorsContext implements IElementContext{
	
	private String type;
	private HashMap<String, Object> behaviors;
	
	public BehaviorsContext() {
		behaviors = new HashMap<String, Object>();
		type="BehaviorsContext";
	}
	
	@Override
	public String getType(){
		return this.type;
	}

	public HashMap<String, Object> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(HashMap<String, Object> behaviors) {
		this.behaviors = behaviors;
	}
	
}
