package edu.autonomic.beta.controller.documentsImp.ctxReqOpeCtx;

import java.util.HashMap;

import edu.autonomic.beta.controller.documents.IElementContext;

/** 
* @author Edith Zavala
*/

public class OperationalizationsContext implements IElementContext{
	private String type;
	private HashMap<String, Object> operationalizations;

	public OperationalizationsContext() {
		this.operationalizations = new HashMap<String, Object>();
		this.type="Operationalizations";
	}

	public void updateOperationalization(String key, Object value) {
		this.operationalizations.put(key,value);
	}
	
	public void setOperationalizations(HashMap<String, Object> hm) {
		this.operationalizations = hm;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, Object> getOperationalizations() {
		return operationalizations;
	}
}
