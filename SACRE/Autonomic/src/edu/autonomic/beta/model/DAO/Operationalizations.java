package edu.autonomic.beta.model.DAO;

/** 
* @author Edith Zavala
*/

public class Operationalizations {

	public String contextId;
	public String behaviorId;
	public String varsInvolved;
	public String operationalization;

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getBehaviorId() {
		return behaviorId;
	}

	public void setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
	}

	public String getVarsInvolved() {
		return varsInvolved;
	}

	public void setVarsInvolved(String varsInvolved) {
		this.varsInvolved = varsInvolved;
	}

	public String getOperationalization() {
		return operationalization;
	}

	public void setOperationalization(String operationalization) {
		this.operationalization = operationalization;
	}
}
