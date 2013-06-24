package edu.thu.keg.mdap.restful.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.sun.jersey.api.json.JSONWithPadding;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.datafeature.DataFeatureType;
import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;

import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDataset;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDatasetName;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JField;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JFieldName;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JStatistic;

/**
 * the functions of dataview's get operations
 * 
 * @author Yuan Bozhi
 * 
 */
@Path("/dvg")
public class DvGetFunctions {
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
				dname.setDescriptionEn(dataset.getDescription(Locale.ENGLISH));
				dname.setDescriptionZh(dataset.getDescription(Locale.CHINESE));
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
				dname.setDescriptionEn(dataset.getDescription(Locale.ENGLISH));
				dname.setDescriptionZh(dataset.getDescription(Locale.CHINESE));
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
	@Path("/getstadv")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getStaDataview(
			@QueryParam("dataset") String dataview,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getStaDataset " + dataview + " "
				+ uriInfo.getAbsolutePath());
		List<JStatistic> datasetList = new ArrayList<JStatistic>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataView dv = datasetManager.getDataView(dataview);
			DataContent rs = dv.getQuery();
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 20) {
				// System.out.println(rs.getValue(ds.getDataFields()[0])
				// .toString()
				// + " "
				// + rs.getValue(ds.getDataFields()[1]).toString());
				JStatistic statistic = new JStatistic();
				ArrayList<String> keys = new ArrayList<>();
				for (DataField key : dv.getKeyFields()) {
					keys.add(rs.getValue(key).toString());
				}
				ArrayList<Double> values = new ArrayList<>();
				for (DataField df : dv.getValueFields()) {
					values.add(Double.valueOf(rs.getValue(df).toString()));
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

	@GET
	@Path("/getdv")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDataview(@QueryParam("dataset") String dataview,
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

	@GET
	@Path("/getdvinfo")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDatasetInfo(
			@QueryParam("dataset") String dataview,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getDatasetInfo " + dataview + " "
				+ uriInfo.getAbsolutePath());
		log.info(uriInfo.getAbsolutePath());
		Platform p = (Platform) servletcontext.getAttribute("platform");
		DataSetManager datasetManager = p.getDataSetManager();
		DataView dv = datasetManager.getDataView(dataview);

		// if(i++>=1)
		// break;
		JDatasetName dname = new JDatasetName();
		dname.setDatasetName(dv.getName());
		dname.setDescriptionEn(dv.getDescription(Locale.ENGLISH));

		dname.setDescriptionZh(dv.getDescription(Locale.CHINESE));

		System.out.println(dname.getDescriptionEn());
		System.out.println(dname.getDescriptionZh());
		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();
		for (DataField df : dv.getKeyFields()) {
			keys.add(df.getName());
		}
		dname.setKeys(keys);
		for (DataField df : dv.getValueFields()) {
			values.add(df.getName());
		}
		dname.setValues(values);

		// } catch (Exception e) {
		// log.warn(e.getStackTrace());
		// // e.printStackTrace();
		// }

		return new JSONWithPadding(new GenericEntity<JDatasetName>(dname) {
		}, jsoncallback);
	}

	@GET
	@Path("/getdvfds")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDataviewFieldsNames(
			@QueryParam("dataset") String dataview,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		System.out.println("getDatasetFieldsNames " + dataview + " "
				+ uriInfo.getAbsolutePath());
		List<JFieldName> all_fn = new ArrayList<JFieldName>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataView dv = datasetManager.getDataView(dataview);
			DataField[] dfs = dv.getAllFields();
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
}
