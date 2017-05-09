package edu.autonomic.beta.controller.dataMining.adaptationImp;

import edu.autonomic.beta.controller.dataMining.adaptation.*;

/** 
* @author Edith Zavala
*/

public class JRipPERCLOSEDAdapt implements IVarAdaptation {
	
	private static String[] ranges;
	
	public JRipPERCLOSEDAdapt(String[] PERCLOSEDAdaptValues) {
		JRipPERCLOSEDAdapt.ranges = PERCLOSEDAdaptValues;
	}
	
	public String get(double x){
		return function(x);
	}
	
	private static String function(double x){
		String out="";
		if(x==-1.0){
			out=ranges[0];
		}else if(x<0.05){
			out=ranges[1];
		}else if(x<0.15){
			out=ranges[2];	
		}else {
			out=ranges[(int)(Math.min((Math.ceil((x*10)+1)),5))];
		}
		return out;
	}
}
