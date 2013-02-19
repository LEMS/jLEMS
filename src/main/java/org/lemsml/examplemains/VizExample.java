package org.lemsml.examplemains;

import java.io.File;

import org.lemsml.jlems.io.examples.RunFileExample;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.logging.TimeLogger;
import org.lemsml.jlems.io.out.FileResultWriterFactory;
import org.lemsml.jlems.viz.datadisplay.SwingDataViewerFactory;

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
		TimeLogger tim = new TimeLogger();
 		
		File fdir = new File(fdrname);
	
		RunFileExample fe = new RunFileExample(fdir, s);
	
		fe.run();
		tim.report("Default run");
	}

	public static void runTree(String s) {
		initIO();
		TimeLogger tim = new TimeLogger();
		File fdir = new File("examples");
	
		RunFileExample fe = new RunFileExample(fdir, s);
	
		fe.runEulerTree();
		tim.report("Euler tree");
	}

	
	public static void runWithMeta(String s) {
		initIO();
		TimeLogger tim = new TimeLogger();
		File fdir = new File("examples");
		RunFileExample fe = new RunFileExample(fdir, s);
		
		fe.runWithMeta();
		tim.report("Meta");
	}
	
	
}
