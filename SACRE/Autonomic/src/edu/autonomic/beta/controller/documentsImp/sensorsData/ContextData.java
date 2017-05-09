package edu.autonomic.beta.controller.documentsImp.sensorsData;

import edu.autonomic.beta.controller.documents.IContextData;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documentsImp.ctxReqOpeCtx.ContextReqOpeContext;
import edu.autonomic.beta.controller.documentsImp.policies.SensorsPolicy;
import edu.autonomic.beta.controller.documentsImp.svContext.SVContext;

/** 
* @author Edith Zavala
*/

public class ContextData implements IContextData{

	private SensorData sensorData;
	private BehaviorData behaviorData;
	private RequirementsData requirementsData;
	private String type;
	private SensorsPolicy sp;

	public ContextData(SensorsPolicy sp) {
		this.sensorData = new SensorData(sp);
		this.behaviorData = new BehaviorData();
		this.requirementsData = new RequirementsData();
		this.type = "ContextData";
		this.sp = sp;
	}
	
	public SensorsPolicy getSp(){
		return this.sp;
	}
	public SensorData getSensorData() {
		return sensorData;
	}

	public void setSensorData(SensorData sensorData) {
		this.sensorData = sensorData;
	}

	public RequirementsData getRequirementsData() {
		return requirementsData;
	}

	public void setRequirementsData(RequirementsData requirementsData) {
		this.requirementsData = requirementsData;
	}

	public BehaviorData getBehaviorData() {
		return behaviorData;
	}

	public void setBehaviorData(BehaviorData behaviorData) {
		this.behaviorData = behaviorData;
	}

	@Override
	public String getType() {
		return this.type;
	}
	
	public void setContext(IDocument doc){
		if (doc.getType().equals("SVContext")) {
			SVContext aux = (SVContext) doc;
			sensorData.setContext(aux.getSensorCtx());
			behaviorData.setContext(aux.getBehaviorCtx());
		}
		if (doc.getType().equals("ContextReqOpeContext")) {
			ContextReqOpeContext aux = (ContextReqOpeContext) doc;
			requirementsData.setContext(aux.getOperationalizations());
		}
	}
	
	public boolean isEmpty(){
		return ((getBehaviorData().getContext().isEmpty())||(getRequirementsData().getContext().isEmpty())||(getSensorData().getContext().isEmpty()));
	}
}
