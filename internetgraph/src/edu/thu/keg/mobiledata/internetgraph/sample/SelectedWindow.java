package edu.thu.keg.mobiledata.internetgraph.sample;
/**
*
* @author Yuan Bozhi
* 
*/
import java.awt.Container;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectedWindow extends JFrame{
	JButton file,extract;
	static File[] filetemp;
	public SelectedWindow(String s){
		 	super(s);
	        this.setLayout(null);
	        JPanel p1 = new JPanel();
	        Container c = getContentPane();
	        c.add(p1);
	        p1.setLayout(null);
	        p1.setBounds(20, 20, 100 ,200);
	        file=new JButton("ä¯ÀÀÎÄ¼þ");
	        file.setBounds(0, 0, 100, 50);
	        extract= new JButton("·ÖÎö");
	        extract.setBounds(0, 50, 100, 50);
	        p1.add(file);
	        p1.add(extract);
	        setVisible(true);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(320, 320);
	}

}
