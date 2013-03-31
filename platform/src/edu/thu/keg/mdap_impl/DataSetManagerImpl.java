/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.IOException;

import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.DataProvider;

/**
 * @author myc
 *
 */
public class DataSetManagerImpl implements DataSetManager {

	private DataSet[] datasets = null;
	public DataSetManagerImpl() {
		try {
			Config.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSet(java.lang.String)
	 */
	@Override
	public DataSet getDataSet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataSetFactory#getDataSetList()
	 */
	@Override
	public DataSet[] getDataSetList() {
		if (datasets == null) {
			loadDataSets();
		}
		return datasets;
	}
	
	private void loadDataSets() {
		String f = Config.getDataSetStorage();
		
	}
	@Override
	public void storeDataSet(DataSet ds) {
		// TODO Auto-generated method stub
		
	}

}
