package edu.autonomic.beta.controller.touchpointsImp.effectors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.managedElements.IManagedElement;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.touchpointsBehavior.AEffectors;

/** 
* @author Edith Zavala
*/

public class EffectorsSV extends AEffectors implements IComponent {

	private List<IComponent> observers;
	private State state;
	private String name;
	private List<IManagedElement> me;
	private CommandsAssigner ca;
	private Calendar cal;

	public EffectorsSV(State state, String name) {
		this.state = state;
		this.name = name;
		this.observers = new ArrayList<IComponent>();
		this.me = new ArrayList<IManagedElement>();
		this.ca = new CommandsAssigner();
		this.cal = Calendar.getInstance();
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

	public void effectAction(String[] commands) {
		startEffecting(commands);
	}

	@Override
	protected void startEffecting(String[] commands) {
		if (commands.length>0) {
			for (int i = 0; i < commands.length; i++) {
				String command = commands[i];
				IManagedElement me = ca.assign(command);
				String message = "Effects adaptations";
				AutonomicOuput.print(Type.INFO, this.getClass().toString(), message);
				me.setAdaptationActions(command);
			}
		}else{
			String message = "No adaptations";
			AutonomicOuput.print(Type.INFO, this.getClass().toString(), message);
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

	public void setManagedElement(IManagedElement me) {
		this.me.add(me);
		this.ca.addME(me);
		((IComponent) me).attachObserver(this);
	}

	@Override
	public void notified(String name, State state, IDocument doc) {
	}
}
