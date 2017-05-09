package edu.smartvehicle.view.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import edu.smartVehicle.view.SVViewBridge;
import edu.smartVehicle.view.SVViewWindow;

/** 
* @author Edith Zavala
*/

public class SmartVehicleViewReader {

	private JSONObject readings;
	private long interReadingsTime;
	private int index;

	private List<Object> eyesState;
	private List<Object> facePosition;
	private List<Object> heartBeatsPerMinute;
	private List<Object> leftHand;
	private List<Object> rightHand;

	private List<Object> supportLaneKeeping;
	private List<Object> vibrationAlarm;
	private List<Object> soundLightAlarm;
	private List<Object> supportLaneKeepingE;
	private List<Object> vibrationAlarmE;
	private List<Object> soundLightAlarmE;
	
	private SVViewWindow svvm;
	private SVViewBridge brigde;
	private Timer timer;
	private DriverReader dr;
	private ActuatorsReader ar;

	public SmartVehicleViewReader(SVViewWindow svvm, String driverMood) {
		this.svvm = svvm;
	}
	
	public SmartVehicleViewReader(SVViewBridge bridge, String driverMood) {
		Properties prop = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"panelConfig.properties");

		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		readings = new JSONObject();

		this.brigde = bridge;
		interReadingsTime = Long.parseLong(prop
				.getProperty("interReadingsTime"));
		index = 0;

		this.eyesState = new ArrayList<Object>();
		this.facePosition = new ArrayList<Object>();
		this.heartBeatsPerMinute = new ArrayList<Object>();
		this.rightHand = new ArrayList<Object>();
		this.leftHand = new ArrayList<Object>();
		
		this.supportLaneKeeping = new ArrayList<Object>();
		this.vibrationAlarm = new ArrayList<Object>();
		this.soundLightAlarm = new ArrayList<Object>();
		this.supportLaneKeepingE = new ArrayList<Object>();
		this.vibrationAlarmE = new ArrayList<Object>();
		this.soundLightAlarmE = new ArrayList<Object>();
		
		this.dr = new DriverReader();
		this.ar = new ActuatorsReader();
		
		dr.readDriver(this, prop.getProperty(driverMood));
		ar.readActuators(this, prop.getProperty(driverMood+"A"));

		this.timer = new Timer();
		this.timer.schedule(new Cycle(this), 0, interReadingsTime);
	}

	public void getNext() {
		readings.put("eyesState", eyesState.get(index));
		readings.put("facePosition", facePosition.get(index));
		readings.put("heartBeatsPerMinute", heartBeatsPerMinute.get(index));
		readings.put("leftHand", leftHand.get(index));
		readings.put("rightHand", rightHand.get(index));
		
		readings.put("supportLaneKeeping", supportLaneKeeping.get(index));
		readings.put("vibrationAlarm", vibrationAlarm.get(index));
		readings.put("soundLightAlarm", soundLightAlarm.get(index));
		readings.put("supportLaneKeepingE", supportLaneKeepingE.get(index));
		readings.put("vibrationAlarmE", vibrationAlarmE.get(index));
		readings.put("soundLightAlarmE", soundLightAlarmE.get(index));

		index++;
		if (index == 7500) { //re-use records
			index = 0;
		}

		brigde.updateFromFile(this.readings);
	}

	public void destroy() {
		this.timer.cancel();
		this.timer.purge();
	}

	public List<Object> getEyesState() {
		return eyesState;
	}

	public void setEyesState(List<Object> eyesState) {
		this.eyesState = eyesState;
	}

	public List<Object> getFacePosition() {
		return facePosition;
	}

	public void setFacePosition(List<Object> facePosition) {
		this.facePosition = facePosition;
	}

	public List<Object> getHeartBeatsPerMinute() {
		return heartBeatsPerMinute;
	}

	public void setHeartBeatsPerMinute(List<Object> heartBeatsPerMinute) {
		this.heartBeatsPerMinute = heartBeatsPerMinute;
	}

	public List<Object> getLeftHand() {
		return leftHand;
	}

	public void setLeftHand(List<Object> leftHand) {
		this.leftHand = leftHand;
	}

	public List<Object> getRightHand() {
		return rightHand;
	}

	public void setRightHand(List<Object> rightHand) {
		this.rightHand = rightHand;
	}
	
	public List<Object> getSupportLaneKeeping() {
		return supportLaneKeeping;
	}

	public void setSupportLaneKeeping(List<Object> supportLaneKeeping) {
		this.supportLaneKeeping = supportLaneKeeping;
	}
	public List<Object> getVibrationAlarm() {
		return vibrationAlarm;
	}

	public void setVibrationAlarm(List<Object> vibrationAlarm) {
		this.vibrationAlarm = vibrationAlarm;
	}
	public List<Object> getSoundLightAlarm() {
		return soundLightAlarm;
	}

	public void setSoundLightAlarm(List<Object> soundLightAlarm) {
		this.soundLightAlarm = soundLightAlarm;
	}

	public List<Object> getSupportLaneKeepingE() {
		return supportLaneKeepingE;
	}

	public void setSupportLaneKeepingE(List<Object> supportLaneKeepingE) {
		this.supportLaneKeepingE = supportLaneKeepingE;
	}
	public List<Object> getVibrationAlarmE() {
		return vibrationAlarmE;
	}

	public void setVibrationAlarmE(List<Object> vibrationAlarmE) {
		this.vibrationAlarmE = vibrationAlarmE;
	}
	public List<Object> getSoundLightAlarmE() {
		return soundLightAlarmE;
	}

	public void setSoundLightAlarmE(List<Object> soundLightAlarmE) {
		this.soundLightAlarmE = soundLightAlarmE;
	}

	class Cycle extends TimerTask {
		SmartVehicleViewReader svvp;

		public Cycle(SmartVehicleViewReader svvp) {
			this.svvp = svvp;
		}

		@Override
		public void run() {
			svvp.getNext();
		}
	}

	// private void readAuto(String fileName) {
	// System.out
	// .println("SmartVehicleViewPannel: loading automaticSV (readFile)");
	// /* Read a file and assign values to readings */
	// FileInputStream file;
	// XSSFWorkbook test;
	//
	// try {
	// file = new FileInputStream(fileName);
	// test = new XSSFWorkbook(file);
	//
	// XSSFSheet svAuto = test.getSheetAt(0);
	// Iterator<Row> svAutoIterator = svAuto.iterator();
	//
	// /* Titles */
	// svAutoIterator.next();
	//
	// while (svAutoIterator.hasNext()) {
	// Row row = svAutoIterator.next();
	// if (row.getCell(0) == null) {
	// // row.getCell(0).getNumericCellValue(); == Time
	// break;
	// }
	// this.accelerationRate.add(row.getCell(1).getNumericCellValue());
	// this.swheelRotation.add(row.getCell(2).getNumericCellValue());
	// }
	// test.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
