package edu.autonomic.beta.controller.mapekImp.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.autonomic.beta.controller.documents.IContextData;
import edu.autonomic.beta.controller.documentsImp.policies.MonitorPolicy;
import edu.autonomic.beta.controller.documentsImp.sensorsData.ContextData;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.Comparator;
import edu.autonomic.beta.controller.functions.OperationalizationSpliter;
import edu.autonomic.beta.controller.functions.Spliter;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;

/** 
* @author Edith Zavala
*/

public class Condition {

	private ArrayList<String> losts = new ArrayList<String>();
	private ArrayList<String> outliers = new ArrayList<String>();
	private HashMap<String, Double> varsMax;
	private HashMap<String, Double> varsMin;
	private HashMap<String, String> varsId;
	
	private int count;
	private boolean contains2C;

	public Condition(MonitorPolicy mp) {
		this.varsMax = mp.getVarsMax();
		this.varsMin = mp.getVarsMin();
		this.varsId = mp.getVarsId();
		count = 0;
		contains2C = false;
	}

	public HashMap<String, String> calculateReqSatisfaction(IContextData cd) {
		HashMap<String, String> output = new HashMap<String, String>();
		boolean first = true;
		Iterator<Map.Entry<String, Object>> it = (((ContextData) cd).getRequirementsData().getContext())
				.entrySet().iterator();
		String message = "";

		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
			String[] operationalization = Spliter.split((String) pair.getValue(), ",");
			String[] varsInvolved = Spliter.split(operationalization[2], " ");
			String[] varsValues;

			if(operationalization.length<4){
				varsValues = new String[0];
			}else{
				varsValues = Spliter.split(operationalization[3], " ");
			}
			
			String behId = operationalization[1];
			String type = checkCase(behId, varsInvolved, varsValues, cd);
			
			if (type != null && !first) {
				message += " | " + pair.getKey()
						+ " unsatisfied in case: " + type;
				output.put((String)pair.getKey(), type);
				if(type.contains("2c")){
					contains2C = true;
				}
			}else if(type != null){
				message += pair.getKey()
						+ " unsatisfied in case: " + type;
				first = false;
				output.put((String)pair.getKey(), type);
				if(type.contains("2c ")){
					contains2C = true;
				}
			}
		}
		if(!message.equals(""))
//		AutonomicOuput.print(Type.INFO, this.getClass().toString(),
//				message);
		if(contains2C){
			count++;
		}else{
			count=0;
		}
		return output;
	}

	private String checkCase(String behId, String[] varsInvolved,
			String[] varsValues, IContextData cd) {
	
		String caseType = null;
		boolean ope = true;
		
		if (varsValues.length == 0) {
			caseType = "1";
			return caseType;
		} else {
			for (int i = 0; i < varsInvolved.length; i++) {
				String varName = varsId.get(varsInvolved[i]);
				Double varCurrentVal = (Double) (((ContextData) cd))
						.getSensorData().getContext().get(varName);
				if (varCurrentVal.doubleValue() == -1.0) {
					if(operationalized(varsInvolved[i],varsValues)){
						caseType = "2a " + varName; /* Sensor lost */
					}
					if(!losts.contains(varName)){
						losts.add(varName);
					}
					ope = false;
				} else if (varCurrentVal.doubleValue() > varsMax.get(varName).doubleValue()
						|| varCurrentVal.doubleValue() < varsMin.get(varName).doubleValue()) {
					caseType = "2b " + varName; /* Sensor decallibrated */
					outliers.add(varName);
					ope = false;
				} else {
					if (losts.contains(varName)) {
						if(!operationalized(varsInvolved[i],varsValues) && count<9){
							caseType = "2c " + varName;/* Sensor up again */
						}else{
							losts.remove(varName);						
						}					
					} else if (outliers.contains(varName)) {
						if(!operationalized(varsInvolved[i],varsValues) && count<9){
							caseType = "2c " + varName; /* Sensor up again */
						}else{
							outliers.remove(varName);					
						}
					}
						ope = ope && evalOpe(varsInvolved[i], varsValues, varCurrentVal);
				}
			}
			Boolean beh = new Boolean((Boolean)(((ContextData) cd).getBehaviorData()
					.getContext().get(behId)));
			if (ope
					&& !beh.booleanValue()) {
				caseType = "3"; /* Violation */
			} else if (!ope
					&& beh.booleanValue()) {
				caseType = "4"; /* Wrong Context */
			}
		}
		return caseType;
	}

	public boolean evalOpe(String varInvolved, String[] varsValues,
			Double varCurrentVal) {
		boolean ope = true;
		for (int j = 0; j < varsValues.length; j++) {
			HashMap<String,String> info=  OperationalizationSpliter.split(varsValues[j]);
			if (varInvolved.equals(info.get("varId"))) {
				/* All relations are ANDs */
				ope = ope && (Comparator.compare(varCurrentVal,new Double(Double.parseDouble(info.get("value"))), info.get("operator")));
			}
		}
		return ope;
	}

	public boolean operationalized(String varInvolved, String[] varsValues) {
		boolean ope = false;
		for (int j = 0; j < varsValues.length; j++) {
			if (varInvolved.equals(varsValues[j].substring(0, 4))) {
				ope=true;
			}
		}
		return ope;
	}
}
