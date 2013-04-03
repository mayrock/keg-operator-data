package edu.thu.keg.mdap.restful;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.OperationNotSupportedException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.impl.provider.header.MediaTypeProvider;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.provider.IllegalQueryException;
import edu.thu.keg.mdap_impl.PlatformImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.xml.XMLSerializer;

@Path("/dataset")
public class DataSetFunctions {
	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/getdatasets")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllDataSetsNames_json() {
		String result = "";
		try {
			String PI = ResourceBundle.getBundle("platform_initial").getString(
					"PlatformImpl");
			Class PIClass;

			PIClass = Class.forName(PI);

			Platform DMSIInstance = (Platform) PIClass.newInstance();
			DataSetManager datasetManager = DMSIInstance.getDataSetManager();
			ArrayList<String> datasetsName = new ArrayList<>();
			Collection<DataSet> datasets = datasetManager.getDataSetList();
			Iterator<DataSet> dataset_it = datasets.iterator();
			while (dataset_it.hasNext()) {
				datasetsName.add(dataset_it.next().getName());
			}
			// int i=0;
			// while(i<10)
			// {
			// datasetsName.add(String.valueOf(i));
			// i++;
			// }
			JSONArray jsonArray = (JSONArray) JSONSerializer
					.toJSON(datasetsName);
			result = jsonArray.toString();
		}

		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@GET
	@Path("/getdatasets")
	@Produces(MediaType.APPLICATION_XML)
	public String getAllDataSetsNames_xml() {
		String result = "";
		try {
			String PI = ResourceBundle.getBundle("platform_initial").getString(
					"PlatformImpl");
			Class PIClass = Class.forName(PI);
			Platform DMSIInstance = (Platform) PIClass.newInstance();
			DataSetManager datasetManager = DMSIInstance.getDataSetManager();
			ArrayList<String> datasetsName = new ArrayList<>();
			Collection<DataSet> datasets = datasetManager.getDataSetList();
			Iterator<DataSet> dataset_it = datasets.iterator();
			while (dataset_it.hasNext()) {
				datasetsName.add(dataset_it.next().getName());
			}
			XMLSerializer xmlSerializer = new XMLSerializer();
			result = xmlSerializer.write(JSONSerializer.toJSON(datasetsName));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@GET
	@Path("/getdatasetlocation/{dataasetname}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDataSetLocation_json(
			@PathParam("datasetname") String datasetname) {
		String result = "";
		try {
			String PI = ResourceBundle.getBundle("platform_initial").getString(
					"PlatformImpl");
			Class PIClass = Class.forName(PI);
			Platform DMSIInstance = (Platform) PIClass.newInstance();
			DataSetManager datasetManager = DMSIInstance.getDataSetManager();
			DataSet dataset = datasetManager.getDataSet(datasetname);
			ResultSet rs = dataset.getResultSet();
			ArrayList<String> al_rs = new ArrayList<>();
			String line = "";
			while (rs.next()) {
				al_rs.add("[" + rs.getString("Longitude") + ","
						+ rs.getString("Latitude") + "]");
			}
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(al_rs);
			result = jsonArray.toString();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@GET
	@Path("/getdatasetlocation/{dataasetname}")
	@Produces(MediaType.APPLICATION_XML)
	public String getDataSetLocation_xml(
			@PathParam("datasetname") String datasetname) {
		String result = "";
		try {
			String PI = ResourceBundle.getBundle("platform_initial").getString(
					"PlatformImpl");
			Class PIClass = Class.forName(PI);
			Platform DMSIInstance = (Platform) PIClass.newInstance();
			DataSetManager datasetManager = DMSIInstance.getDataSetManager();
			DataSet dataset = datasetManager.getDataSet(datasetname);
			ResultSet rs = dataset.getResultSet();
			ArrayList<String> al_rs = new ArrayList<>();
			String line = "";
			while (rs.next()) {
				al_rs.add("[" + rs.getString("Longitude") + ","
						+ rs.getString("Latitude") + "]");
			}
			XMLSerializer xmlSerializer = new XMLSerializer();
			result = xmlSerializer.write(JSONSerializer.toJSON(al_rs));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
