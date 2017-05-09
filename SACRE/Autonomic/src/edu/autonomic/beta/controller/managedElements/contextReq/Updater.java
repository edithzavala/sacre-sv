package edu.autonomic.beta.controller.managedElements.contextReq;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class Updater {

	private HashMap<String, String[]> hm;
	private HashMap<String, String[]> hmT;

	public Updater(String fileName) {
		this.hm = new HashMap<String, String[]>();
		this.hmT = new HashMap<String, String[]>();
		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] vars = Spliter.split(prop.getProperty("vars"), ",");
		for (int i = 0; i < vars.length; i++) {
			hm.put(vars[i], Spliter.split(prop.getProperty(vars[i]), ","));
			hmT.put(vars[i],
					Spliter.split(prop.getProperty(vars[i] + "T"), ","));
		}
	}

	public HashMap<String, String> compose(String varsInvolved, String ope,
			String varId, String cmdOpe) {
		HashMap<String, String> out = new HashMap<String, String>();
		ArrayList<String> opeOut = new ArrayList<String>();
		if (!cmdOpe.equals("-1")) {
			String[] varRanges = hm.get(varId);
			String[] varT = hmT.get(varId);
			String newOpe = "";

			for (int i = 0; i < varRanges.length; i++) {
				if (varRanges[i].equals(cmdOpe)) {
					String[] opeAux = Spliter.split(varT[i], " ");
					if (opeAux.length == 2) {
						newOpe = varId + opeAux[0] + " " + varId + opeAux[1];
					} else {
						newOpe = varId + varT[i];
					}
				}
			}

			if (!varsInvolved.contains(varId)) {
				out.put("varsInvolved", varsInvolved + " " + varId);
				out.put("operationalization", ope + " " + newOpe);
			} else {
				out.put("varsInvolved", varsInvolved);
				String[] opes = Spliter.split(ope, " ");
				for (int i = 0; i < opes.length; i++) {
					if (!opes[i].substring(0, 4).equals(varId)) {
						opeOut.add(opes[i]);
					}
				}
				opeOut.add(newOpe);
				ope = opeOut.get(0);
				for (int i = 1; i < opeOut.size(); i++) {
					ope = ope + " " + opeOut.get(i);
				}
				out.put("operationalization", ope);
			}
		} else {
			out.put("varsInvolved", varsInvolved);
			if (!varsInvolved.contains(varId)) {
				out.put("operationalization", ope);
			} else {
				String[] opes = Spliter.split(ope, " ");
				for (int i = 0; i < opes.length; i++) {
					if (!opes[i].substring(0, 4).equals(varId)) {
						opeOut.add(opes[i]);
					}
				}

				if (!opeOut.isEmpty()) {
					ope = opeOut.get(0);
					for (int i = 1; i < opeOut.size(); i++) {
						ope = ope + " " + opeOut.get(i);
					}
					out.put("operationalization", ope);
				}else{
					out.put("operationalization", "");
				}
			}
		}
		return out;
	}
}
