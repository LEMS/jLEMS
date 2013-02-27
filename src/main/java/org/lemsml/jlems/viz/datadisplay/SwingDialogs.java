package org.lemsml.jlems.viz.datadisplay;

import java.io.File;

import javax.swing.JFileChooser;

public class SwingDialogs {

	
	static SwingDialogs instance;
	
	JFileChooser fileChooser;
	
	
	public static SwingDialogs getInstance() {
		if (instance == null) {
			instance = new SwingDialogs();
		}
		return instance;
	}
	
	
	
	
	public File getFileToRead() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
		}
		
		int retval = fileChooser.showDialog(null, "Import");
		File ret = null;
		if (retval == JFileChooser.APPROVE_OPTION) {
			ret=  fileChooser.getSelectedFile();
		}
		return ret;
	}
	
	
}
