package edu.thu.keg.mobiledata.internetgraph.sample;

/**
*
* @author Yuan Bozhi
* 
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ReadFile {
	public CharString cs1;
	public ReadFile(){
		cs1=new CharString("abaaaa");
	}
	
	
	
	public static void main(String []args)
	{
//		CharString cs=new CharString("abaabaa");
//		CharString cs1=new CharString("ba");
//		CharString cs2=new CharString("CC");
//		int i=cs.indexOf(new CharString("ba"),1);
//		System.out.println(cs.changeFirstAintoBforCs(cs1, cs2).S);
		final SelectedWindow w=new SelectedWindow("文件抽取");
		  w.file.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                JFileChooser jt=new JFileChooser();
	                jt.setMultiSelectionEnabled(true);
	                jt.setCurrentDirectory(new File("E://拓明数据/"));
	                FileNameExtensionFilter ft=new FileNameExtensionFilter("tmr file", "TMR");
	                jt.addChoosableFileFilter(ft);
	                jt.showOpenDialog(null);
	                w.filetemp=jt.getSelectedFiles();
	            }
	      });
	}

}
