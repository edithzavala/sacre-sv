package edu.autonomic.beta.controller.dataMining;

import java.io.IOException;
import java.util.List;

import edu.autonomic.beta.controller.mapekImp.KBSV;

/** 
* @author Edith Zavala
*/

public class DataMining {

	private String algorithm;
	private Tool tool;
	private String name;

	public DataMining(String algorithm, String tool) {
		this.algorithm = algorithm;
		this.name = "DataMiningSV";
		try {
			Class<?> theClass = Class.forName(tool);
			this.tool = (Tool) theClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public String runDataMining(KBSV dataLocation, String crId,
			List<String> variables) throws IOException {
		return tool.runTool(crId, this.algorithm, variables, dataLocation);
	}

	public String getName() {
		return this.name;
	}

	public Tool getTool() {
		return this.tool;
	}
}
