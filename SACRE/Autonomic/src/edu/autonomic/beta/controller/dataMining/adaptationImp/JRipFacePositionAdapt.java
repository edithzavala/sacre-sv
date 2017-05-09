package edu.autonomic.beta.controller.dataMining.adaptationImp;

import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;

import java.lang.Math;

/** 
* @author Edith Zavala
*/

public class JRipFacePositionAdapt implements IVarAdaptation {

	private static String[] ranges;	
	
	public JRipFacePositionAdapt(String[] facePositionAdaptValues) {
		JRipFacePositionAdapt.ranges=facePositionAdaptValues;
	}
	
	public String get(double x) {
		return function(x);
	}

	private static String function(double x) {
		String out = "";
		if (x == -1.0) {
			out = ranges[0];
		} else if (x < 0) {
			out = ranges[1];
		} else {
			out = ranges[(int) (Math.min(Math.ceil(x + 2), 4))];
		}
		return out;
	}
}
