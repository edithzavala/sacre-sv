package edu.autonomic.beta.controller.mapekImp.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** 
* @author Edith Zavala
*/

public class SensorState {

	private HashMap<String, Integer> losts;
	private HashMap<String, Integer> outliers;
	private HashMap<String, Integer> up;
	List<String> out;
	private boolean variablesUp;
	private boolean variablesDown;
	private int minSymptoms = 3;

	public SensorState(String[] vars) {
		this.losts = new HashMap<String, Integer>();
		this.outliers = new HashMap<String, Integer>();
		this.up = new HashMap<String, Integer>();
		this.out = new ArrayList<String>();
		for (int i = 0; i < vars.length; i++) {
			losts.put(vars[i], new Integer(1));
			outliers.put(vars[i], new Integer(1));
			up.put(vars[i], new Integer(1));
			out.add(vars[i]);
		}
		this.variablesUp = false;
		this.variablesDown = false;
	}
	
	public void setVariablesUp(boolean variablesUp){
		this.variablesUp = variablesUp;
	}
	
	public boolean variablesUp(){
		return this.variablesUp;
	}
	
	public void setVariablesDown(boolean variablesDown){
		this.variablesDown = variablesDown;
	}
	
	public boolean variablesDown(){
		return this.variablesDown;
	}

	public List<String> getVars() {
		return this.out;
	}

	public List<String> analyze(String caseType, String var) {
		switch (caseType) {
		case "2a":
			Integer val = this.losts.get(var);
			if (val.intValue() == minSymptoms) {
				if (out.contains(var)) {
					out.remove(var);
					this.variablesDown = true;
				}
			} else {
				this.losts.put(var, new Integer(val.intValue() + 1));
			}
			;
			break;
		case "2b":
			Integer val1 = this.outliers.get(var);
			if (val1.intValue() == minSymptoms) {
				if (out.contains(var)) {
					out.remove(var);
					this.variablesDown = true;
				}
			} else {
				this.outliers.put(var, new Integer(val1.intValue() + 1));
			}
			;
			break;
		case "2c":
			Integer val2 = this.up.get(var);
			if (val2.intValue() == minSymptoms) {
				if (!out.contains(var)) {
					out.add(var);
					this.variablesUp = true;
				}
				losts.put(var,new Integer(0));
				outliers.put(var,new Integer(0));
				up.put(var,new Integer(0));
			} else {
				this.up.put(var, new Integer(val2.intValue() + 1));
			}
			;
			break;
		}
		return this.out;
	}
}
