package edu.smartvehicle.view;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Properties;

import org.json.JSONObject;

import edu.smartVehicle.view.reader.SmartVehicleViewReader;

/** 
* @author Edith Zavala
*/

public class SVViewBridge extends Observable {

	private SmartVehicleViewReader svvr;
	private JSONObject readings;
	private JSONObject readingsFromFile;
	private int cycles;

	public SVViewBridge() {
		SmartVehicleViewClient svv = new SmartVehicleViewClient();
		this.readings = new JSONObject();
		this.cycles = 0;
		
		Properties prop = new Properties();
		InputStream is = getClass().getClassLoader()
				.getResourceAsStream("panelConfig.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeReader(prop.getProperty("driverMood"));
		this.addObserver(svv);
	}
	
	private void initializeReader( String driverMood){
		this.svvr = new SmartVehicleViewReader(this,
				driverMood);
	}
	
	private void finalizeReader(){
		this.svvr.destroy();
	}

	private void updateDisplay(String entity) {
		switch(entity){
		case "file":
		/* SV */
		this.readings.put("speed", 0);
		this.readings.put("accelerationRate", 0);
		this.readings.put("swheelRotation", 0);
		this.readings.put("wheelsRotation", 0);
		this.readings.put("deviationAngle", -1);
		this.readings.put("frontalDistanceRight", -1);
		this.readings.put("lateralDistanceRight", -1);
		this.readings.put("frontalDistanceLeft", -1);
		this.readings.put("lateralDistanceLeft", -1);

		/* Actuators */
		this.readings.put("seatVibration",
				readingsFromFile.get("vibrationAlarm").toString());
		this.readings.put("soundAlarm",
				readingsFromFile.get("soundLightAlarm").toString());
		this.readings.put("lightAlarm",
				readingsFromFile.get("soundLightAlarm").toString());
		this.readings.put("supportLaneKeeping",
				readingsFromFile.get("supportLaneKeeping").toString());

		this.readings.put("seatVibrationE",
				readingsFromFile.get("vibrationAlarmE").toString());
		this.readings.put("soundAlarmE",
				readingsFromFile.get("soundLightAlarmE").toString());
		this.readings.put("lightAlarmE",
				readingsFromFile.get("soundLightAlarmE").toString());
		this.readings.put("supportLaneKeepingE",
				readingsFromFile.get("supportLaneKeepingE").toString());

		/* Driver */
		this.readings.put("eyesState",
				this.readingsFromFile.get("eyesState"));
		this.readings.put("facePosition",
				this.readingsFromFile.get("facePosition"));
		this.readings.put("heartBeatsPerMinute",
				this.readingsFromFile.get("heartBeatsPerMinute"));
		this.readings
				.put("leftHand", this.readingsFromFile.get("leftHand"));
		this.readings.put("rightHand",
				this.readingsFromFile.get("rightHand"));
		break;
		case "sv":
			this.readings = new JSONObject(); 
			break;
		}
		setChanged();
		notifyObservers();
	}

	public void updateFromFile(JSONObject srsVal) {
		this.readingsFromFile = srsVal;
		updateDisplay("file");
	}

	public void updateFromSV(JSONObject readingsFromSV) throws InterruptedException {
		System.out.println(System.currentTimeMillis() + " " + cycles);
		this.cycles++;
		if (this.cycles == 500) { //execution duration
			updateDisplay("sv");
			System.out.println("SVViewBridge finishes");
			finalizeReader();
		}
	}

	public JSONObject getReadings() {
		return this.readings;
	}

	public int getCycles() {
		return this.cycles;
	}

	public static void main(String[] args) {
		System.out.println("SVViewBridge starts");
		try {
			SVViewBridge bridge = new SVViewBridge();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
