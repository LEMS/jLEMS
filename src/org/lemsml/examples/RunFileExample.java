package org.lemsml.examples;

import java.io.File;

import org.lemsml.sim.Sim;


public class RunFileExample {
 
	
	File fdir = null;
	String filename;
	
	
	
	
	public RunFileExample(String fnm) {
		this (new File("."), fnm);
	}
	
	public RunFileExample(File f, String fnm) {
		fdir = f;
		filename = fnm;
	}
	
	public void run() {
		run(false);
	}
	
	public void run(boolean consolidate) {
		run(consolidate, false);
	}
		
	public void run(boolean consolidate, boolean print) {	
			File fex = new File(fdir, "examples");
			File fs = new File(fex, filename);
			
			Sim sim = new Sim(fs);
	 
			try {
			sim.readModel();	
		 		
			sim.build();
				
			if (print) {
				sim.printCB();
			}
			
			sim.run();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			 
		}
	    
}
