package edu.autonomic.beta.controller.documentsImp.sensorsData;

import java.util.HashMap;

import edu.autonomic.beta.controller.documents.IContext;
import edu.autonomic.beta.controller.documents.IElementContext;
import edu.autonomic.beta.controller.documentsImp.ctxReqOpeCtx.OperationalizationsContext;

/** 
* @author Edith Zavala
*/

public class RequirementsData implements IContext {

	private HashMap<String, Object> hm;
	private String type;
	
	public RequirementsData(){
		hm = new HashMap<String, Object>();
		type = "RequirementsData";
	} 
	
	public String getType(){
		return this.type;
	}
	

	@Override
	public void setContext(IElementContext currentValues) {
		hm = ((OperationalizationsContext)currentValues).getOperationalizations();
	}

	@Override
	public HashMap<String, Object> getContext() {
		return this.hm;
	}
}
