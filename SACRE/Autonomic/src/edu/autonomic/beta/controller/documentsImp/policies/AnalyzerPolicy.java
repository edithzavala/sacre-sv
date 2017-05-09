package edu.autonomic.beta.controller.documentsImp.policies;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import edu.autonomic.beta.controller.dataMining.DataMining;
import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;
import edu.autonomic.beta.controller.dataMining.adaptationImp.JRipFacePositionAdapt;
import edu.autonomic.beta.controller.dataMining.adaptationImp.JRipHeartBeatsPerMinuteAdapt;
import edu.autonomic.beta.controller.dataMining.adaptationImp.JRipPERCLOSEDAdapt;
import edu.autonomic.beta.controller.dataMining.adaptationImp.JRiphHandsOnSteeringWheelAdapt;
import edu.autonomic.beta.controller.documents.IPolicy;
import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class AnalyzerPolicy implements IPolicy {

	private String algorithm;
	private String tool;
	private String type;
	private String[] response;
	private String[] vars;

	public AnalyzerPolicy(String fileName) {
		this.type = "AnalyzerPolicy";

		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {

			e.printStackTrace();
		}

		this.algorithm = prop.getProperty("algorithm");
		this.tool = prop.getProperty("tool");
		this.response = Spliter.split(prop.getProperty("response"), ",");
		this.vars = Spliter.split(prop.getProperty("vars"), ",");

	}

	public String[] getVars() {
		return this.vars;
	}

	public String[] getResponse() {
		return this.response;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	@Override
	public String getType() {
		return this.type;
	}
}
