package edu.autonomic.beta.controller.mapekImp.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.autonomic.beta.controller.dataMining.Tool;
import edu.autonomic.beta.controller.documents.IContextData;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documents.IPolicy;
import edu.autonomic.beta.controller.documents.ISymptom;
import edu.autonomic.beta.controller.documentsImp.Symptom;
import edu.autonomic.beta.controller.documentsImp.policies.MonitorPolicy;
import edu.autonomic.beta.controller.documentsImp.sensorsData.ContextData;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.mapek.IAnalyzer;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapek.IKB;
import edu.autonomic.beta.controller.mapek.Producer;
import edu.autonomic.beta.controller.mapekBehavior.AMonitor;
import edu.autonomic.beta.controller.mapekImp.KBSV;
import edu.autonomic.beta.controller.mapekImp.analyzer.AnalyzerSV;
import edu.autonomic.beta.controller.touchpointsImp.sensors.SensorsSV;

/** 
* @author Edith Zavala
*/

public class MonitorSV extends AMonitor implements IComponent,
		Producer<ISymptom> {

	private List<IComponent> observers;
	private State state;
	private String name;
	private KBSV kb;
	private ContextStorage cs;
	private Condition cd;
	private long interReadingsTime;
	private HashMap<Map.Entry, Integer> sympRecord;
	private int minSymptoms;
	private Tool dmTool;

	public MonitorSV(State state, String name, MonitorPolicy mp) {
		super();
		this.state = state;
		this.name = name;
		this.observers = new ArrayList<IComponent>();
		this.cs = new ContextStorage();
		this.cd = new Condition(mp);
		this.symptoms = new Symptom();
		this.sympRecord = new HashMap<Map.Entry, Integer>();
	}

	public ContextStorage getCtxStorage() {
		return this.cs;
	}

	public void setDMTool(Tool dmTool) {
		this.dmTool = dmTool;
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

	@Override
	public void notified(String name, State state, IDocument doc) {
	}

	public void setKB(IKB kb) {
		((KBSV) kb).attachObserver(this);
		this.kb = (KBSV) kb;
		this.minSymptoms = ((KBSV) kb).getMinSymptoms();
		this.interReadingsTime = ((KBSV) kb).getInterReadingsTime();
	}

	public void setAnalyzer(IAnalyzer analyzer) {
		((AnalyzerSV) analyzer).attachObserver(this);
	}

	public void feedSensedContext(IContextData context) {
		cs.storeContext(context, this.dmTool, this.kb);
		HashMap<String, String> results = cd.calculateReqSatisfaction(context);
		if (!results.isEmpty()) {
			if (this.sympRecord.isEmpty()) {
				@SuppressWarnings("rawtypes")
				Iterator it = results.entrySet().iterator();
				while (it.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry pairs = (Map.Entry) it.next();
					/* Symptom(s) not satisfied after all satisfied */
					this.sympRecord.put(pairs, 1);
				}
			} else {
				/* Symptom(s) not satisfied after some not satisfied */
				checkSympRecord(results);
			}
			buildSymptoms();
		} else {
			this.symptoms.getSymptoms().clear();
			this.sympRecord.clear();
		}
	}

	private void checkSympRecord(HashMap<String, String> results) {
		Iterator it = this.sympRecord.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (results.containsKey(((Map.Entry) (pairs.getKey())).getKey())) {
				if (results.get(((Map.Entry) (pairs.getKey())).getKey())
						.equals(((Map.Entry) (pairs.getKey())).getValue())) {
					/* Increase count in (record) for repeated symptoms */
					this.sympRecord.put((Map.Entry) pairs.getKey(),
							new Integer((int) pairs.getValue() + 1));
					/* Remove already counted unsatisfied symptoms */
					results.remove(((Map.Entry) (pairs.getKey())).getKey());
				} else {
					/* Remove from (record) not repeated symptoms - Now satisfied */
					this.sympRecord.remove(pairs.getKey());
				}
			} else {
				/* Remove from (record) not repeated symptoms - Now satisfied */
				this.sympRecord.remove(pairs.getKey());
			}
		}

		/*
		 * Add new symptoms from (results) not previously stored in (record) if
		 * exist - First time not satisfied after satisfied
		 */
		if (!results.isEmpty()) {
			Iterator it2 = results.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pairs2 = (Map.Entry) it2.next();
				this.sympRecord.put(pairs2, 1);
			}
		}
		AutonomicOuput.print(Type.DEBUG, this.getClass().toString(),
				this.sympRecord.toString());
	}

	protected void buildSymptoms() {
		HashMap<String, String> output = new HashMap<String, String>();
		Iterator it = this.sympRecord.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();

			if ((((String) ((Map.Entry) pairs.getKey()).getValue())
					.contains("2a"))
					|| (((String) ((Map.Entry) pairs.getKey()).getValue())
							.contains("2b"))
					|| (((String) ((Map.Entry) pairs.getKey()).getValue())
							.contains("2c"))) {
				output.put((String) ((Map.Entry) pairs.getKey()).getKey(),
						(String) ((Map.Entry) pairs.getKey()).getValue());
			}else if (((int) pairs.getValue() == this.minSymptoms)
					|| (((int) pairs.getValue() % this.minSymptoms) == 0)) {
				output.put((String) ((Map.Entry) pairs.getKey()).getKey(),
						(String) ((Map.Entry) pairs.getKey()).getValue());
			}
		}
		if (!output.isEmpty()) {
			this.symptoms.setSymptoms(output);
		} else {
			this.symptoms.getSymptoms().clear();
		}
	}

	@Override
	public ISymptom produce() {
		ISymptom temp = null;
		if (!this.symptoms.getSymptoms().isEmpty()) {
			temp = new Symptom();
			HashMap<String, String> output = new HashMap<String, String>();
			synchronized (this) {
				Iterator it = this.symptoms.getSymptoms().entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					output.put((String) pairs.getKey(),
							(String) pairs.getValue());
				}
			}
			temp.setSymptoms(output);
			this.symptoms.getSymptoms().clear();
		}
		return temp;
	}

	public void start() {
		this.startMonitoring();
	}

	@Override
	protected void startMonitoring() {
		Timer timer = new Timer();
		timer.schedule(new Cycle(this), 0, this.interReadingsTime);
	}

	class Cycle extends TimerTask {
		MonitorSV monitor;

		public Cycle(MonitorSV monitor) {
			this.monitor = monitor;
		}

		@Override
		public void run() {
			ArrayList<IComponent> copyObservers;
			ContextData aux;
			synchronized (monitor) {
				copyObservers = new ArrayList<IComponent>(monitor.observers);
			}
			Iterator<IComponent> i = copyObservers.iterator();
			while (i.hasNext()) {
				SensorsSV srAux = (SensorsSV) i.next();
				aux = (ContextData) srAux.getSensedContext();
				if (!aux.isEmpty()) {
					monitor.feedSensedContext(aux);
				} else {
					return;
				}
			}
		}
	}
}
