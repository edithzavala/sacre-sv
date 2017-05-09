package edu.autonomic.beta.controller.dataMining;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;
import edu.autonomic.beta.controller.documents.IPolicy;
import edu.autonomic.beta.controller.mapekImp.KBSV;

/** 
* @author Edith Zavala
*/

public interface Tool {
	public String runTool(String crId, String algorithm,
			List<String> variables, KBSV dataLocation) throws UnsupportedEncodingException;
	public void setPolicy(IPolicy p);
	public IPolicy getPolicy();
}
