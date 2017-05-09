package edu.autonomic.beta.controller.functions;

/** 
* @author Edith Zavala
*/

public class Comparator {

	public static boolean compare(Double num1, Double num2, String operator) {
		boolean out = false;
		// TODO: Change to hashmap and assign operation result to out
		switch (operator) {
		case "<": 
			if (num1.doubleValue()<num2.doubleValue()) {
				out = true;
			}
			;
			break;
		case ">":
			if (num1.doubleValue()>num2.doubleValue()) {
				out = true;
			}
			;
			break;
		case "=":
			if (num1.doubleValue()==num2.doubleValue()) {
				out = true;
			}
			;
			break;
		case "<=":
			if (num1.doubleValue()<=num2.doubleValue()) {
				out = true;
			}
			;
			break;
		case ">=":
			if (num1.doubleValue()>=num2.doubleValue()) {
				out = true;
			}
			;
			break;
		}
		return out;
	}
}
