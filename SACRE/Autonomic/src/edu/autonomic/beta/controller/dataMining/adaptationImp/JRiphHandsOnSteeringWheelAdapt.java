package edu.autonomic.beta.controller.dataMining.adaptationImp;

import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;

/** 
* @author Edith Zavala
*/

public class JRiphHandsOnSteeringWheelAdapt implements IVarAdaptation {

	private static String[] ranges;
	
	public JRiphHandsOnSteeringWheelAdapt(String[] handsOnSteeringWheelAdaptValues) {
		JRiphHandsOnSteeringWheelAdapt.ranges=handsOnSteeringWheelAdaptValues;
	}
	
	public String get(double x){
		return function(x);
	}
	
	private static String function(double x){
		String out="";
		if(x==-1.0){
			out=ranges[0];
		}else if(x<0){
			out=ranges[1];
		}else {
			out=ranges[(int)(Math.min(Math.ceil((x+1)*2),5))];
		}
		return out;
	}
}
