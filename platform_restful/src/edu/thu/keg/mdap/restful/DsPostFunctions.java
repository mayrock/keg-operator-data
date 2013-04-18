package edu.thu.keg.mdap.restful;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.json.impl.provider.entity.JSONObjectProvider;

import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.provider.DataProvider;

/**
 * the functions of dataset's post operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsp")
public class DsPostFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;

	@POST
	@Path("/adds/{connstr}/{datasetname}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createDataset(@PathParam("connstr") String connstr,
			@PathParam("datasetname") String dataset, JSONObject JContent) {
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataProvider provider = p.getDataProviderManager()
					.getDefaultSQLProvider(connstr);
			if ((provider == null)) {
				return Response.status(409).entity("provider not found!\n")
						.build();
			}
			String ds_description = "";
			if (JContent.has("description"))
				ds_description = JContent.getString("description");
			boolean loadable = false;
			if (JContent.has("loadable"))
				loadable = JContent.getBoolean("loadable");
			JSONArray datafields = JContent.getJSONArray("dsFields");
			String fieldname = null;
			FieldType fieldtype = null;
			String description = null;
			boolean isKey = false;
			DataField[] fields = new DataField[datafields.length()];
			for (int i = 0; i < datafields.length(); i++) {
				JSONObject field = datafields.getJSONObject(i);
				fieldname = field.getString("fieldName");
				fieldtype = DataField.FieldType.parse(field
						.getString("fieldType"));
				description = field.getString("description");
				isKey = field.getBoolean("isKey");
				fields[i] = new GeneralDataField(fieldname, fieldtype,
						description, isKey);
			}
			p.getDataSetManager().createDataSet(dataset, ds_description,
					provider, fields, loadable);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.created(uriInfo.getAbsolutePath()).build();
	}
}
