package edu.thu.keg.mdap.restful;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

/**
 * the functions of dataset's put operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsu")
public class DsPutFunctions {
	@Path("/upds/{datasetname/{datasetname2}}")
	public Response updateDatasetInto(@PathParam("datasetname") String dataset,
			@PathParam("datasetname2") String dataset2, JSONObject JContent) {
		return null;
	}
}
