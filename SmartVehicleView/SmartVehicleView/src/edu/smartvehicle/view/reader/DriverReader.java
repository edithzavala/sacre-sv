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

public class DriverReader {

	public void readDriver(SmartVehicleViewReader svvr, String fileName) {
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
				svvr.getEyesState().add(row.getCell(1).getNumericCellValue());
				svvr.getFacePosition().add(row.getCell(2).getNumericCellValue());
				svvr.getHeartBeatsPerMinute().add(row.getCell(3)
						.getNumericCellValue());
				svvr.getLeftHand().add(row.getCell(4).getBooleanCellValue());
				svvr.getRightHand().add(row.getCell(5).getBooleanCellValue());
			}

			test.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
