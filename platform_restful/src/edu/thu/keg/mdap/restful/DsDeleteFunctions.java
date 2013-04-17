package edu.thu.keg.mdap.restful;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

/**
 * the functions of dataset's delete operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsd")
public class DsDeleteFunctions {
	@Path("/rmds/{datasetname}")
	public Response removeDataset(@PathParam("datasetname") String dataset,
			JSONObject JContent) {
		return null;
	}
}
