package edu.autonomic.beta.controller.documentsImp.policies;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import edu.autonomic.beta.controller.documents.IPolicy;
import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class SensorsPolicy implements IPolicy {
	
	private HashMap<String, String> varsOperations;
	private HashMap<String, Double> varsMax;
	private HashMap<String, Double> varsMin;

	public SensorsPolicy(String fileName) {
		this.varsMax =  new HashMap<String, Double>();
		this.varsMin = new HashMap<String, Double>();
		this.varsOperations = new HashMap<String, String>();
		Properties prop = new Properties();		
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] vars = Spliter.split(prop.getProperty("vars"), ",");
		for (int i = 0; i < vars.length; i++) {
			varsMax.put(vars[i],new Double(Double.parseDouble(prop.getProperty(vars[i] + "Max"))));
			varsMin.put(vars[i],new Double(Double.parseDouble(prop.getProperty(vars[i] + "Min"))));
			varsOperations.put(vars[i], prop.getProperty(vars[i] + "Ope"));	
		}
	}
	
	public HashMap<String, String> getVarsOperations() {
		return varsOperations;
	}

	public void setVarsOperations(HashMap<String, String> varsOperations) {
		this.varsOperations = varsOperations;
	}

	public HashMap<String, Double> getVarsMax() {
		return varsMax;
	}

	public void setVarsMax(HashMap<String, Double> varsMax) {
		this.varsMax = varsMax;
	}

	public HashMap<String, Double> getVarsMin() {
		return varsMin;
	}

	public void setVarsMin(HashMap<String, Double> varsMin) {
		this.varsMin = varsMin;
	}

	@Override
	public String getType() {
		return null;
	}

}
