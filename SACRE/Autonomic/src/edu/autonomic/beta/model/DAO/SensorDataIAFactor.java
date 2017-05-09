package edu.autonomic.beta.model.DAO;

/** 
* @author Edith Zavala
*/

public class SensorDataIAFactor {
	public String timeStamp;
	public String crId;
	public boolean crIA;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getCrId() {
		return crId;
	}
	public void setCrId(String crId) {
		this.crId = crId;
	}
	public boolean isCrIA() {
		return crIA;
	}
	public void setCrIA(Object crIA) {
		this.crIA = Boolean.parseBoolean(crIA.toString());
	}
}
