package edu.autonomic.beta.controller.dataMining;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import edu.autonomic.beta.controller.dataMining.adaptation.IVarAdaptation;
import edu.autonomic.beta.controller.documents.IPolicy;
import edu.autonomic.beta.controller.documentsImp.policies.WekaPolicy;
import edu.autonomic.beta.controller.mapekImp.KBSV;

/** 
* @author Edith Zavala
*/

public class Weka implements Tool {
	private WekaPolicy wp;

	@Override
	public void setPolicy(IPolicy p) {
		wp = (WekaPolicy) p;
	}

	@Override
	public IPolicy getPolicy() {
		return this.wp;
	}

	@Override
	public String runTool(String crId, String algorithm,
			List<String> variables, KBSV dataLocation)
			throws UnsupportedEncodingException {
		String out = analyzeDataService(algorithm, crId);
		return out;
	}

	public String analyzeDataService(String algorithm, String crId) {
		HttpConnection connection = null;
		InputStream is = null;
		String out = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			connection = (HttpConnection) Connector.open(
					"http://localhost:8080/wekaREST/analyze/" + crId + "/"
							+ algorithm, Connector.READ);
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
		} catch (Exception e) {
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			System.gc();
		}
		return out;
	}

}
