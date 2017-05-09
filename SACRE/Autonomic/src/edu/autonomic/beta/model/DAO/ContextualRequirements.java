package edu.autonomic.beta.model.DAO;

/** 
* @author Edith Zavala
*/

public class ContextualRequirements {

	public String crId;
	public String contextId;
	public String behaviorId;

	public String getCrId() {
		return crId;
	}

	public void setCrId(String crId) {
		this.crId = crId;
	}

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

}
