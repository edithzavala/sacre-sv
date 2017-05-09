package edu.autonomic.beta.controller.managedElements.sv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import edu.autonomic.beta.controller.smartVehicleComponents.Wheel;

/** 
* @author Edith Zavala
*/

public class SmartVehicleClient {

	private SmartVehicle sm;
	private boolean leftHand;
	private boolean rightHand;

	public SmartVehicleClient(SmartVehicle sm) {
		this.sm = sm;
	}

	public boolean getSVData() {
		boolean thereIsData = false;
		String aux = getSVDataService();
		try {
			if (aux != null) {
				JSONObject svData = new JSONObject(aux);
				if (svData.length() != 0) {
					thereIsData = true;

					/***************************** Calculated by View *********************/
					sm.setSpeed(svData.get("speed"));
					sm.getLaneLeftCamera().setDeviationAngle(
							svData.get("deviationAngle"));
					sm.getLaneLeftCamera().setFrontalDistance(
							svData.get("frontalDistanceLeft"));
					sm.getLaneLeftCamera().setLateralDistance(
							svData.get("lateralDistanceLeft"));

					sm.getLaneRightCamera().setDeviationAngle(
							svData.getString("deviationAngle"));
					sm.getLaneRightCamera().setFrontalDistance(
							svData.getString("frontalDistanceRight"));
					sm.getLaneRightCamera().setLateralDistance(
							svData.getString("lateralDistanceRight"));
					/***********************************************************************/

					/************************** From Auto and Driver **********************************/
					sm.setAccelerationRate(svData.get("accelerationRate"));
					sm.getSwheel().setAngle(svData.get("swheelRotation"));

					sm.getSunVisorCamera()
							.setEyesState(svData.get("eyesState"));
					sm.getSunVisorCamera().setFaceposition(
							svData.get("facePosition"));

					sm.getdECG().setHbpm(svData.get("heartBeatsPerMinute"));

					this.leftHand = Boolean.parseBoolean(svData.get("leftHand")
							.toString());
					this.rightHand = Boolean.parseBoolean(svData.get(
							"rightHand").toString());

					if (this.leftHand && this.rightHand) {
						sm.getPressureSWheel().setHandsOn(new Integer(2));
					} else if (this.leftHand || this.rightHand) {
						sm.getPressureSWheel().setHandsOn(new Integer(1));
					} else {
						sm.getPressureSWheel().setHandsOn(new Integer(0));
					}
					/***********************************************************************/

					/*************** Behaviors changed or not by driver **********************/
					sm.getdSeat().setState(svData.get("seatVibration"));
					sm.getAlarmBeep().setState(svData.get("soundAlarm"));
					sm.getAlarmLED().setState(svData.get("lightAlarm"));
					sm.setLaneKeepingSupport(svData.get("supportLaneKeeping"));
					sm.getAlarmBeep().setEnable(svData.get("soundAlarmE"));
					sm.getAlarmLED().setEnable(svData.get("lightAlarmE"));
					sm.getdSeat().setEnable(svData.get("seatVibrationE"));
					sm.setEnableSLK(svData.get("supportLaneKeepingE"));
					/***********************************************************************/
				} else {
					thereIsData = false;
				}
			} else {
				thereIsData = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return thereIsData;
	}

	private String getSVDataService() {
		HttpConnection connection = null;
		InputStream is = null;
		String out = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			connection = (HttpConnection) Connector.open(
					"http://localhost:8080/viewService/sensorsValues/get/view",
					Connector.READ);
			connection.setRequestMethod(HttpConnection.GET);
			connection.setRequestProperty("User-Agent",
					"Profile/MEEP-8.0 Confirguration/CLDC-1.8");
			if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
				is = connection.openInputStream();

				if (is != null) {
					int ch = -1;

					while ((ch = is.read()) != -1) {
						bos.write(ch);
					}
				}
				out = bos.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
					bos = null;
				}

				if (is != null) {
					is.close();
					is = null;
				}
				if (connection != null) {
					connection.close();
					connection = null;
				}
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out;
	}

	public void setSVData() throws JSONException {
		JSONObject svData = new JSONObject();

		svData.put("client", "controller");

		svData.put("accelerationRate", new Double(sm.getAccelerationRate()));
		svData.put("swheelRotation", new Double(sm.getSwheel().getAngle()));

		svData.put("eyesState",
				new Double(sm.getSunVisorCamera().getValue("eyesState")
						.toString()));
		svData.put("facePosition",
				sm.getSunVisorCamera().getValue("facePosition"));
		svData.put("heartBeatsPerMinute",
				sm.getdECG().getValue("heartBeatsPerMinute"));
		svData.put("leftHand", this.leftHand);
		svData.put("rightHand", this.rightHand);

		svData.put("seatVibration", sm.getdSeat().isVibrating());
		svData.put("soundAlarm", sm.getAlarmBeep().getState());
		svData.put("lightAlarm", sm.getAlarmLED().getState());
		svData.put("supportLaneKeeping", sm.getLaneKeepingSupport());

		setSVDataService(svData.toString());

	}

	private void setSVDataService(String data) {
		HttpConnection connection = null;
		OutputStream os = null;

		try {

			connection = (HttpConnection) Connector.open(
					"http://localhost:8080/viewService/sensorsValues/post",
					Connector.READ_WRITE);
			connection.setRequestMethod(HttpConnection.POST);
			connection.setRequestProperty("User-Agent",
					"Profile/MEEP-8.0 Confirguration/CLDC-1.8");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-length",
					String.valueOf(data.getBytes().length));

			os = connection.openDataOutputStream();
			os.write(data.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
					os = null;
				}
				if (connection != null) {
					connection.close();
					connection = null;
				}
				System.gc();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
