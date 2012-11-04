package org.lemsml.jlemsviz.examples;

import java.io.File;

import org.lemsml.jlemsio.examples.RunFileExample;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsviz.datadisplay.SwingDataViewerFactory;

public class VizExample {
	
	
	
	public static void run(String s) {
		SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
		
 		
		File fdir = new File(".");
	
		RunFileExample fe = new RunFileExample(fdir, s);
	
		fe.run();

	}

	public static void runTree(String s) {
		SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
	 
		
		File fdir = new File("../jLEMS");
	
		RunFileExample fe = new RunFileExample(fdir, s);
	
		fe.runEulerTree();

	}
	
}
