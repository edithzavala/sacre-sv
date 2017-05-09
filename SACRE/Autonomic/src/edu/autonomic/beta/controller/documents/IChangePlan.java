package edu.autonomic.beta.controller.documents;

import java.util.List;

/** 
* @author Edith Zavala
*/

public interface IChangePlan extends IDocument{
	
	public void setChangePlan(List <String> action);

	public List <String> getChangePlan();

	public void clear();
}
