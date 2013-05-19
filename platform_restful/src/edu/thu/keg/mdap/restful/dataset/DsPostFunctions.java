package edu.thu.keg.mdap.restful.dataset;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
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
import edu.thu.keg.mdap.restful.jerseyclasses.JColumn;
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
			@PathParam("datasetname") String dataset,
			@FormParam("description") String description,
			@FormParam("loadable") boolean loadable,
			@FormParam("owner") String owner,
			@FormParam("dsFields") JSONArray datafields) {
		/**
		 * description 数据集描述 loadable 是否可以加载 dsFields 数据域的jsonarray fieldName
		 * fieldType description isKey
		 */
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

	/**
	 * get a column form the dataset
	 * 
	 * @param dataset
	 * @param fieldname
	 * @return
	 */
	@POST
	@Path("/getds/{datasetname}")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONWithPadding getDatasetField(
			@PathParam("datasetname") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback,
			@FormParam("fields") JSONArray jsonFileds,
			@FormParam("orderby") String orderby) {

		String fieldname = null;
		List<JColumn> all_dfs = null;
		List<JField> list_df = null;
		System.out.println("POST");
		/**
		 * fields 存储列名的参数jsonarray orderby 排序的域名
		 */
		try {
			if (jsonFileds == null)
				throw new JSONException("have not the this Field");
			all_dfs = new ArrayList<>();
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs;
			if (orderby != null)
				rs = ds.getQuery().orderBy(orderby, Order.parse(orderby));
			else
				rs = ds.getQuery();
			for (int i = 0; i < jsonFileds.length(); i++) {
				fieldname = (String) jsonFileds.get(i);
				System.out.println("getDatasetField " + dataset + " "
						+ fieldname + " " + uriInfo.getAbsolutePath());
				list_df = new ArrayList<JField>();
				DataField df = ds.getField(fieldname);

				rs.open();
				int ii = 0;
				while (rs.next() && ii++ < 20) {
					JField jf = new JField();
					jf.setField(rs.getValue(df));
					list_df.add(jf);
				}
				rs.close();
				// ObjectMapper mapper = new ObjectMapper();
				// StringWriter sw = new StringWriter();
				// JsonGenerator gen = new
				// JsonFactory().createJsonGenerator(sw);
				// mapper.writeValue(gen, list_df);
				// gen.close();
				// String json = sw.toString();
				// System.out.println("json: "+json);
				JColumn jc = new JColumn();
				jc.setColumn(list_df);
				all_dfs.add(jc);
			}

		} catch (JSONException e) {
			System.out.println("POST: Json form wrong!");
			e.printStackTrace();
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		// return all_dfs;
		return new JSONWithPadding(new GenericEntity<List<JColumn>>(all_dfs) {
		}, callback);
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
	public JSONWithPadding getDatasetValueOfOpr(
			@PathParam("datasetname") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback,
			@FormParam("jsonoper") JSONArray jsonOper) {
		String fieldname = null;
		String opr = null;
		String value = null;
		List<JColumn> all_dfs = null;
		List<JField> list_df = null;
		/**
		 * jsonOper 操作参数jsonarray fieldname 域名 opr 操作符号 value 值
		 */
		try {
			if (jsonOper == null)
				throw new JSONException("have not the this Operation");
			all_dfs = new ArrayList<>();
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);

			for (int i = 0; i < jsonOper.length(); i++) {
				JSONObject job = (JSONObject) jsonOper.get(i);
				fieldname = job.getString("fieldname");
				opr = job.getString("opr");
				value = job.getString("value");
				System.out.println("getDatasetValueOfOpr " + dataset + " "
						+ fieldname + " " + opr + " " + value + " "
						+ uriInfo.getAbsolutePath());
				list_df = new ArrayList<JField>();
				DataField df = ds.getField(fieldname);
				DataContent rs = ds.getQuery().where(fieldname,
						Operator.parse(opr), value);
				rs.open();
				int ii = 0;
				while (rs.next() && ii++ < 2) {
					JField jf = new JField();
					jf.setField(rs.getValue(df));
					list_df.add(jf);
				}
				rs.close();
				JColumn jc = new JColumn();
				jc.setColumn(list_df);
				all_dfs.add(jc);
			}
		} catch (OperationNotSupportedException | DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("POST: Json form wrong!");
			e.printStackTrace();
		}
		return new JSONWithPadding(new GenericEntity<List<JColumn>>(all_dfs) {
		}, callback);
	}

}
