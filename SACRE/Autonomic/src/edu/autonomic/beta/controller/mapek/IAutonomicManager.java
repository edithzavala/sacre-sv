package edu.autonomic.beta.controller.mapek;

import edu.autonomic.beta.controller.documentsImp.policies.AnalyzerPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.KBPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.MonitorPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.PlannerPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.SensorsPolicy;

/** 
* @author Edith Zavala
*/

public interface IAutonomicManager {

	public void start(SensorsPolicy sp,MonitorPolicy mp,AnalyzerPolicy ap, KBPolicy kbp,PlannerPolicy pp);
}
