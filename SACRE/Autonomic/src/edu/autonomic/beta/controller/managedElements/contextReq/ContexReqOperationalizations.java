package edu.autonomic.beta.controller.managedElements.contextReq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.autonomic.beta.controller.dB.DBCtxReqOpeController;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documentsImp.ctxReqOpeCtx.ContextReqOpeContext;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.Spliter;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.managedElements.IManagedElement;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.model.DAO.Operationalizations;

/** 
* @author Edith Zavala
*/

public class ContexReqOperationalizations implements IManagedElement,
		IComponent {
	private ArrayList<IComponent> observers;
	private State state;
	private String name;
	private DBCtxReqOpeController dbc;
	private ContextReqOpeContext cd;
	private List<Operationalizations> ope;
	private HashMap<String, String> varsNames;
	private HashMap<String, String> opeId;
	private Updater up;
	private int adaptNo;
	private Calendar cal;
	private String order = "";

	public ContexReqOperationalizations(State state, String name, Updater up) {
		this.observers = new ArrayList<IComponent>();
		this.state = state;
		this.name = name;
		this.dbc = new DBCtxReqOpeController();
		this.cd = new ContextReqOpeContext();
		this.ope = new ArrayList<Operationalizations>();
		this.varsNames = new HashMap<String, String>();
		this.opeId = new HashMap<String, String>();
		this.up = up;
		this.adaptNo = 0;
		this.cal = Calendar.getInstance();
	}

	public void startDB() throws IOException {
		dbc.initDB("/IContextualRequirements.txt", ope, varsNames, opeId);
	}

	public void start() throws IOException {
		/* IOperationalizations */
		HashMap<String, Object> opeOutput = new HashMap<String, Object>();
		for (int i = 0; i < ope.size(); i++) {
			opeOutput.put("cr" + (i + 1), (ope.get(i).getContextId() + ","
					+ ope.get(i).getBehaviorId() + ","
					+ ope.get(i).getVarsInvolved() + "," + ope.get(i)
					.getOperationalization()));
		}
		cd.getOperationalizations().setOperationalizations(opeOutput);
		notifyState();
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void setAdaptationActions(String param) {
		if (order.equals(param)) {
			return;
		} else {
			order = param;
		}
		String message = "Adaptation No. " + (++adaptNo) + ": " + param;
		AutonomicOuput.print(Type.INFO, this.getClass().toString(), message);

		String[] command = Spliter.split(param, " ");
		String crId = null;
		String[] crIdComponents = null;
		boolean allReq = false;
		int idx = 0;

		if (command[0].equals("REMOVE")) {
			allReq = true;
		} else {
			crId = opeId.get(command[0]);
			crIdComponents = Spliter.split(crId, " ");
			idx = 1;
		}

		Iterator<Operationalizations> it = ope.iterator();

		while (it.hasNext()) {
			Operationalizations aux = (Operationalizations) it.next();

			if (allReq) {
				crIdComponents = new String[2];
				crIdComponents[0] = aux.getContextId();
				crIdComponents[1] = aux.getBehaviorId();
			}

			if (aux.getContextId().equals(crIdComponents[0])
					&& aux.getBehaviorId().equals(crIdComponents[1])) {
				switch (command[idx]) { // Actions to do
				case "UPDATE":
					String varsInvolved = aux.getVarsInvolved();
					String operationalization = aux.getOperationalization();
					String cmdVarId = varsNames.get(command[2]);
					HashMap<String, String> hm = up.compose(varsInvolved,
							operationalization, cmdVarId, command[3]);
					dbc.updateOperationalization(aux, hm.get("varsInvolved"),
							hm.get("operationalization"));

					cd.getOperationalizations().updateOperationalization(
							"cr" + aux.getContextId().substring(3),
							aux.getContextId() + "," + aux.getBehaviorId()
									+ "," + aux.getVarsInvolved() + ","
									+ aux.getOperationalization());
					
					message = "Adapted CR: " + aux.getContextId() + "," + aux.getBehaviorId()
							+ "," + aux.getVarsInvolved() + ","
							+ aux.getOperationalization();
					AutonomicOuput.print(Type.DEBUG, this.getClass().toString(), message);

					break;
				case "REMOVE":
					String varsInvolved2 = aux.getVarsInvolved();
					String operationalization2 = aux.getOperationalization();

					if (operationalization2.contains(varsNames.get(command[1]))) {
						String cmdVarId2 = varsNames.get(command[1]);
						HashMap<String, String> hm2 = up.compose(varsInvolved2,
								operationalization2, cmdVarId2, "-1");
						dbc.updateOperationalization(aux,
								hm2.get("varsInvolved"),
								hm2.get("operationalization"));

						cd.getOperationalizations().updateOperationalization(
								"cr" + aux.getContextId().substring(3),
								aux.getContextId() + "," + aux.getBehaviorId()
										+ "," + aux.getVarsInvolved() + ","
										+ aux.getOperationalization());

						message = "Adapted CR: " + aux.getContextId() + ","
								+ aux.getBehaviorId() + ","
								+ aux.getVarsInvolved() + ","
								+ aux.getOperationalization();
						AutonomicOuput.print(Type.DEBUG, this.getClass().toString(), message);
					} else {
						message = aux.getContextId() + "'s operationalization does not contain the variable";
						AutonomicOuput.print(Type.DEBUG, this.getClass().toString(), message);
					}
					break;
				default:
					AutonomicOuput.print(Type.ERROR, this.getClass().toString(), "Command not supported");
					break;
				}
			}
		}
		notifyState();
	}

	@Override
	public void notifyState() {
		ArrayList<IComponent> copyObservers;
		ContextReqOpeContext copyCd;
		synchronized (this) {
			copyObservers = new ArrayList<IComponent>(this.observers);
			copyCd = this.cd;
		}
		Iterator<IComponent> i = copyObservers.iterator();
		while (i.hasNext()) {
			IComponent aux = i.next();
			String type = (aux.getClass().toString());
			if (!copyCd.isEmpty()
					&& (type.equals("class edu.autonomic.beta.controller.touchpointsImp.sensors.SensorsSV") || type
							.equals("class edu.autonomic.beta.controller.managedElements.sv.SmartVehicle"))) {
				aux.notified(this.name, this.state, copyCd);
			} else if (copyCd.isEmpty()) {
				aux.notified(this.name, this.state, null);
			}

		}

	}

	@Override
	public void attachObserver(IComponent observer) {
		observers.add(observer);
	}

	@Override
	public void deattachObserver(IComponent observer) {
		int idx = observers.indexOf(observer);
		observers.remove(idx);

	}

	@Override
	public void notified(String name, State state, IDocument doc) {
		System.out.println(this.name + ": " + name + " is " + state);
	}

	@Override
	public void notifiedResponse(HashMap<String, Object> response) {

	}
}
