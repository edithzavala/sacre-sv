package edu.autonomic.beta.model.DAO;

/** 
* @author Edith Zavala
*/

public class SensorData {

	public String timeStamp;
	public String varName;
	public String varValue;
	
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getVarValue() {
		return varValue;
	}
	public void setVarValue(Object varValue) {
		this.varValue = varValue.toString();
	}
}
