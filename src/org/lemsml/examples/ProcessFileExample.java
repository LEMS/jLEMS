package org.lemsml.examples;

import java.io.File;

import org.lemsml.sim.LemsProcess;


public class ProcessFileExample {
 
	File froot;
	String filename;
	
	public ProcessFileExample(File fdir, String fnm) {
		froot = fdir;
		filename = fnm;
	}
	
		public void process() {
	 		File fs = new File(froot, filename);
			
			LemsProcess lemsp = new LemsProcess(fs);
	 
			try {
				lemsp.readModel();	
		 		lemsp.process();
			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			 
		}
	    
}
