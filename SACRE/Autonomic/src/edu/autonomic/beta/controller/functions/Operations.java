package edu.autonomic.beta.controller.functions;

/** 
* @author Edith Zavala
*/

public class Operations {

	PERCLOSED p;
			
	public Operations() {
		p = new PERCLOSED();
	}
	
	public Double division(Double[] arguments){
		Double output=arguments[0];
		for(int i=1; i<arguments.length;i++){
			output = Double.valueOf(output.doubleValue()/arguments[i].doubleValue());
		}
		return output;
	}

	public Double multiplication(Double[] arguments) {
		Double output=arguments[0];
		for(int i=1; i<arguments.length;i++){
			output = Double.valueOf(output.doubleValue()*arguments[i].doubleValue());
		}
		return output;
	}

	public Double sum(Double[] arguments) {
		Double output=arguments[0];
		for(int i=1; i<arguments.length;i++){
			output = Double.valueOf(output.doubleValue()+arguments[i].doubleValue());
		}
		return output;
	}

	public Double substraction(Double[] arguments) {
		Double output=arguments[0];
		for(int i=1; i<arguments.length;i++){
			output = Double.valueOf(output.doubleValue()-arguments[i].doubleValue());
		}
		return output;
	}

	public Double PERCLOSED(Double[] arguments) {
		return p.calculatePERCLOSED(arguments[0]);
	}

	public void reStartPERCLOSED(){
		p.reStart();
	}
	
	public Double doOperation(String operation, Double[] arguments) {
		Double output = null;
		if (arguments != null && operation!=null) {
			switch (operation) {
			case "Division":
				output = division(arguments);
				break;
			case "Multiplication":
				output = multiplication(arguments);
				break;
			case "Sum":
				output = sum(arguments);
				break;
			case "Substraction":
				output = substraction(arguments);
				break;
			case "PERCLOSED":				
				output = PERCLOSED(arguments);
				break;
			case "-":
				output = arguments[0];
				break;
			default:
				output = new Double(-1.0);
			}
		}
		return output;
	}
}
