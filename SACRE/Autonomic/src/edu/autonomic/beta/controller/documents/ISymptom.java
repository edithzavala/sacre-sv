package edu.autonomic.beta.controller.documents;

import java.util.HashMap;

/** 
* @author Edith Zavala
*/

public interface ISymptom extends IDocument{

	public void setSymptoms(HashMap<String,String> crAndCase);

	public HashMap<String,String> getSymptoms();

}
