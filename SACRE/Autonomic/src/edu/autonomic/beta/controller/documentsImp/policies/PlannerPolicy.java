package edu.autonomic.beta.controller.documentsImp.policies;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class PlannerPolicy {

	private HashMap<String, Double> varsTresholdsMax;
	private HashMap<String, Double> varsTresholdsMin;
	private String[] vars;

	public PlannerPolicy(String fileName) {
		this.varsTresholdsMax = new HashMap<String, Double>();
		this.varsTresholdsMin = new HashMap<String, Double>();

		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		vars = Spliter.split(prop.getProperty("vars"), ",");
		for (int i = 0; i < vars.length; i++) {
			this.varsTresholdsMax.put(vars[i],
					new Double(Double.parseDouble(prop.getProperty(vars[i] + "Max"))));
			this.varsTresholdsMin.put(vars[i],
					new Double(Double.parseDouble(prop.getProperty(vars[i] + "Min"))));
		}
	}

	public HashMap<String, Double> getVarsTresholdsMax() {
		return varsTresholdsMax;
	}

	public void setVarsTresholdsMax(HashMap<String, Double> varsTresholdsMax) {
		this.varsTresholdsMax = varsTresholdsMax;
	}

	public HashMap<String, Double> getVarsTresholdsMin() {
		return varsTresholdsMin;
	}

	public void setVarsTresholdsMin(HashMap<String, Double> varsTresholdsMin) {
		this.varsTresholdsMin = varsTresholdsMin;
	}

	public String[] getVars() {
		return vars;
	}

	public void setVars(String[] vars) {
		this.vars = vars;
	}
}
