package edu.smartvehicle.view.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** 
* @author Edith Zavala
*/

public class ActuatorsReader {
	
	public void readActuators(SmartVehicleViewReader svvr, String fileName) {
		/* Read a file and assign values to readings */
		FileInputStream file;
		XSSFWorkbook test;

		try {
			file = new FileInputStream(fileName);
			test = new XSSFWorkbook(file);
			XSSFSheet driverSensors = test.getSheetAt(0);
			Iterator<Row> driverSensorsIterator = driverSensors.iterator();

			driverSensorsIterator.next();
			while (driverSensorsIterator.hasNext()) {
				Row row = driverSensorsIterator.next();
				if (row.getCell(0) == null) {
					break;
				}
				svvr.getSupportLaneKeeping().add(row.getCell(1));
				svvr.getVibrationAlarm()
						.add(row.getCell(2));
				svvr.getSoundLightAlarm().add(
						row.getCell(3));
				svvr.getSupportLaneKeepingE().add(row.getCell(4));
				svvr.getVibrationAlarmE()
						.add(row.getCell(5));
				svvr.getSoundLightAlarmE().add(
						row.getCell(6));
			}
			test.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
