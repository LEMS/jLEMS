package org.lemsml.examples;

import java.io.File;

import org.lemsml.sim.Sim;


public class RunFileExample {
 
	String filename;
	
	public RunFileExample(String fnm) {
		filename = fnm;
	}
	
	
	public void run() {
		run(false);
	}
	
	
		public void run(boolean consolidate) {
			File fex = new File("examples");
			File fs = new File(fex, filename);
			
			Sim sim = new Sim(fs);
	 
			try {
			sim.readModel();	
		 		
			sim.build(consolidate);
				
			sim.run();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			 
		}
	    
}
