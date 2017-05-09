package edu.autonomic.beta.controller.mapekImp.monitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.microedition.rms.RecordStoreException;

import edu.autonomic.beta.controller.dataMining.Tool;
import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;
import edu.autonomic.beta.controller.dataMining.adaptationImp.Adapter;
import edu.autonomic.beta.controller.documents.IContextData;
import edu.autonomic.beta.controller.documentsImp.policies.WekaPolicy;
import edu.autonomic.beta.controller.documentsImp.sensorsData.ContextData;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.mapek.Producer;
import edu.autonomic.beta.controller.mapekImp.KBSV;
import edu.autonomic.beta.model.DAO.SensorData;
import edu.autonomic.beta.model.DAO.SensorDataIAFactor;

/** 
* @author Edith Zavala
*/

public class ContextStorage implements Producer<Object>{

	private HashMap<String, HashMap<String, String>> record;
	private int timeStamp;
	
	public ContextStorage(){
		this.timeStamp = 0;
		this.record = new HashMap<String, HashMap<String, String>>();
		this.record.put("SensorDataIA", null);
		this.record.put("SensorData", null);
	}
	
	public void storeContext(IContextData cd, Tool dmTool,KBSV kb) {
		storeSensorDataIA(((ContextData)cd).getBehaviorData().getContext(),kb);
		storeSensorData(((ContextData)cd).getSensorData().getContext(),dmTool,kb);
		
		if ((timeStamp % 100) == 0) {
			String message = (timeStamp + 1) + " record(s) stored";
			AutonomicOuput.print(Type.DEBUG, this.getClass().toString(),
					message);
		}
		this.timeStamp++;
	}

	@Override
	public Object produce() {
		HashMap<String, HashMap<String, String>> aux = null;
		if(this.record.get("SensorDataIA")!=null && this.record.get("SensorData")!=null){
			aux = new HashMap<String, HashMap<String, String>>();
			aux.put("SensorDataIA",this.record.get("SensorDataIA"));
			aux.put("SensorData",this.record.get("SensorData"));
			this.record.put("SensorDataIA", null);
			this.record.put("SensorData", null);
		}
		return aux;
	}
	
	public void storeSensorData(
			HashMap<String, Object> sdContext, Tool dmTool, KBSV kb) {
		HashMap<String, String> out = new HashMap<String, String>();
		Iterator<Map.Entry<String, Object>> it = sdContext.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it
					.next();
			SensorData sd = new SensorData();
			sd.setTimeStamp(Double.toString(this.timeStamp));
			sd.setVarName((String) pair.getKey());

			IVarAdaptation va = ((WekaPolicy)(dmTool.getPolicy())).getAdaptation().get((String) pair.getKey());
			double x = (double) pair.getValue();
			String interval = Adapter.doAdaptation(va, x);

			sd.setVarValue(interval);

			out.put(sd.getVarName(), sd.getVarValue());
		}
		this.record.put("SensorData", out);
	}

	public void storeSensorDataIA(
			HashMap<String, Object> sdIAFac, KBSV kb) {
		HashMap<String, String> out = new HashMap<String, String>();
		Iterator<Map.Entry<String, Object>> it = sdIAFac.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it
					.next();
			SensorDataIAFactor sdIA = new SensorDataIAFactor();
			sdIA.setTimeStamp(Double.toString(this.timeStamp));
			sdIA.setCrId("cr" + pair.getKey().substring(3));
			sdIA.setCrIA(new Boolean(Boolean.parseBoolean(pair.getValue()
					.toString()))); 	
			out.put(sdIA.getCrId(), String.valueOf(sdIA.crIA));
		}
		this.record.put("SensorDataIA", out);
	}
}
