package edu.autonomic.beta.controller.dB;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.autonomic.beta.controller.functions.*;
import edu.autonomic.beta.model.DAO.*;
import edu.autonomic.beta.model.dataAccess.*;

/** 
* @author Edith Zavala
*/

public class DBSVController {

	public void initDB(String fileName, ArrayList<Contexts> ctx,
			ArrayList<Behaviors> beh, ArrayList<ContextualRequirements> cr,
			ArrayList<Sensors> srs,ArrayList<Variables> var) throws IOException {
		String line = null;

		InputStreamReader reader = new InputStreamReader(getClass()
				.getResourceAsStream(fileName));

		try {
			while ((line = ReadLine.read(reader)) != null) {
				try {
					createEntity(line);
					if (line.equals("Contexts")) {
						ReadLine.read(reader);
						int i = 1;
						while (!((line = ReadLine.read(reader)).equals("*"))) {
							String[] aux = Spliter.split(line, ",");
							Contexts context = new Contexts();
							context.setContextId("ctx" + (i));
							context.setContextDescription(aux[0]);
							ctx.add(context);
							addContext(context);
							i++;
						}
					}

					if (line.equals("Behaviors")) {
						ReadLine.read(reader);
						int i = 1;
						while (!((line = ReadLine.read(reader)).equals("*"))) {
							String[] aux = Spliter.split(line, ",");
							Behaviors behavior = new Behaviors();
							behavior.setBehaviorId("beh" + (i));
							behavior.setBehaviorDescription(aux[0]);
							beh.add(behavior);
							addBehavior(behavior);
							i++;
						}
					}

					if (line.equals("ContextualRequirements")) {
						ReadLine.read(reader);
						int i = 1;
						while (!((line = ReadLine.read(reader)).equals("*"))) {
							String[] aux = Spliter.split(line, ",");
							ContextualRequirements crs = new ContextualRequirements();
							crs.setCrId("cr" + (i));
							crs.setContextId(aux[0]);
							crs.setBehaviorId(aux[1]);
							cr.add(crs);
							addContextualRequirement(crs);
							i++;
						}
					}
					if (line.equals("Sensors")) {
						ReadLine.read(reader);
						int i = 1;
						while (!((line = ReadLine.read(reader)).equals("*"))) {
							String[] aux = Spliter.split(line, ",");
							Sensors sensor = new Sensors();
							sensor.setSensorId("srs" + (i));
							sensor.setSensorType(aux[0]);
							srs.add(sensor);
							addSensor(sensor);
							i++;
						}
					}
					if (line.equals("Variables")) {
						ReadLine.read(reader);
						int i = 1;
						while (!((line = ReadLine.read(reader)).equals("*"))) {
							String[] aux = Spliter.split(line, ",");
							Variables variable = new Variables();
							variable.setVarId("var" + (i));
							variable.setVarName(aux[0]);
							variable.setSensorId(aux[1]);
							variable.setMax(Double.parseDouble(aux[2]));
							variable.setMin(Double.parseDouble(aux[3]));
							var.add(variable);
							addVariable(variable);
							i++;
						}
					}
				} catch (RecordStoreException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader.close();
	}

	private void createEntity(String tableName)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		DataAccess da = new DataAccess();
		da.createTable(tableName);
	}

	public void deleteEntity(String tableName)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		DataAccess da = new DataAccess();
		da.deleteTable(tableName);
	}

	public void addContext(Contexts ctx) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		DataAccess da = new DataAccess();
		da.insertRecord("Contexts",
				ctx.getContextId() + "," + ctx.getContextDescription());
	}

	public void addBehavior(Behaviors beh) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		DataAccess da = new DataAccess();
		da.insertRecord("Behaviors",
				beh.getBehaviorId() + "," + beh.getBehaviorDescription());
	}

	public void addContextualRequirement(ContextualRequirements contextualReq)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		DataAccess da = new DataAccess();
		da.insertRecord("ContextualRequirements", contextualReq.getContextId()
				+ "," + contextualReq.getBehaviorId());
	}

	public void addSensor(Sensors srs) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		DataAccess da = new DataAccess();
		da.insertRecord("Sensors",
				srs.getSensorId() + "," + srs.getSensorType());
	}

	public void addVariable(Variables var) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		DataAccess da = new DataAccess();
		da.insertRecord("Variables", var.getVarId() + "," + var.getVarName()
				+ "," + var.getSensorId());
	}
}
