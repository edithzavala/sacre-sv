package edu.autonomic.beta.controller.touchpointsImp.effectors;

import java.util.ArrayList;
import java.util.Iterator;

import edu.autonomic.beta.controller.functions.Spliter;
import edu.autonomic.beta.controller.managedElements.IManagedElement;

/** 
* @author Edith Zavala
*/

public class CommandsAssigner {

	private ArrayList<IManagedElement> me;

	public CommandsAssigner() {
		this.me = new ArrayList<IManagedElement>();
	}

	public IManagedElement assign(String command){
		String[] content = Spliter.split(command, " ");
		IManagedElement output=null;
		if(content[0].substring(0,2).equals("cr") || content[0].substring(0,6).equals("REMOVE")){
			Iterator<IManagedElement> it = me.iterator();
			while(it.hasNext()){
				IManagedElement auxMe = (IManagedElement)it.next();
				if(auxMe.getName().equals("ContexReqOperationalizations"));
				output = auxMe;
			}
		}else{
			Iterator<IManagedElement> it = me.iterator();
			while(it.hasNext()){
				IManagedElement auxMe = (IManagedElement)it.next();
				if(auxMe.getName().equals("SmartVehicle"));
				output = auxMe;
			}
		}
		return output;
	}
	
	public void addME(IManagedElement me){
		this.me.add(me);
	}
}
