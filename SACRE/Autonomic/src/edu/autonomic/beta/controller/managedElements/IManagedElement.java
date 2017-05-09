package edu.autonomic.beta.controller.managedElements;

import java.util.HashMap;

/** 
* @author Edith Zavala
*/

public interface IManagedElement {

	public void setAdaptationActions(String param); // effectors
																	// commands
	public void notifiedResponse(HashMap<String,Object> response);
	
	public String getName();

}
