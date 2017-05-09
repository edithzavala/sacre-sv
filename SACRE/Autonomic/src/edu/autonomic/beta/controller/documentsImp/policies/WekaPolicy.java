package edu.autonomic.beta.controller.documentsImp.policies;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

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

public class WekaPolicy implements IPolicy {

	private HashMap<String, IVarAdaptation> adaptation;
	private String type;
	private HashMap<String, String> data;
	private String[] attr;
	private String[] vars;

	public WekaPolicy(String fileName) {
		this.type = "WekaPolicy";
		this.adaptation = new HashMap<String, IVarAdaptation>();
		this.data = new HashMap<String, String>();

		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {

			e.printStackTrace();
		}

		this.vars = Spliter.split(prop.getProperty("vars"), ",");
		this.data.put("relation", prop.getProperty("relation"));
		this.data.put("attribute", prop.getProperty("attribute"));
		this.attr = Spliter.split(prop.getProperty("attribute"), ",");

		for (int i = 0; i < attr.length; i++) {
			this.data.put(attr[i], prop.getProperty(attr[i]));
		}

		for (int i = 0; i < vars.length; i++) {
			switch (vars[i]) {
			case "PERCLOSED":
				this.adaptation
						.put(vars[i],
								new JRipPERCLOSEDAdapt(Spliter.split(
										prop.getProperty(vars[i]
												+ "AdaptValues"), ",")));
				break;
			case "facePosition":
				this.adaptation
						.put(vars[i],
								new JRipFacePositionAdapt(Spliter.split(
										prop.getProperty(vars[i]
												+ "AdaptValues"), ",")));
				break;
			case "heartBeatsPerMinute":
				this.adaptation
						.put(vars[i],
								new JRipHeartBeatsPerMinuteAdapt(Spliter.split(
										prop.getProperty(vars[i]
												+ "AdaptValues"), ",")));
				break;
			case "handsOnSteeringWheel":
				this.adaptation
						.put(vars[i],
								new JRiphHandsOnSteeringWheelAdapt(Spliter
										.split(prop.getProperty(vars[i]
												+ "AdaptValues"), ",")));
				break;
			}
		}
	}

	public HashMap<String, IVarAdaptation> getAdaptation() {
		return this.adaptation;
	}

	public String[] getAttr() {
		return this.attr;
	}

	public HashMap<String, String> getData() {
		return this.data;
	}

	@Override
	public String getType() {
		return null;
	}
}
