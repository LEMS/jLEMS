package org.lemsml.examples;

import java.io.File;
 
import org.lemsml.examples.RunFileExample;
 

public class Example7 {
	
	public static void main(String[] argv) {
	 		
		File fdir = new File("../jLEMS");
		
		RunFileExample fe = new RunFileExample(fdir, "example7.xml");
		
		fe.run();
	
	}
	 
    
    
}
