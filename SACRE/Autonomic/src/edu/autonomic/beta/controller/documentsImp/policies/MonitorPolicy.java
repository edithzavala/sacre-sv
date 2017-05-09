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

public class MonitorPolicy implements IPolicy {

	private HashMap<String, Double> varsMax;
	private HashMap<String, Double> varsMin;
	private HashMap<String, String> varsId;
	private String[] vars;

	public MonitorPolicy(String fileName) {
		this.varsMax = new HashMap<String, Double>();
		this.varsMin = new HashMap<String, Double>();
		this.varsId = new HashMap<String, String>();
		
		Properties prop = new Properties();		
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		vars = Spliter.split(prop.getProperty("vars"), ",");
		for (int i = 0; i < vars.length; i++) {
			varsMax.put(vars[i],new Double(Double.parseDouble(prop.getProperty(vars[i] + "Max"))));
			varsMin.put(vars[i], new Double(Double.parseDouble(prop.getProperty(vars[i] + "Min"))));
			varsId.put(prop.getProperty(vars[i] + "Id"),vars[i]);	
		}
	}

	public String[] getVars(){
		return this.vars;
	}
	
	public HashMap<String, String> getVarsId() {
		return varsId;
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
