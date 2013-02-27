package org.lemsml.jlems.io.examples;

import java.io.File;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.io.reader.FileInclusionReader;


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
		File fs = new File(fdir, filename);
		return fs;
	}
	
	 
		
	public void run() {
		 
			FileInclusionReader fir = new FileInclusionReader(getSrcFile());
 			
			
			try {
			Sim sim = new Sim(fir.read());
	 
			sim.readModel();	
 			
		
		    sim.build();
	        sim.run();
			} catch (Exception ex) {
				E.report("Failed to run " + filename, ex);
			}
		}
	
	
	public void runEulerTree() {
         try {
	 
		FileInclusionReader fir = new FileInclusionReader(getSrcFile());
			Sim sim = new Sim(fir.read());
		 
		sim.setNoConsolidation();
		
		sim.readModel();	
	 		
		sim.build();
		
		sim.runTree();
         } catch (Exception ex) {
        	 E.report("Failed to run " + filename, ex);
         }
		 
	}
	
	public void runWithMeta() {
        try {
	 
		FileInclusionReader fir = new FileInclusionReader(getSrcFile());
		Sim sim = new Sim(fir.read());
		 
		// sim.setNoConsolidation();

		sim.readModel();	
	 		
		sim.build();
		
		sim.runWithMeta();
        } catch (Exception ex) {
       	 E.report("Failed to run " + filename, ex);
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
			E.report("Failed to consolidate" + filename, ex);
			 
		}
	}
	
}
