package edu.smartvehicle.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONObject;

/** 
* @author Edith Zavala
*/

public class SmartVehicleViewClient implements Observer {

	private JSONObject srsVal;
	private SVViewBridge bridge;

	public SmartVehicleViewClient() {
		this.srsVal = new JSONObject();
	}

	public JSONObject getSrsVal() {
		return this.srsVal;
	}

	private JSONObject getSensorData() throws Exception {
		String url = "http://localhost:8080/viewService/sensorsValues/get/controller";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int respCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer resp = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			resp.append(inputLine);
		}
		in.close();

		return new JSONObject(resp.toString());
	}

	private void postSensorData() throws Exception {
		String url = "http://localhost:8080/viewService/sensorsValues/post";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		String urlParameters = getSrsVal().toString();

		con.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(urlParameters);
		wr.flush();
		wr.close();

		int respCode = con.getResponseCode();

		con.disconnect();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.bridge = ((SVViewBridge) arg0);
		srsVal = bridge.getReadings();
		srsVal.put("client", "view");
		try {
			postSensorData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (srsVal.length() != 0) {
			try {
				bridge.updateFromSV(srsVal);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
