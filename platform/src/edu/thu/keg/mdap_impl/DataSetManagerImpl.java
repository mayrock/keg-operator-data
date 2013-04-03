/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.datafield.DataField;
import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap_impl.dataset.DataSetImpl;

/**
 * Implementation of the interface DataSetManager
 * @author Yuanchao Ma
 *
 */
public class DataSetManagerImpl implements DataSetManager {

	private HashMap<String, DataSet> datasets = null;
	private XStream xstream;
	
	private XStream getXstream() {
		if (xstream == null) {
			xstream = new XStream(new StaxDriver());
		}
		return xstream;
	}
	
	public DataSetManagerImpl() {
		datasets = new HashMap<String, DataSet>();
		try {
			loadDataSets();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSet(java.lang.String)
	 */
	@Override
	public DataSet getDataSet(String name) {
		DataSet ds = datasets.get(name);
		if (ds == null) {
			throw new IllegalArgumentException("the dataset \"" + name + "\" does not exist.");
		}
		return ds;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSetList()
	 */
	@Override
	public Collection<DataSet> getDataSetList() {
		return datasets.values();
	}
	
	private void loadDataSets() {
		String f = Config.getDataSetFile();
		DataSet[] sets = (DataSet[])getXstream().fromXML(new File(f));
		for (DataSet ds : sets) {
			datasets.put(ds.getName(), ds);
		}
	}
	@Override
	public void storeDataSet(DataSet ds) {
		datasets.put(ds.getName(), ds);
		FileWriter fw;
		try {
			DataSet[] dss = new DataSet[datasets.size()];
			int i = 0;
			for (DataSet d : datasets.values()) {
				dss[i++] = d;
			}
			fw = new FileWriter(Config.getDataSetFile(), false);
			getXstream().marshal(dss, new PrettyPrintWriter(fw));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public DataSet createDataSet(String name, DataProvider provider,
			DataField[] fields, boolean loadable) {
		return new DataSetImpl(name, provider, loadable, fields);
	}
	

}
