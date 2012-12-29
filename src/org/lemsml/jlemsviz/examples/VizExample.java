package org.lemsml.jlemsviz.examples;

import java.io.File;

import org.lemsml.jlemsio.examples.RunFileExample;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsio.out.FileResultWriterFactory;
import org.lemsml.jlemsviz.datadisplay.SwingDataViewerFactory;

public final class VizExample {
	
	
	private VizExample() {
		
	}
	
	public static void run(String s) {
		run("examples", s);
	}
	
	
	private static void initIO() {
		SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
		FileResultWriterFactory.initialize();
	}
	
	
	
	public static void run(String fdrname, String s) {
		initIO();
		
 		
		File fdir = new File(fdrname);
	
		RunFileExample fe = new RunFileExample(fdir, s);
	
		fe.run();

	}

	public static void runTree(String s) {
		initIO();
			
		File fdir = new File("examples");
	
		RunFileExample fe = new RunFileExample(fdir, s);
	
		fe.runEulerTree();

	}
	
}
