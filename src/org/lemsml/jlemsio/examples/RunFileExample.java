package org.lemsml.jlemsio.examples;

import java.io.File;

import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.util.E;
import org.lemsml.jlemsio.FileInclusionReader;


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
  	
	private File getSrcFile() {
		File fex = new File(fdir, "examples");
		File fs = new File(fex, filename);
		return fs;
	}
	
	 
		
	public void run() {	
		
 			
		try {
			FileInclusionReader fir = new FileInclusionReader(getSrcFile());
 			
			
			
			Sim sim = new Sim(fir.read());
	 
			sim.readModel();	
 			
		
		    sim.build();
	        sim.run(true);
	        
			} catch (Exception ex) {
				ex.printStackTrace();
			}		 
		}
	
	
	public void runEulerTree() {
         
		try {
		FileInclusionReader fir = new FileInclusionReader(getSrcFile());
			Sim sim = new Sim(fir.read());
		 
		sim.setNoConsolidation();
		
		sim.readModel();	
	 		
		sim.build();
		
		sim.run(false);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}		 
	}
	
	
	
	public void printConsolidated() {
		
 
		try {
			FileInclusionReader fir = new FileInclusionReader(getSrcFile());
			Sim sim = new Sim(fir.read());
			sim.readModel();	
	 		
			sim.build();
			sim.printFirstConsolidated();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
