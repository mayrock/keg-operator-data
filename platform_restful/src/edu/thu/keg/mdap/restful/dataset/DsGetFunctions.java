package edu.thu.keg.mdap.restful.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datafeature.DataFeature;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.datamodel.Query.Operator;
import edu.thu.keg.mdap.datamodel.Query.Order;

import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JColumn;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDataset;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDatasetName;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JField;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JFieldName;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JGeograph;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JStatistic;

/**
 * the functions of dataset's get operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dsg")
public class DsGetFunctions {
	/**
	 * 
	 */
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ServletContext servletcontext;
	@Context
	HttpServletRequest httpServletRequest;
	private static Logger log = Logger.getLogger(DsGetFunctions.class);

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset names
	 */
	@GET
	@Path("/getdss")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDatasetsNames(
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		log.info(uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		JDatasetName datasetName = new JDatasetName();

		try {
			//
			// log.fatal("测试fatal");
			// log.error("测试error");
			// log.warn("测试warn");
			// log.info("测试info");
			// log.debug("测试debug");

			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager.getDataSetList();
			int i = 0;
			for (DataSet dataset : datasets) {
				// if(i++>=1)
				// break;
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				ArrayList<String> keys = new ArrayList<>();
				ArrayList<String> values = new ArrayList<>();
				for (DataField df : dataset.getKeyFields()) {
					keys.add(df.getName());
				}
				dname.setKeys(keys);
				for (DataField df : dataset.getValueFields()) {
					values.add(df.getName());
				}
				dname.setValues(values);
				datasetsName.add(dname);
			}

		} catch (Exception e) {
			log.warn(e.getStackTrace());
			// e.printStackTrace();
		}

		return new JSONWithPadding(new GenericEntity<List<JDatasetName>>(
				datasetsName) {
		}, jsoncallback);

	}

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset Geo names
	 */
	@GET
	@Path("/getgeodss")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getGeoDatasetsNames(
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getGeoDatasetsNames " + uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager
					.getDataSetList(DataFeatureType.GeoFeature);
			for (DataSet dataset : datasets) {
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				// ArrayList<String> schema = new ArrayList<>();
				// for (DataField df : dataset.getDataFields()) {
				// schema.add(df.getName());
				// }
				// dname.setSchema(schema);
				ArrayList<String> keys = new ArrayList<>();
				ArrayList<String> values = new ArrayList<>();
				for (DataField df : dataset.getKeyFields()) {
					keys.add(df.getName());
				}
				dname.setKeys(keys);
				for (DataField df : dataset.getValueFields()) {
					values.add(df.getName());
				}
				dname.setValues(values);
				datasetsName.add(dname);
			}

		} catch (Exception e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JDatasetName>>(
				datasetsName) {
		}, jsoncallback);
	}

	/**
	 * get all dataset names list
	 * 
	 * @return a list including dataset Sta names
	 */
	@GET
	@Path("/getstadss")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getStaDatasetsNames(
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getStaDatasetsNames " + uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataSet> datasets = datasetManager
					.getDataSetList(DataFeatureType.DistributionFeature);
			int i = 0;
			for (DataSet dataset : datasets) {
				// if(i++>=2)
				// break;
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				ArrayList<String> schema = new ArrayList<>();
				// for (DataField df : dataset.getDataFields()) {
				// schema.add(df.getName());
				// }
				// dname.setSchema(schema);
				ArrayList<String> keys = new ArrayList<>();
				ArrayList<String> values = new ArrayList<>();
				for (DataField df : dataset.getKeyFields()) {
					keys.add(df.getName());
				}
				dname.setKeys(keys);
				for (DataField df : dataset.getValueFields()) {
					values.add(df.getName());
				}
				dname.setValues(values);
				datasetsName.add(dname);
			}

		} catch (Exception e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JDatasetName>>(
				datasetsName) {
		}, jsoncallback);
	}

	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format all rs array of JDataset
	 */
	@GET
	@Path("/getds")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDataset(@QueryParam("dataset") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JDataset> datasetList = new ArrayList<>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
				JDataset jdataset = new JDataset();
				List<JField> fields = new ArrayList<>();
				DataField[] dfs = ds.getDataFields().toArray(new DataField[0]);
				int j = 0;
				for (DataField df : dfs) {
					// if(j++>=2)
					// break;
					JField field = new JField();
					field.setValue(rs.getValue(df).toString());
					field.setType(rs.getValue(df).getClass().getSimpleName());
					fields.add(field);
				}
				jdataset.setField(fields);
				datasetList.add(jdataset);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JDataset>>(
				datasetList) {
		}, jsoncallback);
	}
	
	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format location array
	 */
	@GET
	@Path("/getgeods")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getGeoDataset(
			@QueryParam("dataset") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getLocDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JGeograph> datasetList = new ArrayList<JGeograph>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
			DataFeature gds = ds.getFeature(DataFeatureType.GeoFeature);
			if (gds == null)
				throw new OperationNotSupportedException(
						"can't find the geograph Exception");
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
				JGeograph location = new JGeograph();
				location.setLatitude((double) rs.getValue(gds.getKeyFields()[0]));
				location.setLongitude((double) rs.getValue(gds.getKeyFields()[1]));
				datasetList.add(location);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JGeograph>>(
				datasetList) {
		}, jsoncallback);
	}

	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format statistics array
	 */
	@GET
	@Path("/getstads")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getStaDataset(
			@QueryParam("dataset") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getStaDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JStatistic> datasetList = new ArrayList<JStatistic>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
			DataFeature gds = ds
					.getFeature(DataFeatureType.DistributionFeature);
			if (gds == null)
				throw new OperationNotSupportedException(
						"can't find the statistic Exception");
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
				// System.out.println(rs.getValue(ds.getDataFields()[0])
				// .toString()
				// + " "
				// + rs.getValue(ds.getDataFields()[1]).toString());
				JStatistic statistic = new JStatistic();
				ArrayList<String> keys = new ArrayList<>();
				for (DataField key : gds.getKeyFields()) {
					keys.add(rs.getValue(key).toString());
				}
				ArrayList<Double> values = new ArrayList<>();
				for (DataField value : gds.getValueFields()) {
					values.add(Double.valueOf(rs.getValue(value).toString()));
				}
				statistic.setKey(keys);
				statistic.setValue(values);
				datasetList.add(statistic);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JStatistic>>(
				datasetList) {
		}, jsoncallback);
	}

	/**
	 * get the location fields form the dataset
	 * 
	 * @param dataset
	 * @return a json or xml format statistics array
	 */
	@GET
	@Path("/getstatds")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getStaTimeDataset(
			@QueryParam("dataset") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getStaTimeDataset " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JStatistic> datasetList = new ArrayList<JStatistic>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataContent rs = ds.getQuery();
			DataFeature gds = ds.getFeature(DataFeatureType.TimeSeriesFeature);
			// TimeSeriesFeature gds = (TimeSeriesFeature) ds
			// .getFeature(TimeSeriesFeature.class);
			if (gds == null)
				throw new OperationNotSupportedException(
						"can't find the statisticTime Exception");
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
				System.out.println(rs.getValue(ds.getDataFields().get(0))
						.toString()
						+ " "
						+ rs.getValue(ds.getDataFields().get(1)).toString());
				JStatistic statistic = new JStatistic();
				ArrayList<String> keys = new ArrayList<>();
				for (DataField key : gds.getKeyFields()) {
					keys.add(rs.getValue(key).toString());
				}
				ArrayList<Double> values = new ArrayList<>();
				for (DataField value : gds.getValueFields()) {
					values.add(Double.valueOf(rs.getValue(value).toString()));
				}
				statistic.setKey(keys);
				statistic.setValue(values);
				datasetList.add(statistic);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JStatistic>>(
				datasetList) {
		}, jsoncallback);
	}

	/**
	 * get all of the dataset's fields
	 * 
	 * @param dataset
	 * @return a list of all fields name
	 */
	@GET
	@Path("/getdsfds")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDatasetFieldsNames(
			@QueryParam("dataset") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getDatasetFieldsNames " + dataset + " "
				+ uriInfo.getAbsolutePath());
		List<JFieldName> all_fn = new ArrayList<JFieldName>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataSet ds = datasetManager.getDataSet(dataset);
			DataField[] dfs = ds.getDataFields().toArray(new DataField[0]);
			for (DataField df : dfs) {
				JFieldName jfn = new JFieldName();
				jfn.setFieldName(df.getName());
				jfn.setDescription(df.getDescription());
				jfn.setIsKey(df.isKey());
				jfn.setType(df.getFieldType().name());
				all_fn.add(jfn);
			}

		} catch (Exception e) {
			log.warn(e.getStackTrace());
		} finally {

		}
		return new JSONWithPadding(new GenericEntity<List<JFieldName>>(all_fn) {
		}, jsoncallback);
	}

	/**
	 * get all dataviews
	 * 
	 * @param jsoncallback
	 * @return
	 */
	@GET
	@Path("/getdvs")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDatasetViewsNames(
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getDatasetViewsNames " + uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		JDatasetName datasetName = new JDatasetName();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataView> datasets = datasetManager.getDataViewList();
			int i = 0;
			for (DataView dataset : datasets) {
				// if(i++>=1)
				// break;
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				ArrayList<String> schema = new ArrayList<>();
				// for (DataField df : dataset.getAllFields()) {
				// schema.add(df.getName());
				// }
				// dname.setSchema(schema);
				ArrayList<String> keys = new ArrayList<>();
				ArrayList<String> values = new ArrayList<>();
				for (DataField df : dataset.getKeyFields()) {
					keys.add(df.getName());
				}
				dname.setKeys(keys);
				for (DataField df : dataset.getValueFields()) {
					values.add(df.getName());
				}
				dname.setValues(values);
				datasetsName.add(dname);
			}
		} catch (Exception e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JDatasetName>>(
				datasetsName) {
		}, jsoncallback);

	}

	/**
	 * get Statistic dataview
	 * 
	 * @param jsoncallback
	 * @return
	 */
	@GET
	@Path("/getstadvs")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getStaDatasetViewsNames(
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getStaDatasetViewsNames "
				+ uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		JDatasetName datasetName = new JDatasetName();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataView> datasets = datasetManager.getDataViewList();
			int i = 0;
			for (DataView dataset : datasets) {
				// if(i++>=1)
				// break;
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				ArrayList<String> schema = new ArrayList<>();
				// for (DataField df : dataset.getAllFields()) {
				// schema.add(df.getName());
				// }
				// dname.setSchema(schema);
				ArrayList<String> keys = new ArrayList<>();
				ArrayList<String> values = new ArrayList<>();
				for (DataField df : dataset.getKeyFields()) {
					keys.add(df.getName());
				}
				dname.setKeys(keys);
				for (DataField df : dataset.getValueFields()) {
					values.add(df.getName());
				}
				dname.setValues(values);
				datasetsName.add(dname);
			}
		} catch (Exception e) {
			log.warn(e.getStackTrace());
		}

		return new JSONWithPadding(new GenericEntity<List<JDatasetName>>(
				datasetsName) {
		}, jsoncallback);

	}

	/**
	 * get all geo dataviews
	 * 
	 * @param jsoncallback
	 * @return
	 */
	@GET
	@Path("/getgeodvs")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getGeoDatasetViewsNames(
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getGeoDatasetViewsNames "
				+ uriInfo.getAbsolutePath());
		List<JDatasetName> datasetsName = new ArrayList<JDatasetName>();
		JDatasetName datasetName = new JDatasetName();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			Collection<DataView> datasets = datasetManager
					.getDataViewList(DataFeatureType.GeoFeature);
			int i = 0;
			for (DataView dataset : datasets) {
				// if(i++>=1)
				// break;
				JDatasetName dname = new JDatasetName();
				dname.setDatasetName(dataset.getName());
				dname.setDescription(dataset.getDescription());
				// ArrayList<String> schema = new ArrayList<>();
				// for (DataField df : dataset.getAllFields()) {
				// schema.add(df.getName());
				// }
				// dname.setSchema(schema);
				ArrayList<String> keys = new ArrayList<>();
				ArrayList<String> values = new ArrayList<>();
				for (DataField df : dataset.getKeyFields()) {
					keys.add(df.getName());
				}
				dname.setKeys(keys);
				for (DataField df : dataset.getValueFields()) {
					values.add(df.getName());
				}
				dname.setValues(values);
				datasetsName.add(dname);
			}
		} catch (Exception e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JDatasetName>>(
				datasetsName) {
		}, jsoncallback);

	}



	@GET
	@Path("/getdv")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDataview(
			@QueryParam("dataview") String dataview,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getDataset " + dataview + " "
				+ uriInfo.getAbsolutePath());
		List<JDataset> datasetList = new ArrayList<>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataView dv = datasetManager.getDataView(dataview);
			DataContent rs = dv.getQuery();
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
				JDataset jdataset = new JDataset();
				List<JField> fields = new ArrayList<>();
				DataField[] dfs = dv.getAllFields();
				int j = 0;
				for (DataField df : dfs) {
					// if(j++>=2)
					// break;
					JField field = new JField();
					field.setValue(rs.getValue(df).toString());
					field.setType(rs.getValue(df).getClass().getSimpleName());
					fields.add(field);
				}
				jdataset.setField(fields);
				datasetList.add(jdataset);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			log.warn(e.getStackTrace());
		}
		return new JSONWithPadding(new GenericEntity<List<JDataset>>(
				datasetList) {
		}, jsoncallback);
	}
	/**
	 * get a column form the dataset
	 * 
	 * @param dataset
	 * @param fieldname
	 * @return
	 */
	@GET
	@Path("/getdscs")
	@Produces({ MediaType.APPLICATION_JSON })
	public JSONWithPadding getDatasetField(
			@QueryParam("dataset") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback,
			@QueryParam("fields") JSONArray jsonFileds,
			@QueryParam("orderby") String orderby) {
		log.info(uriInfo.getAbsolutePath());
		String fieldname = null;
		List<JColumn> all_dfs = null;
		List<JField> list_df = null;
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
					JField field = new JField();
					field.setValue(rs.getValue(df).toString());
					field.setType(rs.getValue(df).getClass().getSimpleName());
					list_df.add(field);
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
	@Path("/getdsres")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public JSONWithPadding getDatasetValueOfOpr(
			@QueryParam("dataset") String dataset,
			@QueryParam("jsoncallback") @DefaultValue("fn") String callback,
			@QueryParam("jsonoper") JSONArray jsonOper) {
		log.info(uriInfo.getAbsolutePath());
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
					JField field = new JField();
					field.setValue(rs.getValue(df).toString());
					field.setType(rs.getValue(df).getClass().getSimpleName());
					list_df.add(field);
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
	@GET
	@Path("/hello")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getString() {
		String a = "{\"city\":\"helloworld_json\"}";
		System.out.println(a);
		return a;
	}

	@GET
	@Path("/hello")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getString2() {
		System.out.println("{helloworld_json}");
		return "{helloworld_json}";
	}

	@GET
	@Path("/jsonp")
	@Produces("application/x-javascript")
	@Consumes({ MediaType.APPLICATION_JSON })
	public JSONWithPadding readAllP(
			@QueryParam("jsoncallback") String jsoncallback,
			@QueryParam("acronym") String acronym,
			@QueryParam("title") String title,
			@QueryParam("competition") String competition) {
		String a = "{\"city\":\"Beijing\",\"street\":\" Chaoyang Road \",\"postcode\":100025}";
		System.out.println(a);
		return new JSONWithPadding(a, jsoncallback);
	}
}
