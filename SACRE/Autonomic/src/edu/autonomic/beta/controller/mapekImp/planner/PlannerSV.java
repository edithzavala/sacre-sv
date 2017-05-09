package edu.autonomic.beta.controller.mapekImp.planner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.json.me.JSONException;

import edu.autonomic.beta.controller.documents.IChangePlan;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documents.IRequestForChange;
import edu.autonomic.beta.controller.documentsImp.ChangePlan;
import edu.autonomic.beta.controller.documentsImp.policies.PlannerPolicy;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapek.IExecuter;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.mapekBehavior.APlanner;
import edu.autonomic.beta.controller.mapekImp.KBSV;
import edu.autonomic.beta.controller.mapekImp.executer.ExecuterSV;

/** 
* @author Edith Zavala
*/

public class PlannerSV extends APlanner implements IComponent {

	private List<IComponent> observers;
	private State state;
	private String name;
	private ExecuterSV executer;
	private KBSV kb;
	private PoliciesReviser pr;
	private Calendar cal;

	public PlannerSV(State state, String name, PlannerPolicy pp) {
		this.state = state;
		this.name = name;
		this.changePlan = new ChangePlan();
		this.observers = new ArrayList<IComponent>();
		this.pr = new PoliciesReviser(pp);
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

	public void analyzeReqsForChange(IRequestForChange reqForChange) {
		startPlanning(reqForChange);
		IChangePlan cp = new ChangePlan();
		synchronized (this) {
			cp.setChangePlan(this.changePlan.getChangePlan());
		}
		this.executer.executeChangePlan(cp);
		this.changePlan.clear();
	}

	@Override
	protected void startPlanning(IRequestForChange reqForChange) {
		boolean aproved = true;
		ArrayList<String> actions = new ArrayList<String>();
		Iterator<String> it = reqForChange.getReqForChange().iterator();
		while (it.hasNext()) {
			String req = it.next();
			try {
				aproved = pr.revise(req);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (aproved) {
				AutonomicOuput.print(Type.INFO, this.getClass().toString(), "policies allow changes");
				String[] aux = null;
				try {
					aux = pr.getActions(req);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < aux.length; i++) {
					actions.add(aux[i]);
				}
			} else {
				AutonomicOuput.print(Type.INFO, this.getClass().toString(), "policies don't allow changes");
			}
		}
		this.buildChangePlan(actions);
	}

	@Override
	public void buildChangePlan(ArrayList<String> actions) {
		String message = "Generates Change Plan";
		/* Adding change plans */
		this.changePlan.setChangePlan(actions);
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

	public void setKB(IKB kb) {
		((KBSV) kb).attachObserver(this);
		this.kb = (KBSV) kb;
	}

	public void setExecuter(IExecuter executer) {
		((ExecuterSV) executer).attachObserver(this);
		this.executer = (ExecuterSV) executer;
	}
}
