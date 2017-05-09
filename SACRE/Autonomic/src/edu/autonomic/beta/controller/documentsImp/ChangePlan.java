package edu.autonomic.beta.controller.documentsImp;

import java.util.ArrayList;
import java.util.List;

import edu.autonomic.beta.controller.documents.IChangePlan;

/** 
* @author Edith Zavala
*/

public class ChangePlan implements IChangePlan {

	private List<String> actions;
	private String type;
	
	public ChangePlan() {
		this.actions = new ArrayList<String>();
		this.type = "ChangePlan";
	}
	
	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setChangePlan(List <String> actions) {
		this.actions = actions;
		
	}

	@Override
	public List <String> getChangePlan() {
		return this.actions;
	}

	public void clear(){
		this.actions.clear();
	}

}
