package edu.thu.keg.mdap.restful;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.json.impl.provider.entity.JSONObjectProvider;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.datamodel.Query.Operator;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.restful.jerseyclasses.JDataset;
import edu.thu.keg.mdap.restful.jerseyclasses.JField;

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

	/**
	 * create a new dataset
	 * 
	 * @param connstr
	 * @param dataset
	 * @param JContent
	 * @return
	 */
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

	/**
	 * get a column form the dataset
	 * 
	 * @param dataset
	 * @param fieldname
	 * @return
	 */
	@POST
	@Path("/getds/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<List<JField>> getDatasetField(
			@PathParam("datasetname") String dataset, JSONObject JContent) {
		JSONArray jsonFileds = null;
		String fieldname = null;
		List<List<JField>> all_dfs = null;
		System.out.println("POST");
		try {
			if (JContent.has("Fields"))
				jsonFileds = JContent.getJSONArray("Fields");
			else
				throw new JSONException("have not the this Field");
			all_dfs = new ArrayList<>();
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			List<JField> list_df = null;
			for (int i = 0; i < jsonFileds.length(); i++) {
				fieldname = (String) jsonFileds.get(i);
				System.out.println("getDatasetField " + dataset + " "
						+ fieldname + " " + uriInfo.getAbsolutePath());
				list_df = new ArrayList<JField>();
				DataField df = ds.getField(fieldname);
				DataContent rs = ds.getQuery();
				rs.open();
				while (rs.next()) {
					JField jf = new JField();
					jf.setField(rs.getValue(df));
					list_df.add(jf);
				}
				rs.close();
			}
			all_dfs.add(list_df);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return all_dfs;
	}

	/**
	 * get the clause result
	 * 
	 * @param dataset
	 * @param fieldname
	 * @param opr
	 * @param value
	 * @return
	 */
	@POST
	@Path("/getdsres/{datasetname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<JDataset> getDatasetValueOfOpr(
			@PathParam("datasetname") String dataset, JSONObject JContent) {
		String fieldname = null, opr = null, value = null;
		System.out.println("getDatasetValueOfOpr " + dataset + " " + fieldname
				+ " " + opr + " " + value + " " + uriInfo.getAbsolutePath());
		List<JDataset> datasetList = new ArrayList<>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery().where(ds.getField(fieldname),
					Operator.parse(opr), value);
			rs.open();
			while (rs.next()) {
				JDataset jdataset = new JDataset();
				List<JField> fields = new ArrayList<JField>();
				DataField[] dfs = ds.getDataFields();
				for (DataField df : dfs) {
					JField field = new JField();
					field.setField(rs.getValue(df));
					fields.add(field);
				}
				jdataset.setField(fields);
				datasetList.add(jdataset);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasetList;
	}

}
