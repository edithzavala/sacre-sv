package edu.autonomic.beta.controller.documents;

import java.util.HashMap;

/** 
* @author Edith Zavala
*/

public interface IContext extends IDocument{

	public void setContext(IElementContext currentValues);

	public HashMap<String, Object> getContext();
}
