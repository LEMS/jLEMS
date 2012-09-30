package org.lemsml.jlemsio.examples;

import java.io.File;
 
import org.lemsml.jlemsio.examples.ProcessFileExample;
import org.lemsml.jlemsio.examples.RunFileExample;
 

public class Example9 {
	
	public static void main(String[] argv) {
	
			
		File fdir = new File("../jLEMS");
		
		try {
			ProcessFileExample fe = new ProcessFileExample(fdir, "example9.xml");
			fe.process();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	 
    
    
}
