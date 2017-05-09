package edu.autonomic.beta.controller.dataMining.adaptationImp;

import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;

/** 
* @author Edith Zavala
*/

public class Adapter {

	public static String doAdaptation(IVarAdaptation va, double x) {
		String output = va.get(x); 
		return output;
	}

}
