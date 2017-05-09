package edu.autonomic.beta.controller.documentsImp.sensorsData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.autonomic.beta.controller.documents.IContext;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documents.IElementContext;
import edu.autonomic.beta.controller.documentsImp.policies.SensorsPolicy;
import edu.autonomic.beta.controller.documentsImp.svContext.SensorsContext;
import edu.autonomic.beta.controller.functions.Operations;
import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class SensorData implements IContext {

	private HashMap<String, Object> hm;
	private HashMap<String, String> varsOperations;
	private HashMap<String, Double> varsMax;
	private HashMap<String, Double> varsMin;
	private String type;
	private Operations ope;

	public SensorData(SensorsPolicy sp) {
		this.type = "SensorData";
		this.hm = new HashMap<String, Object>();
		this.varsOperations = sp.getVarsOperations();
		this.varsMax = sp.getVarsMax();
		this.varsMin = sp.getVarsMin();
		this.ope = new Operations();
	}

	public String getType() {
		return this.type;
	}

	@Override
	public void setContext(IElementContext currentValues) {
		calculateContext(currentValues);
	}

	private void calculateContext(IDocument currentValues) {
		Iterator<Map.Entry<String, String>> it = varsOperations.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pair = (Map.Entry<String,String>) it.next();
			String[] line = Spliter.split((String) pair.getValue(), " ");
			String[] argsNames = Spliter.split(line[1], ",");
			Double[] argsValues=new Double[argsNames.length];
			for (int i = 0; i < argsNames.length; i++) {
				argsValues[i] = new Double ((((SensorsContext) currentValues).getSensors().get(argsNames[i])).toString());
			}
			Double result = ope.doOperation(line[0], argsValues);
			Double out=normalize((String) pair.getKey(), result);
			hm.put((String) pair.getKey(), out);
		}
	}
	
	private Double normalize(String varName, Double data) {
		double normVal;
		double max = varsMax.get(varName).doubleValue();
		double min = varsMin.get(varName).doubleValue();

		if (max == min) {
			normVal = 0.5;
		} else {
			normVal = (data.doubleValue() - min) / (max - min);
		}
		
		if(normVal>1 || normVal<0){
			normVal=-1.0;
		}
		return new Double(normVal);
	}

	@Override
	public HashMap<String, Object> getContext() {
		return this.hm;
	}
	
	public Operations getOperations(){
		return this.ope;
	}
	
	public void setOperations(Operations ope){
		this.ope = ope;
	}
}
