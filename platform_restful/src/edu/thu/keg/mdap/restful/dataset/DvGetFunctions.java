package edu.thu.keg.mdap.restful.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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

import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDataviewLine;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDataviewName;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JField;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JFieldName;

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
			@QueryParam("featuretype") String featureType,
			@QueryParam("id") String id,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		log.info(uriInfo.getAbsolutePath());
		List<JDataviewName> dataviewList = new ArrayList<JDataviewName>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");

			DataSetManager datasetManager = p.getDataSetManager();
			System.out.println(id + " " + featureType);
			Collection<DataView> dataviews = null;
			if (id == null && featureType == null)
				dataviews = datasetManager.getDataViewList();
			else if (id == null && featureType != null)
				dataviews = datasetManager.getDataViewList(DataFeatureType
						.valueOf(featureType));
			else if (id != null && featureType == null) {
				dataviews = datasetManager.getDataViewList(id);
			} else {
				Collection<DataView> mix1 = new HashSet<>();
				Collection<DataView> mix2 = new HashSet<>();
				dataviews = datasetManager.getDataViewList(DataFeatureType
						.valueOf(featureType));
				mix1.addAll(dataviews);
				dataviews = datasetManager.getDataViewList(id);
				mix2.addAll(dataviews);
				mix1.retainAll(mix2);
				dataviews = mix1;
			}
			if (dataviews != null)
				for (DataView dataview : dataviews) {
					JDataviewName dname = new JDataviewName();
					dname.setId(dataview.getId());
					dname.setDataviewName(dataview.getName());
					dname.setDescriptionEn(dataview
							.getDescription(Locale.ENGLISH));
					dname.setDescriptionZh(dataview
							.getDescription(Locale.CHINESE));
					ArrayList<String> identifiers = new ArrayList<>();
					ArrayList<String> values = new ArrayList<>();
					for (DataField df : dataview.getKeyFields()) {
						identifiers.add(df.getName());
					}
					dname.setIdentifiers(identifiers);
					for (DataField df : dataview.getValueFields()) {
						values.add(df.getName());
					}
					dname.setValues(values);
					dname.setDatafeature(dataview.getFeatureType().name());
					dname.setDataset(dataview.getDataSet());
					dataviewList.add(dname);
				}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return new JSONWithPadding(new GenericEntity<List<JDataviewName>>(
				dataviewList) {
		}, jsoncallback);

	}

	@GET
	@Path("/getdv")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDataview(@QueryParam("id") String id,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		log.info(uriInfo.getAbsolutePath());
		List<JDataviewLine> dataviewList = new ArrayList<>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataView dv = datasetManager.getDataView(id);
			DataContent rs = dv.getQuery();
			rs.open();
			int i = 0;
			while (rs.next() && i++ < 100) {
				JDataviewLine jdataview = new JDataviewLine();
				List<JField> identifiers = new ArrayList<>();
				DataField[] dfs = dv.getKeyFields().toArray(new DataField[0]);
				for (DataField df : dfs) {
					JField indentifier = new JField();
					indentifier.setValue(rs.getValue(df).toString());
					indentifier.setType(rs.getValue(df).getClass()
							.getSimpleName());
					identifiers.add(indentifier);
				}
				List<JField> values = new ArrayList<>();
				dfs = dv.getValueFields().toArray(new DataField[0]);
				for (DataField df : dfs) {
					JField value = new JField();
					value.setValue(rs.getValue(df).toString());
					value.setType(rs.getValue(df).getClass().getSimpleName());
					values.add(value);
				}
				jdataview.setIdentifiers(identifiers);
				jdataview.setValues(values);
				dataviewList.add(jdataview);
			}
			rs.close();
		} catch (OperationNotSupportedException | DataProviderException e) {
			log.warn(e.getMessage());
		}
		return new JSONWithPadding(new GenericEntity<List<JDataviewLine>>(
				dataviewList) {
		}, jsoncallback);
	}

	@GET
	@Path("/getdvinfo")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDataviewInfo(@QueryParam("id") String id,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		log.info(uriInfo.getAbsolutePath());
		Platform p = (Platform) servletcontext.getAttribute("platform");
		DataSetManager datasetManager = p.getDataSetManager();
		DataView dv = datasetManager.getDataView(id);
		JDataviewName dname = new JDataviewName();
		dname.setId(dv.getId());
		dname.setDataviewName(dv.getName());
		dname.setDescriptionEn(dv.getDescription(Locale.ENGLISH));
		dname.setDescriptionZh(dv.getDescription(Locale.CHINESE));
		ArrayList<String> identifiers = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();
		for (DataField df : dv.getKeyFields()) {
			identifiers.add(df.getName());
		}
		dname.setIdentifiers(identifiers);
		for (DataField df : dv.getValueFields()) {
			values.add(df.getName());
		}
		dname.setValues(values);
		dname.setDatafeature(dv.getFeatureType().name());
		// } catch (Exception e) {
		// log.warn(e.getStackTrace());
		// // e.printStackTrace();
		// }

		return new JSONWithPadding(new GenericEntity<JDataviewName>(dname) {
		}, jsoncallback);
	}

	@GET
	@Path("/getdvfds")
	@Produces({ "application/javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding getDataviewFieldsNames(@QueryParam("id") String id,
			@QueryParam("jsoncallback") @DefaultValue("fn") String jsoncallback) {
		log.info(uriInfo.getAbsolutePath());
		List<JFieldName> all_fn = new ArrayList<JFieldName>();
		try {
			Platform p = (Platform) servletcontext.getAttribute("platform");
			DataSetManager datasetManager = p.getDataSetManager();
			DataView dv = datasetManager.getDataView(id);
			DataField[] dfs = dv.getAllFields().toArray(new DataField[0]);
			for (DataField df : dfs) {
				JFieldName jfn = new JFieldName();
				jfn.setFieldName(df.getName());
				jfn.setDescription(df.getDescription());
				jfn.setIsKey(df.isKey());
				jfn.setType(df.getFieldType().name());

				jfn.setFunctionality(df.getFunction().name());
				jfn.setDatasetName(df.getDataSet().getName());
				jfn.setDatasetOwner(df.getDataSet().getOwner());
				all_fn.add(jfn);
			}

		} catch (Exception e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new JSONWithPadding(new GenericEntity<List<JFieldName>>(all_fn) {
		}, jsoncallback);
	}
}
