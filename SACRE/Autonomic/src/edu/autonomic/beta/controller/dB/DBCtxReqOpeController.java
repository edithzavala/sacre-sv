package edu.autonomic.beta.controller.dB;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import edu.autonomic.beta.controller.functions.ReadLine;
import edu.autonomic.beta.controller.functions.Spliter;
import edu.autonomic.beta.model.DAO.Operationalizations;
import edu.autonomic.beta.model.dataAccess.DataAccess;

/** 
* @author Edith Zavala
*/

public class DBCtxReqOpeController {

	public void initDB(String file, List<Operationalizations> ope,
			HashMap<String, String> varsNames, HashMap<String, String> opeId)
			throws IOException {

		String line = null;
		InputStreamReader reader = new InputStreamReader(getClass()
				.getResourceAsStream(file));

		while ((line = ReadLine.read(reader)) != null) {
			try {
				createEntity(line);
				if (line.equals("Operationalizations")) {
					ReadLine.read(reader);
					while (!((line = ReadLine.read(reader)).equals("*"))) {
						String[] aux = Spliter.split(line, ",");
						Operationalizations operationalization = new Operationalizations();
						operationalization.setContextId(aux[0]);
						operationalization.setBehaviorId(aux[1]);
						operationalization.setVarsInvolved(aux[2]);
						operationalization.setOperationalization(aux[3]);
						ope.add(operationalization);
						addOperationalization(operationalization);
					}
				}
				if (line.equals("Variables")) {
					ReadLine.read(reader);
					while (!((line = ReadLine.read(reader)).equals("*"))) {
						String[] aux = Spliter.split(line, ",");
						varsNames.put(aux[1], aux[0]);
					}
				}
				if (line.equals("OpeIds")) {
					ReadLine.read(reader);
					while (!((line = ReadLine.read(reader)).equals("*"))) {
						String[] aux = Spliter.split(line, ",");
						opeId.put(aux[0], aux[1]);
					}
				}
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}

		}
	}

	public void createEntity(String tableName) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		DataAccess da = new DataAccess();
		da.createTable(tableName);
	}

	public void deleteEntity(String tableName) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		DataAccess da = new DataAccess();
		da.deleteTable(tableName);
	}

	public void addOperationalization(Operationalizations ope)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		DataAccess da = new DataAccess();
		da.insertRecord(
				"Operationalizations",
				ope.getContextId() + "," + ope.getBehaviorId() + ","
						+ ope.getVarsInvolved() + ","
						+ ope.getOperationalization());
	}

	public void updateOperationalization(Operationalizations ope,
			String newVarsInvolved, String newOperationalization) {
		DataAccess da = new DataAccess();
		da.updateRecord(
				"Operationalizations",
				ope.getContextId() + "," + ope.getBehaviorId() + ","
						+ ope.getVarsInvolved() + ","
						+ ope.getOperationalization(), ope.getContextId() + ","
						+ ope.getBehaviorId() + "," + "," + newVarsInvolved
						+ "," + newOperationalization);
		ope.setVarsInvolved(newVarsInvolved);
		ope.setOperationalization(newOperationalization);
	}

	public void deleteOperationalization(Operationalizations ope) {
		DataAccess da = new DataAccess();
		da.deleteRecord(
				"Operationalizations",
				ope.getContextId() + "," + ope.getBehaviorId() + ","
						+ ope.getVarsInvolved() + ","
						+ ope.getOperationalization());
	}
}
