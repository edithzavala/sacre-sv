package edu.autonomic.beta.controller.documentsImp;

import java.util.HashMap;

import edu.autonomic.beta.controller.documents.ISymptom;

/** 
* @author Edith Zavala
*/

public class Symptom implements ISymptom {

	public HashMap<String, String> symptoms;
	
	public Symptom() {
		this.symptoms= new HashMap<String, String>();
	}
	
	@Override
	public String getType() {
		return null;
	}
	@Override
	public void setSymptoms(HashMap<String, String> crAndCase) {
		this.symptoms = crAndCase;
	}
	
	@Override
	public HashMap<String, String> getSymptoms() {
		return this.symptoms;
	}
}
