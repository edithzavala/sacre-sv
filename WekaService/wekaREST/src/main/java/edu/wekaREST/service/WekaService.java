package edu.wekaREST.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import weka.classifiers.Evaluation;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.JRip.RipperRule;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/** 
* @author Edith Zavala
*/

@Path("/")
public class WekaService {

	@POST
	@Path("create/file/{param1}/{param2}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createFile(@PathParam("param1") String crId,
			@PathParam("param2") String state, String contentFile)
			throws Exception {
		System.out.println(System.currentTimeMillis() + " DEBUG - "
				+ this.getClass() + " - Receives POST");
		createArffFile(crId, state, URLDecoder.decode(contentFile, "UTF-8"));
		return Response.status(200).build();

	}

	private void createArffFile(String crId, String state, String content) {
		String filePath = ".../sensorData" + crId + ".arff";
		boolean action;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			if (state.equals("start")) {
				action = false;
			} else {
				/* append */
				action = true;
			}

			FileWriter fileWritter = new FileWriter(file, action);
			BufferedWriter output = new BufferedWriter(fileWritter);
			output.write(content);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("analyze/{param1}/{param2}")
	public Response getDM(@PathParam("param1") String crId,
			@PathParam("param2") String algorithm) throws Exception {
		System.out.println(System.currentTimeMillis() + " DEBUG - "
				+ this.getClass() + " - Receives GET");
		JSONObject jsonResponse = runWeka(algorithm, crId);
		return Response.status(200).entity(jsonResponse.toString()).build();

	}

	public JSONObject runWeka(String algorithm, String crId) throws Exception {
		JSONObject jsonResponse = new JSONObject();

		String rootFile = ".../sensorData" + crId + ".arff";
		DataSource source = new DataSource(rootFile);
		Instances dataset = source.getDataSet();
		dataset.setClassIndex(dataset.numAttributes() - 1);

		// create classifier/algorithm
		if (algorithm.equals("JRip")) {
			JRip jr = new JRip();
			
			jr.buildClassifier(dataset);

			/************************* 10-fold Cross-validation ************************/
			int folds = 10;
			int seed = 1;
			Random rand = new Random(seed);
			Evaluation eval = new Evaluation(dataset);
			eval.crossValidateModel(jr, dataset, folds, rand);
			/******************************************************************/
			
			FastVector fv = jr.getRuleset();
			JSONArray ja = new JSONArray();
			
			for (int i = 0; i < fv.size(); i++) {
				ja.put(((RipperRule) (fv.elementAt(i))).toString(dataset
						.classAttribute()));
			}
			jsonResponse.put("Rules", ja);
			jsonResponse.put("ErrorRate", eval.errorRate());
			jsonResponse.put("Precision", eval.precision(0));
			jsonResponse.put("Recall", eval.recall(0));
			jsonResponse.put("fMeasure", eval.fMeasure(0));
			System.out.println(System.currentTimeMillis() + " DEBUG - "
					+ this.getClass() + " - Puts results in JSON");
		}
		return jsonResponse;
	}
}
