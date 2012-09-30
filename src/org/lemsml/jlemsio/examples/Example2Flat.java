package org.lemsml.jlemsio.examples;

import java.io.File;

import org.lemsml.jlemsio.examples.RunFileExample;
 

public class Example2Flat {
	
	public static void main(String[] argv) {
		
	 
		File fdir = new File("../jLEMS");
		
		RunFileExample fe = new RunFileExample(fdir, "example2.xml");
		
		fe.runEulerTree();
	}
	 
    
    
}
