package edu.thu.keg.mdap.restful.dataset;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.datamodel.Query.Operator;
import edu.thu.keg.mdap.datamodel.Query.Order;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JColumn;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JField;

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
	@Context
	HttpServletRequest httpServletRequest;
	private static Logger log = Logger.getLogger(DsPostFunctions.class);

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
	public Response createDataset(@FormParam("connstr") String connstr,
			@FormParam("dataset") String dataset,
			@FormParam("description") String description,
			@FormParam("loadable") boolean loadable,
			@FormParam("owner") String owner,
			@FormParam("dsFields") JSONArray datafields) {
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
				fieldname = field.getString("fieldName");
				fieldtype = DataField.FieldType.parse(field
						.getString("fieldType"));
				df_description = field.getString("description");
				isKey = field.getBoolean("isKey");
				allowNull = field.getBoolean("allownull");
				isDim = field.getBoolean("isdim");
				func = field.getString("func");
				fields[i] = new GeneralDataField(fieldname, fieldtype,
						df_description, isKey, allowNull, isDim,
						FieldFunctionality.parse(func));
			}
			p.getDataSetManager().createDataSet(dataset, owner, description,
					provider, loadable, fields);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.created(uriInfo.getAbsolutePath()).build();
	}



}
