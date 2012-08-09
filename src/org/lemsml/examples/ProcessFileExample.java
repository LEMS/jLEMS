package org.lemsml.examples;

import java.io.File;

import org.lemsml.sim.LemsProcess;


public class ProcessFileExample {
 
	String filename;
	
	public ProcessFileExample(String fnm) {
		filename = fnm;
	}
	
		public void process() {
			File fex = new File("examples");
			File fs = new File(fex, filename);
			
			LemsProcess lemsp = new LemsProcess(fs);
	 
			try {
				lemsp.readModel();	
		 		lemsp.process();
			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			 
		}
	    
}
