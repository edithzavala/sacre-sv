package edu.autonomic.beta.controller.mapekImp.executer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import edu.autonomic.beta.controller.documents.IChangePlan;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.mapekBehavior.AExecuter;
import edu.autonomic.beta.controller.mapekImp.KBSV;
import edu.autonomic.beta.controller.touchpoints.IEffectors;
import edu.autonomic.beta.controller.touchpointsImp.effectors.EffectorsSV;

/** 
* @author Edith Zavala
*/

public class ExecuterSV extends AExecuter implements IComponent {

	private List<IComponent> observers;
	private State state;
	private String name;
	private EffectorsSV effector;
	private Calendar cal;

	public ExecuterSV(State state, String name) {
		this.state = state;
		this.name = name;
		this.observers = new ArrayList<IComponent>();
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
		return;
	}

	public void executeChangePlan(IChangePlan changePlan) {
			startExecuting(changePlan);
	}

	@Override
	protected void startExecuting(IChangePlan changePlan) {
		String[] commands = CommandGenerator.createCommand(changePlan.getChangePlan());
		String message = "Executes Change Plan";
		AutonomicOuput.print(Type.INFO, this.getClass().toString(), message);
		effector.effectAction(commands);
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

	@Override
	public void notified(String name, State state, IDocument doc) {
	}

	public void setEffector(IEffectors effector) {
		((EffectorsSV) effector).attachObserver(this);
		this.effector = (EffectorsSV) effector;
	}

	public void setKB(IKB kb) {
		((KBSV) kb).attachObserver(this);
	}
}
