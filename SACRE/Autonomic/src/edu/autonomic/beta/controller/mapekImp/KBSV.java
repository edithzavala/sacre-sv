package edu.autonomic.beta.controller.mapekImp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import edu.autonomic.beta.controller.dataMining.FileComposer;
import edu.autonomic.beta.controller.dataMining.HeaderComposer;
import edu.autonomic.beta.controller.dataMining.Tool;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documentsImp.policies.KBPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.WekaPolicy;
import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.functions.ReadLine;
import edu.autonomic.beta.controller.mapek.Consumer;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.mapekBehavior.AKB;
import edu.autonomic.beta.model.DAO.SensorData;
import edu.autonomic.beta.model.DAO.SensorDataIAFactor;
import edu.autonomic.beta.model.dataAccess.DataAccess;

/** 
* @author Edith Zavala
*/

public class KBSV extends AKB implements IComponent, Consumer<Object> {

	private ArrayList<IComponent> observers = new ArrayList<IComponent>();
	private State state;
	private String name;
	private long interReadingsTime;
	private int minSymptoms;
	private Tool dmTool;
	private List<String> variables;
	private boolean varsChanged;

	/**************************** Attributes Weka-specific *********************/
	private HeaderComposer hc; // WEKA file header
	private FileComposer fc;

	/*************************************************************************/

	public KBSV(State state, String name, KBPolicy kbp) {
		this.state = state;
		this.name = name;
		this.interReadingsTime = kbp.getInterReadingTime();
		this.minSymptoms = kbp.getMinSymptoms();
		this.variables = new ArrayList<String>();
		try {
			initDB("/IKB.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.varsChanged = false;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
		this.varsChanged = true;
	}

	public void setDMTool(Tool dmTool) {
		this.dmTool = dmTool;
		WekaPolicy wp = (WekaPolicy) dmTool.getPolicy();
		this.hc = new HeaderComposer(wp.getData());
		this.fc = new FileComposer(wp.getAttr());
	}

	private void initDB(String file) throws IOException {
		String line = null;
		InputStreamReader reader = new InputStreamReader(getClass()
				.getResourceAsStream(file));

		while ((line = ReadLine.read(reader)) != null) {
			try {
				createEntity(line);
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}

	public long getInterReadingsTime() {
		return this.interReadingsTime;
	}

	public int getMinSymptoms() {
		return this.minSymptoms;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void notifyState() {
		ArrayList<IComponent> copyObservers;
		synchronized (this) {
			copyObservers = new ArrayList<IComponent>(this.observers);
		}
		Iterator<IComponent> i = copyObservers.iterator();
		while (i.hasNext()) {
			i.next().notified(this.name, this.state, null);
		}
		return;
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

	@Override
	public void consume(Object item) {
		generateFiles((HashMap<String, HashMap<String, String>>) item);
	}

	private void generateFiles(
			HashMap<String, HashMap<String, String>> recordData) {
		int records = 1;
		if (varsChanged) {
			this.hc.modify(variables);
			String post = hc.getHeader();

			Iterator<Map.Entry<String, String>> it = recordData
					.get("SensorDataIA").entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pair = (Map.Entry<String, String>) it
						.next();
				createFileService(pair.getKey(), "start", post);
			}
			this.varsChanged = false;
			records = 10;
		}

		Iterator<Map.Entry<String, String>> itSD = recordData.get("SensorData")
				.entrySet().iterator();
		while (itSD.hasNext()) {
			Map.Entry<String, String> pairSD = (Map.Entry<String, String>) itSD
					.next();
			if (this.variables.contains(pairSD.getKey())) {
				fc.composeRecord(pairSD.getKey(), pairSD.getValue());
			}
		}

		Iterator<Map.Entry<String, String>> itIA = recordData
				.get("SensorDataIA").entrySet().iterator();
		while (itIA.hasNext()) {
			Map.Entry<String, String> pairIA = (Map.Entry<String, String>) itIA
					.next();
			String iaFac = "";

			if (pairIA.getValue().equals("false")) {
				iaFac = "0";
			} else {
				iaFac = "1";
			}
			fc.composeRecord("IAFactorCr", iaFac);
			
			String record = fc.getRecord();

			for (int i = 0; i < records; i++) {
				createFileService(pairIA.getKey(), "append", record);
			}
		}
		fc.clean();
	}

	public void addSensorData(SensorData sensorData)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		DataAccess da = new DataAccess();
		String data = sensorData.getTimeStamp() + "," + sensorData.varName
				+ "," + sensorData.getVarValue();
		da.insertRecord("SensorData_" + sensorData.varName, data);
	}

	public void addSensorDataIAFactor(SensorDataIAFactor sdIA)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		DataAccess da = new DataAccess();
		String data = sdIA.getTimeStamp() + "," + sdIA.getCrId() + ","
				+ String.valueOf(sdIA.crIA);
		da.insertRecord("SensorDataIAFactor_" + sdIA.getCrId(), data);

	}

	public String createFileService(String crId, String state, String data) {
		HttpConnection connection = null;
		InputStream is = null;
		OutputStream os = null;

		String out = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			connection = (HttpConnection) Connector
					.open("http://localhost:8080/wekaREST/create/file/" + crId
							+ "/" + state);
			connection.setRequestMethod(HttpConnection.POST);
			connection.setRequestProperty("User-Agent",
					"Profile/MEEP-8.0 Confirguration/CLDC-1.8");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					String.valueOf(data.getBytes().length));

			os = connection.openDataOutputStream();
			os.write(data.getBytes());

			out = connection.getResponseMessage();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
					bos = null;
				}

				if (os != null) {
					os.close();
					os = null;
				}

				if (is != null) {
					is.close();
					is = null;
				}

				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			System.gc();
		}
		return out;
	}

	public void createEntity(String tableName) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		DataAccess da = new DataAccess();
		da.createTable(tableName);
	}

	public void printKBEntities() {
		// DataAccess da = new DataAccess();
		// List<String> output = new ArrayList<String>();
		// output = da.selectAll("SensorData");
		// int i=0;
		// while(i<output.size()) {
		// 	// print!
		// 	i++;
		// }
		// output = da.selectAll("SensorDataIAFactor");
		// i=0;
		// while(i<output.size()) {
		// 	// print!
		// 	i++;
		// }
	}

	public List<String> getAllSensorDataIAFactor() {
		DataAccess da = new DataAccess();
		List<String> out = null; 
		// da.selectAll("SensorDataIAFactor");
		return out;
	}

	public List<String> getAllSensorData() {
		DataAccess da = new DataAccess();
		List<String> out = null; 
		// da.selectAll("SensorData");
		return out;
	}

	public RecordStore getRecordStoreSensorDataIAFactor(String crId) {
		DataAccess da = new DataAccess();
		RecordStore out = da.getRecordStore("SensorDataIAFactor_" + crId);
		return out;
	}

	public RecordStore[] getRecordStoreSensorData(List<String> variables) {
		DataAccess da = new DataAccess();
		Iterator<String> it = variables.iterator();
		RecordStore[] out = new RecordStore[variables.size()];
		int i = 0;
		while (it.hasNext()) {
			out[i] = da.getRecordStore("SensorData_" + it.next());
			i++;
		}
		return out;
	}
}
