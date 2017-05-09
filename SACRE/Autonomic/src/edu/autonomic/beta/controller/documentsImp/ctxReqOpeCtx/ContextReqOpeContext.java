package edu.autonomic.beta.controller.documentsImp.ctxReqOpeCtx;

import edu.autonomic.beta.controller.documents.IMEContext;

/** 
* @author Edith Zavala
*/

public class ContextReqOpeContext  implements IMEContext{

	private String type;
	private OperationalizationsContext operationalizations;

	public ContextReqOpeContext() {
		this.operationalizations = new OperationalizationsContext();
		this.type="ContextReqOpeContext";
	}

	public void setOperationalizations(OperationalizationsContext ope) {
		this.operationalizations = ope;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public OperationalizationsContext getOperationalizations() {
		return operationalizations;
	}
	
	@Override
	public boolean isEmpty(){
		return (getOperationalizations().getOperationalizations().isEmpty());
	}
}
