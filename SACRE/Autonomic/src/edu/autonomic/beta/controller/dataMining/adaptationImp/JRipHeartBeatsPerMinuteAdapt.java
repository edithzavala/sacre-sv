package edu.autonomic.beta.controller.dataMining.adaptationImp;

import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;

/** 
* @author Edith Zavala
*/

public class JRipHeartBeatsPerMinuteAdapt implements IVarAdaptation {
	
	private static String[] ranges;
	
	public JRipHeartBeatsPerMinuteAdapt(String[] heartBeatsPerMinuteAdaptValues) {
		JRipHeartBeatsPerMinuteAdapt.ranges = heartBeatsPerMinuteAdaptValues;
	}
	
	public String get(double x){
		return function(x);
	}
	
	private static String function(double x){
		String out = "";
		if (x == -1.0) {
			out = ranges[0];
		} else if (x > 0.75) {
			out = ranges[5];
		} else if(x>0.55){
			out = ranges[4];
		}else if(x>0.45){
			out = ranges[3];
		}else if(x>=0.30){
			out = ranges[2];
		}else{
			out = ranges[1];
		}
		return out;
	}

}
