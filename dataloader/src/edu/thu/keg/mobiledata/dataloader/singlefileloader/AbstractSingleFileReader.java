package edu.thu.keg.mobiledata.dataloader.singlefileloader;

import java.io.File;
import java.util.ArrayList;

public abstract class AbstractSingleFileReader {
	protected File file;
	public void init(String path) {
		this.file = new File(path);
	}
	public abstract ArrayList<String> getInsertSQL();
}
