package edu.autonomic.beta.controller.mapekImp.executer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.autonomic.beta.controller.functions.Spliter;

/** 
* @author Edith Zavala
*/

public class CommandGenerator {

	public static String[] createCommand(List<String> actions) {
		String[] out = null;
		List<String> accepted = new ArrayList<String>();
		/* Translate rules in commands and eliminate redundand actions */
		Iterator<String> it = actions.iterator();
		while (it.hasNext()) {
			String next = it.next();
			String[] aux = Spliter.split(next, " ");
			if (!aux[1].equals("NO_ACTIONS")) {
				accepted.add(next);
			}
		}
		out = new String[accepted.size()];
		for(int i=0; i < out.length;i++){
			out[i] = accepted.get(i);
		}
		return out;
	}
}
