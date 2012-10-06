package org.lemsml.jlemsio.examples;

import java.io.File;
 

public class Example3 {
	
	public static void main(String[] argv) {
	 
		
		File fdir = new File("../jLEMS");
		
		RunFileExample fe = new RunFileExample(fdir, "example3.xml");
		
		fe.run();
	
	}
	 
    
    
}
