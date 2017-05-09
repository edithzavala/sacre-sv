package edu.autonomic.beta.controller.touchpointsImp.sensors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.autonomic.beta.controller.documents.IContextData;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documents.IElementContext;
import edu.autonomic.beta.controller.documentsImp.policies.SensorsPolicy;
import edu.autonomic.beta.controller.documentsImp.sensorsData.ContextData;
import edu.autonomic.beta.controller.functions.Operations;
import edu.autonomic.beta.controller.managedElements.IManagedElement;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapek.IMonitor;
import edu.autonomic.beta.controller.mapekImp.monitor.MonitorSV;
import edu.autonomic.beta.controller.touchpointsBehavior.ASensors;

/** 
* @author Edith Zavala
*/

public class SensorsSV extends ASensors implements IComponent {

	private List<IComponent> observers;
	private State state;
	private String name;
	private ContextData cd;
	private ContextData tempCd;
	private List<IManagedElement> me;
	private ResponseAssigner ra;

	public SensorsSV(State state, String name, SensorsPolicy sp) {
		this.state = state;
		this.name = name;
		this.cd = new ContextData(sp);
		this.tempCd = new ContextData(sp);
		this.observers = new ArrayList<IComponent>();
		this.me = new ArrayList<IManagedElement>();
		this.ra = new ResponseAssigner();
	}

	public String getName() {
		return name;
	}

	@Override
	public void notifyState() {
		List<IComponent> copyObservers;
		synchronized (this) {
			copyObservers = new ArrayList<IComponent>(this.observers);
		}
		Iterator<IComponent> i = copyObservers.iterator();
		while (i.hasNext()) {
			i.next().notified(this.name, this.state, null);
		}
	}

	@Override
	public void attachObserver(IComponent mc) {
		observers.add(mc);
	}

	@Override
	public void deattachObserver(IComponent mc) {
		int idx = observers.indexOf(mc);
		observers.remove(idx);
	}

	public void calculateVariables(IDocument doc){
		this.tempCd.setContext(doc);
		ra.respondNotified("SmartVehicle", this.tempCd);
	}
	
	@Override
	public void notified(String name, State state, IDocument doc) {
		if (doc != null) {
			cd.setContext(doc);
		}
		if(cd.isEmpty()){
			cd.getSensorData().getOperations().reStartPERCLOSED();
		}
	}

	public void setMonitor(IMonitor monitor) {
		((MonitorSV) monitor).attachObserver(this);
	}

	public synchronized IContextData getSensedContext() {
		return this.cd;
	}

	public void setManagedElement(IManagedElement me) {
		this.me.add(me);
		ra.addME(me);
		((IComponent) (me)).attachObserver(this);
	}
}
