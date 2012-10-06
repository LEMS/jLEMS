package org.lemsml.jlemsio.examples;

import java.io.File;
 

public class Example1 {
	
	public static void main(String[] argv) {
	 
		
		File fdir = new File("../jLEMS");
		
		RunFileExample fe = new RunFileExample(fdir, "example1.xml");
		
		fe.run();
	
	}
	 
    
    
}
