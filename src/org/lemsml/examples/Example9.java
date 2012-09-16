package org.lemsml.examples;

import java.io.File;
 
import org.lemsml.examples.ProcessFileExample;
import org.lemsml.examples.RunFileExample;
 

public class Example9 {
	
	public static void main(String[] argv) {
	
			
		File fdir = new File("../jLEMS");
		
		ProcessFileExample fe = new ProcessFileExample(fdir, "example9.xml");
		fe.process();
	}
	 
    
    
}
