package edu.svviewservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

/** 
* @author Edith Zavala
*/

@Path("/sensorsValues")
public class SVViewService {

	@GET
	@Path("/get/view")
	public Response getMsg() throws Exception {
		return Response.status(200).entity(SensorsData.getSensorsView().toString()).build();
	}
	
	@GET
	@Path("/get/controller")
	public Response getMsg2() throws Exception {
		return Response.status(200).entity(SensorsData.getSensorsController().toString()).build();
	}

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postMsg(String srsValue)
			throws Exception {
		JSONObject aux = new JSONObject(srsValue);
		SensorsData.setSensors(aux);
		return Response.status(200).build();

	}
}
