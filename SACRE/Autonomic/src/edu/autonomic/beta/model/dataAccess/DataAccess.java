package edu.autonomic.beta.model.dataAccess;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.rms.*;

/** 
* @author Edith Zavala
*/

public class DataAccess {

	public void createTable(String dataStoreName)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(dataStoreName, true);
			rs.closeRecordStore();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void deleteTable(String dataStoreName)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		try {
			RecordStore.deleteRecordStore(dataStoreName);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String selectRecord(String dataStoreName, String record)
			throws UnsupportedEncodingException {
		String aux = null;
		byte[] dataAux;
		RecordStore rs = null;
		int idx = -1;
		boolean found = false;
		try {
			rs = RecordStore.openRecordStore(dataStoreName, false);
			RecordEnumeration re = rs.enumerateRecords(null, null, false);
			while (re.hasNextElement() && !found) {
				idx = re.nextRecordId();
				dataAux = rs.getRecord(idx);
				aux = new String(dataAux, "UTF-8");
				if (!aux.equals(record)) {
					aux = null;
				} else {
					found = true;
				}
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			System.out.println(e);
		}

		return aux;
	}

	public void insertRecord(String dataStoreName, String data)
			throws RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(dataStoreName, false);
			rs.addRecord(data.getBytes(), 0, data.length());
			rs.closeRecordStore();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void updateRecord(String dataStoreName, String record, String newData) {
		String aux = null;
		byte[] dataAux;
		RecordStore rs = null;
		int idx = -1;
		boolean found = false;
		try {
			rs = RecordStore.openRecordStore(dataStoreName, false);
			RecordEnumeration re = rs.enumerateRecords(null, null, false);
			while (re.hasNextElement() && !found) {
				idx = re.nextRecordId();
				dataAux = rs.getRecord(idx);
				aux = new String(dataAux, "UTF-8");
				if (aux.equals(record)) {
					found = true;
				}
			}
			if (found) {
				rs.setRecord(idx, newData.getBytes(), 0, newData.length());
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void deleteRecord(String dataStoreName, String record) {
		String aux = null;
		byte[] dataAux;
		RecordStore rs = null;
		boolean found = false;
		int idx = -1;
		try {
			rs = RecordStore.openRecordStore(dataStoreName, false);
			RecordEnumeration re = rs.enumerateRecords(null, null, false);
			while (re.hasNextElement()) {
				idx = re.nextRecordId();
				dataAux = rs.getRecord(idx);
				aux = new String(dataAux, "UTF-8");
				if (aux.equals(record)) {
					found = true;
				}
			}
			if (found) {
				rs.deleteRecord(idx);
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void deleteAll(){
		String[] allRS = RecordStore.listRecordStores();
		if(allRS!=null){
			for(int i=0; i<allRS.length;i++){
				try {
					RecordStore.deleteRecordStore(allRS[i]);
				} catch (RecordStoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<String> selectAll(String dataStoreName) {
		RecordStore rs = null;
		List<String> records = new ArrayList<String>();
		int i = 0;
		try {
			rs = RecordStore.openRecordStore(dataStoreName, false);
			RecordEnumeration re = rs.enumerateRecords(null, null, false);
			int idx;
			while (re.hasNextElement()) {
				idx = re.nextRecordId();
				records.add(new String(rs.getRecord(idx), "UTF-8"));
				i++;
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}

	public RecordStore getRecordStore(String dataStoreName) {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(dataStoreName, false);
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		return rs;
	}
}
