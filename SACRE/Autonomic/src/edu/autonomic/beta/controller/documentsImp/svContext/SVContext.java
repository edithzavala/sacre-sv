package edu.autonomic.beta.controller.documentsImp.svContext;

import edu.autonomic.beta.controller.documents.*;

/** 
* @author Edith Zavala
*/

public class SVContext implements IMEContext{

	private SensorsContext sensorCtx;
	private BehaviorsContext behaviorCtx;
	private String type;
	
	public SVContext() {
		sensorCtx = new SensorsContext();
		behaviorCtx = new BehaviorsContext();
		this.type = "SVContext";
	}

	public SensorsContext getSensorCtx() {
		return sensorCtx;
	}

	public void setSensorCtx(SensorsContext sensorCtx) {
		this.sensorCtx = sensorCtx;
	}

	public BehaviorsContext getBehaviorCtx() {
		return behaviorCtx;
	}

	public void setBehaviorCtx(BehaviorsContext behaviorCtx) {
		this.behaviorCtx = behaviorCtx;
	}

	@Override
	public String getType() {
		return this.type;
	}
	
	@Override
	public boolean isEmpty(){
		return ((getBehaviorCtx().getBehaviors().isEmpty())||(getSensorCtx().getSensors().isEmpty()));
	}
}
