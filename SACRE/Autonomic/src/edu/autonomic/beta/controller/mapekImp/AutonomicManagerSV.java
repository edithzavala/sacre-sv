package edu.autonomic.beta.controller.mapekImp;

import edu.autonomic.beta.controller.documentsImp.policies.AnalyzerPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.KBPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.MonitorPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.PlannerPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.SensorsPolicy;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapekBehavior.AAutonomicManager;
import edu.autonomic.beta.controller.mapekImp.analyzer.AnalyzerSV;
import edu.autonomic.beta.controller.mapekImp.executer.ExecuterSV;
import edu.autonomic.beta.controller.mapekImp.monitor.MonitorSV;
import edu.autonomic.beta.controller.mapekImp.planner.PlannerSV;
import edu.autonomic.beta.controller.touchpointsImp.effectors.EffectorsSV;
import edu.autonomic.beta.controller.touchpointsImp.sensors.SensorsSV;
import edu.autonomic.beta.controller.documents.ISymptom;;

/** 
* @author Edith Zavala
*/

public class AutonomicManagerSV extends AAutonomicManager {

	public MonitorSV monitor;
	public AnalyzerSV analyzer;
	public PlannerSV planner;
	public KBSV knowledgeBase;
	public ExecuterSV executer;
	public SensorsSV sensors;
	public EffectorsSV effectors;

	public void start(SensorsPolicy sp,MonitorPolicy mp,AnalyzerPolicy ap, KBPolicy kbp, PlannerPolicy pp) {
		/* Initialize all the modules */
		SyncronizedBuffer<ISymptom> bufferSymptoms = new SyncronizedBuffer<ISymptom>();
		SyncronizedBuffer<Object> bufferMonitorData = new SyncronizedBuffer<Object>();
		knowledgeBase = new KBSV(IComponent.State.OK,"KBSV",kbp);
		monitor = new MonitorSV(IComponent.State.OK,"MonitorSV",mp);
		analyzer = new AnalyzerSV(IComponent.State.OK,"AnalyzerSV",ap);
		planner = new PlannerSV(IComponent.State.OK,"PlannerSV",pp);
		executer = new ExecuterSV(IComponent.State.OK,"ExecutorSV");
		sensors = new SensorsSV(IComponent.State.OK, "SensorsSV",sp);
		effectors = new EffectorsSV(IComponent.State.OK,"EffectorsSV");
		
		setResources();
		getAliveMessage();
		Thread consumer = new Thread(new ConsumerTask<ISymptom>(bufferSymptoms,analyzer));
		Thread producer = new Thread(new ProducerTask<ISymptom>(bufferSymptoms,monitor));
		Thread consumer1 = new Thread(new ConsumerTask<Object>(bufferMonitorData,knowledgeBase));
		Thread producer1 = new Thread(new ProducerTask<Object>(bufferMonitorData,monitor.getCtxStorage()));
		consumer.start();
		producer.start();
		consumer1.start();
		producer1.start();
	}

	private void setResources() {
		monitor.setKB(knowledgeBase);
		analyzer.setKB(knowledgeBase);
		planner.setKB(knowledgeBase);
		executer.setKB(knowledgeBase);

		sensors.setMonitor(monitor);
		monitor.setAnalyzer(analyzer);
		analyzer.setPlanner(planner);
		planner.setExecuter(executer);
		executer.setEffector(effectors);
	}

	public void getAliveMessage() {
		monitor.notifyState();
		analyzer.notifyState();
		planner.notifyState();
		executer.notifyState();
		effectors.notifyState();
		knowledgeBase.notifyState();
	}

}
