package org.lemsml.jlemsviz.examples;

import java.io.File;

import org.lemsml.jlemsio.examples.ProcessFileExample;
import org.lemsml.jlemsio.logging.DefaultLogger;
 

public class Example9 {
	
	public static void main(String[] argv) {
	
		DefaultLogger.initialize();
		 
		File fdir = new File("../jLEMS");
		File fpd = new File(fdir, "examples");
		ProcessFileExample fe = new ProcessFileExample(fpd, "example9.xml");
		try {
			fe.process();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	 
    
    
}
