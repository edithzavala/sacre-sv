package edu.autonomic.beta.controller.mapekImp.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.me.JSONException;

import edu.autonomic.beta.controller.dataMining.DataMining;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documents.IRequestForChange;
import edu.autonomic.beta.controller.documents.ISymptom;
import edu.autonomic.beta.controller.documentsImp.RequestForChange;
import edu.autonomic.beta.controller.documentsImp.policies.AnalyzerPolicy;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.functions.Spliter;
import edu.autonomic.beta.controller.mapek.Consumer;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.mapek.IPlanner;
import edu.autonomic.beta.controller.mapekBehavior.AAnalyzer;
import edu.autonomic.beta.controller.mapekImp.KBSV;
import edu.autonomic.beta.controller.mapekImp.planner.PlannerSV;

/** 
* @author Edith Zavala
*/

public class AnalyzerSV extends AAnalyzer implements IComponent,
		Consumer<ISymptom> {

	private List<IComponent> observers;
	private State state;
	private String name;
	private PlannerSV planner;
	private KBSV kb;
	private Response resp;
	private DataMining dm;
	private SensorState ss;
	private Calendar cal;
	private boolean adaptations;

	public AnalyzerSV(State state, String name, AnalyzerPolicy ap) {
		this.state = state;
		this.name = name;
		this.reqForChange = new RequestForChange();
		this.observers = new ArrayList<IComponent>();
		this.dm = new DataMining(ap.getAlgorithm(), ap.getTool());
		this.resp = new Response(ap.getResponse());
		this.ss = new SensorState(ap.getVars());
		this.cal = Calendar.getInstance();
		this.adaptations = false;

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
	public void consume(ISymptom item) {
		analyzeSymptoms(item);
	}

	@Override
	public void analyzeSymptoms(ISymptom symptoms) {
		if (!symptoms.getSymptoms().isEmpty()) {
			startAnalyzing(symptoms);
			if (this.adaptations) {
				IRequestForChange rc = new RequestForChange();
				synchronized (this) {
					rc.setReqForChange(this.reqForChange.getReqForChange());
				}
				this.planner.analyzeReqsForChange(rc);
				this.reqForChange.clear();
				this.adaptations = false;
			}
		}
	}

	protected void buildRequestForChange(List<String> rules)
			throws JSONException {
		String message = "Generates Request for Change";
		// AutonomicOuput.print(Type.INFO, this.getClass().toString(), message);
		List<String> actions = new ArrayList<String>();
		Iterator<String> it = rules.iterator();
		while (it.hasNext()) {
			String out = resp.processResponse(it.next());
			actions.add(out);
			// AutonomicOuput.print(Type.INFO, this.getClass().toString(), out);
		}
		this.reqForChange.setReqForChange(actions);
	}

	@Override
	protected void startAnalyzing(ISymptom symptoms) {
		List<String> sympVar = new ArrayList<String>();
		List<String> varRemoved_Added = new ArrayList<String>();
		Iterator<Map.Entry<String, String>> it = symptoms.getSymptoms()
				.entrySet().iterator();
		List<String> output = new ArrayList<String>();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = (Map.Entry<String, String>) it
					.next();
			String[] value = Spliter.split((String) pair.getValue(), " ");
			String caseType = value[0];
			List<String> variables = ss.getVars();

			if (value.length > 1 && !sympVar.contains(value[1])) {
				sympVar.add(value[1]);

				if (!caseType.equals("2c") && variables.contains(value[1])) {
					variables = ss.analyze(caseType, value[1]);
					//TODO:Extract removed variables from a runtime list containing all possible variables, not one by one.
					if (!variables.contains("handsOnSteeringWheel")) {
						if (!output
								.contains("{\"REMOVE\": handsOnSteeringWheel}")) {
							output.add("{\"REMOVE\": handsOnSteeringWheel}");
						}
					}
					if (!variables.contains("heartBeatsPerMinute")) {
						if (!output
								.contains("{\"REMOVE\": heartBeatsPerMinute}")) {
							output.add("{\"REMOVE\": heartBeatsPerMinute}");
						}
					}
					if (!variables.contains("facePosition")) {
						if (!output.contains("{\"REMOVE\": facePosition}")) {
							output.add("{\"REMOVE\": facePosition}");
						}
					}
					if (!variables.contains("PERCLOSED")) {
						if (!output.contains("{\"REMOVE\": PERCLOSED}")) {
							output.add("{\"REMOVE\": PERCLOSED}");
						}
					}
				} else if (caseType.equals("2c")
						&& !variables.contains(value[1])) {
					variables = ss.analyze(caseType, value[1]);
					if (variables.contains(value[1])) {
						output.add("{\"ADD\": " + value[1] + "}");
					}
				}
			}

			try {
				if ((caseType.equals("2a") || caseType.equals("2b"))
						&& !ss.variablesDown()) {
					if (!varRemoved_Added.contains(value[1])
							&& variables.contains(value[1])) {
						output.add("variable " + value[1]
								+ " candidate to be removed");
						varRemoved_Added.add(value[1]);
					}
				} else if (ss.variablesDown()) {
					kb.setVariables(ss.getVars());
					varRemoved_Added.add(value[1]);
					this.adaptations = true;
					ss.setVariablesDown(false);
				} else if (caseType.equals("2c") && !ss.variablesUp()) {
					if (!varRemoved_Added.contains(value[1])
							&& !variables.contains(value[1])) {
						output.add("variable " + value[1]
								+ " candidate to be added");
						varRemoved_Added.add(value[1]);
					}
				} else if (ss.variablesUp()) {
					kb.setVariables(ss.getVars());
					varRemoved_Added.add(value[1]);
					ss.setVariablesUp(false);
				} else {
					String head = "{\"crId\":\"" + pair.getKey()
							+ "\",\"result\":";
					String message = "Requests data mining for: "
							+ pair.getKey();
					AutonomicOuput.print(Type.INFO, this.getClass().toString(),
							message);

					String out = head
							+ dm.runDataMining(this.kb, (String) pair.getKey(),
									variables) + "}";
					output.add(out);
					message = "Receives data mining response for: "
							+ pair.getKey();
					AutonomicOuput.print(Type.INFO, this.getClass().toString(),
							message);
					this.adaptations = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!output.isEmpty()) {
			AutonomicOuput.print(Type.INFO, this.getClass().toString(),
					output.toString());
		}

		if (this.adaptations) {
			try {
				this.buildRequestForChange(output);
			} catch (JSONException e) {
				e.printStackTrace();
			}
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

	@Override
	public void notified(String name, State state, IDocument doc) {
	}

	@Override
	public void setKB(IKB kb) {
		((KBSV) kb).attachObserver(this);
		this.kb = (KBSV) kb;
		this.kb.setVariables(ss.getVars());
	}

	@Override
	public void setPlanner(IPlanner planner) {
		((PlannerSV) planner).attachObserver(this);
		this.planner = (PlannerSV) planner;
	}

	public DataMining getDM() {
		return this.dm;
	}
}
