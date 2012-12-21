package edu.thu.keg.mobiledata.dataloader;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Beijing1211 {
	public static void main(String[] args) {
		String path = args[0];
		File log = new File(path + "log.txt");
		FileWriter writer = null;
		try {
			log.createNewFile();
			writer = new FileWriter(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File f = new File(path);
		File[] servers = f.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		});
//		Calendar date = Calendar.getInstance();
//		date.set(2012, 11, 1);
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		format.format(date.getTime());
		for (File server : servers) {
			File[] dates = server.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.length() == 10;
				}
			});
			for (File date : dates) {
				try {
				File wap = date.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return name.contains("WAP");
					}
				})[0];
				File[] fs = wap.listFiles();
				DataLoader.loadGNData(fs, writer);
				}
				catch (Exception ex) {
					try {
						writer.write(date.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
