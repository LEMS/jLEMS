package org.lemsml.examples;

import java.io.File;
 
import org.lemsml.examples.RunFileExample;
 

public class Example8 {
	
	public static void main(String[] argv) {
	
 		
		File fdir = new File("../jLEMS");
		
		RunFileExample fe = new RunFileExample(fdir, "example8.xml");
		
		fe.run();
	
	}
	 
    
    
}
