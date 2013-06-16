package edu.thu.keg.mdap.restful.dataset;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * the functions of dataset's administrator operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsa")
public class DsAdFunctions {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;
	@Context
	HttpServletRequest httpServletRequest;
	private static Logger log = Logger.getLogger(DsAdFunctions.class);

	/**
	 * create a new dataset
	 * 
	 * @param connstr
	 * @param dataset
	 * @param JContent
	 * @return
	 */
	@POST
	@Path("/addds")
	@Consumes({ MediaType.APPLICATION_JSON })
	// @Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public Response createDataset(@FormParam("connstr") String connstr,
			@FormParam("dataset") String dataset,
			@FormParam("description") String description,
			@FormParam("loadable") boolean loadable,
			@FormParam("owner") String owner,
			@FormParam("dsfields") JSONArray datafields) {
		/**
		 * description 数据集描述 loadable 是否可以加载 dsFields 数据域的jsonarray fieldName
		 * fieldType description isKey
		 */
		log.info(uriInfo.getAbsolutePath());
		try {

			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataProvider provider = p.getDataProviderManager()
					.getDefaultSQLProvider(connstr);
			if ((provider == null)) {
				return Response.status(409).entity("provider not found!\n")
						.build();
			}
			// JSONArray datafields = JContent.getJSONArray("dsFields");
			String fieldname = null;
			FieldType fieldtype = null;
			String df_description = null;
			boolean isKey = false;
			boolean allowNull = false;
			boolean isDim = false;
			String func;
			DataField[] fields = new DataField[datafields.length()];
			for (int i = 0; i < datafields.length(); i++) {
				JSONObject field = datafields.getJSONObject(i);
				fieldname = field.getString("fieldname");
				fieldtype = DataField.FieldType.parse(field
						.getString("fieldtype"));
				df_description = field.getString("description");
				isKey = field.getBoolean("iskey");
				allowNull = field.getBoolean("allownull");
				isDim = field.getBoolean("isdim");
				func = field.getString("func");
				fields[i] = new GeneralDataField(fieldname, fieldtype,
						df_description, isKey, allowNull, isDim,
						FieldFunctionality.parse(func));
			}
			p.getDataSetManager().createDataSet(dataset, owner, description,
					provider, loadable, fields);
			p.getDataSetManager().saveChanges();
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			log.warn(e.getMessage());
		}
		return Response.status(Status.OK).build();
		// return Response.created(uriInfo.getAbsolutePath()).build();
	}

	@POST
	@Path("/adddv")
	@Consumes({ MediaType.APPLICATION_JSON })
	// // @Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public void createDataview(@FormParam("dataset") String dataset,
			@FormParam("dataview") String dataview,
			@FormParam("description") String description,
			@FormParam("datafeaturetype") String datafuturetype,
			@FormParam("key") String key, @FormParam("values") JSONArray values) {
		log.info(uriInfo.getAbsolutePath());
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataView dv = null;
			DataSet ds = null;
			DataField[] vs = null;
			Query q = null;

			ds = p.getDataSetManager().getDataSet(dataset);
			vs = new DataField[values.length()];
			for (int i = 0; i < vs.length; i++) {
				vs[i] = ds.getField(values.getJSONObject(i).getString("valuse"));
			}
			q = ds.getQuery().select();
			dv = p.getDataSetManager().defineView(dataview, description,
					DataFeatureType.ValueFeature, q, ds.getField(key), vs);
			p.getDataSetManager().saveChanges();
		} catch (OperationNotSupportedException | DataProviderException
				| IOException | JSONException e) {
			// TODO Auto-generated catch block
			log.warn(e.getMessage());
		}

		// return Response.status(Status.OK).build();
		//
		// // return Response.created(uriInfo.getAbsolutePath()).build();
	}
}
