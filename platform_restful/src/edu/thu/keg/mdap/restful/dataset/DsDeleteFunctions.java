package edu.thu.keg.mdap.restful.dataset;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONObject;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * the functions of dataset's delete operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsd")
public class DsDeleteFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;

	@DELETE
	@Path("/rmds/{datasetname}")
	public void removeDataset(@PathParam("datasetname") String dataset) {
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			datasetManager.removeDataSet(datasetManager.getDataSet(dataset));
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
