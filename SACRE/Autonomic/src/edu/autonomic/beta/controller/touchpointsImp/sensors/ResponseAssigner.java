package edu.autonomic.beta.controller.touchpointsImp.sensors;

import java.util.ArrayList;
import java.util.Iterator;

import edu.autonomic.beta.controller.documentsImp.sensorsData.ContextData;
import edu.autonomic.beta.controller.managedElements.IManagedElement;

/** 
* @author Edith Zavala
*/

public class ResponseAssigner {

	private ArrayList<IManagedElement> me;

	public ResponseAssigner() {
		this.me = new ArrayList<IManagedElement>();
	}

	public void respondNotified(String meName,ContextData copyCd){
		if(meName.equals("SmartVehicle")){
			Iterator<IManagedElement> it = this.me.iterator();
			while(it.hasNext()){
				IManagedElement aux = it.next();
				if(aux.getName().equals(meName)){
					aux.notifiedResponse(copyCd.getSensorData().getContext());
				}
			}
		}
	}

	public void addME(IManagedElement me) {
		this.me.add(me);
	}
}
