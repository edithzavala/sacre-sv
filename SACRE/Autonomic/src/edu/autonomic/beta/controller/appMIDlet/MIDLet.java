package edu.autonomic.beta.controller.appMIDlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import edu.autonomic.beta.controller.documentsImp.policies.AnalyzerPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.KBPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.MonitorPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.PlannerPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.SensorsPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.WekaPolicy;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.managedElements.contextReq.ContexReqOperationalizations;
import edu.autonomic.beta.controller.managedElements.contextReq.Updater;
import edu.autonomic.beta.controller.managedElements.sv.SmartVehicle;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapekImp.AutonomicManagerSV;
import edu.autonomic.beta.model.dataAccess.DataAccess;

/** 
* @author Edith Zavala
*/

public class MIDLet extends MIDlet {

	AutonomicManagerSV autMSV;
	SmartVehicle sm;
	ContexReqOperationalizations crOpe;

	SensorsPolicy sp;
	MonitorPolicy mp;
	AnalyzerPolicy ap;
	KBPolicy kbp;
	PlannerPolicy pp;
	WekaPolicy wp;

	public MIDLet() {
	}

	@Override
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		cleanUpActions();
		notifyDestroyed();
		System.out
				.println("************************FINISHED**************************");
	}

	@Override
	protected void startApp() throws MIDletStateChangeException {

		System.out
				.println("************************SACRE**************************");

		autMSV = new AutonomicManagerSV();
		sm = new SmartVehicle(IComponent.State.OK, "SmartVehicle",
				"/svUpdate.properties");
		crOpe = new ContexReqOperationalizations(IComponent.State.OK,
				"ContextualRequirements", new Updater(
						"/contextReqVarsUpdate.properties"));
		sp = new SensorsPolicy("/sensorsPolicy.properties");
		mp = new MonitorPolicy("/monitorPolicy.properties");
		ap = new AnalyzerPolicy("/analyzerPolicy.properties");
		kbp = new KBPolicy("/kbPolicy.properties");
		pp = new PlannerPolicy("/plannerPolicy.properties");
		wp = new WekaPolicy("/wekaPolicy.properties");

		/* Specific Application Initialization */
		try {
			sm.startDB();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			crOpe.startDB();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/* Autonomic Manager (MAPE-K) Initialization + DM specific Tool(s) */
		autMSV.start(sp, mp, ap, kbp, pp);
		autMSV.analyzer.getDM().getTool().setPolicy(wp);
		autMSV.knowledgeBase.setDMTool(autMSV.analyzer.getDM().getTool());
		autMSV.monitor.setDMTool(autMSV.analyzer.getDM().getTool());

		/* Activate missing observers */
		autMSV.sensors.setManagedElement(sm);
		autMSV.sensors.setManagedElement(crOpe);

		autMSV.effectors.setManagedElement(sm);
		autMSV.effectors.setManagedElement(crOpe);

		crOpe.attachObserver(sm);

		sm.notifyState();
		crOpe.notifyState();

		/* Start initial processes */
		try {
			sm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			crOpe.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		autMSV.monitor.start();
	}

	public void cleanUpActions() {
		System.out
				.println("*******************************************************");
		System.out.println("CleanUp Actions...");
	}
}
