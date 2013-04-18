package edu.thu.keg.mdap.restful;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * the functions of dataset's put operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsu")
public class DsPutFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;

	@PUT
	@Path("/upds/{datasetname}/{datasetname2}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDatasetInto(@PathParam("datasetname") String dataset,
			@PathParam("datasetname2") String dataset2, JSONObject jcontent) {
		try {
			String a = (String) jcontent.get("name");

			System.out.println("½øÀ´ÁË" + "updateDatasetInto" + " " + a);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.created(uriInfo.getAbsolutePath()).build();
	}
}
