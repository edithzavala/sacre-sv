package edu.autonomic.beta.controller.documentsImp;

import java.util.ArrayList;
import java.util.List;

import edu.autonomic.beta.controller.documents.IRequestForChange;

/** 
* @author Edith Zavala
*/

public class RequestForChange implements IRequestForChange {

	private List<String> actions;
	private String type;
	
	public RequestForChange() {
		this.actions = new ArrayList<String>();
		this.type = "RequestForChange";
	}
	
	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setReqForChange(List<String> actions) {
		this.actions = actions;
		
	}

	@Override
	public List<String> getReqForChange() {
		return this.actions;
	}
	
	public void clear(){
		this.actions.clear();
	}



}
