package edu.autonomic.beta.controller.functions;

/** 
* @author Edith Zavala
*/

public class PERCLOSED {

	private static int currentCycle;
	private static int closed;
	
	public PERCLOSED() {
		currentCycle = 1;
		closed = 0;
	}
	
	public Double calculatePERCLOSED(Double eyeState){
		double out;
		
		if(eyeState.doubleValue()==-1){
			out=-1;
		}else if (eyeState.doubleValue() < 0.4){
			closed++;
			out = (closed*100) / currentCycle;
			currentCycle++;
		}else {
			out = (closed*100) / currentCycle;
			currentCycle++;
		}
		return new Double(out);
	}
	
	public void reStart(){
		PERCLOSED.currentCycle = 1;
		PERCLOSED.closed = 0;
	}
	
}
