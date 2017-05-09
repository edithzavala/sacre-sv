package edu.autonomic.beta.controller.documents;

import java.util.List;

/** 
* @author Edith Zavala
*/

public interface IRequestForChange extends IDocument{

	public void setReqForChange(List<String> actions);

	public List<String> getReqForChange();
	
	public void clear();
}
