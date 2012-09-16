package org.lemsml.examples;

import java.io.File;

import org.lemsml.sim.Sim;
import org.lemsml.util.E;


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
 
	private void tryDataViewerInitialization() {
		String cnm = "org.lemsml.datadisplay.SwingDataViewerFactory";
		try {
			Class<? extends Object> csdv = Class.forName(cnm);
			
			Object o = csdv.newInstance();
			E.info("instantiated " + o);
			
		} catch (Exception ex) {
			E.info("Couldn't initialize a viewer - none on classpath?");
		}
	}
	
	
	private File getSrcFile() {
		File fex = new File(fdir, "examples");
		File fs = new File(fex, filename);
		return fs;
	}
	
	 
		
	public void run() {	
		
		tryDataViewerInitialization();
		
 			Sim sim = new Sim(getSrcFile());
	 
			try {
			sim.readModel();	
		 		
			sim.build();
			
			sim.run(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}		 
		}
	
	
	public void runEulerTree() {
		
		tryDataViewerInitialization();
		
		Sim sim = new Sim(getSrcFile());
		 
		sim.setNoConsolidation();
		
		try {
		sim.readModel();	
	 		
		sim.build();
		
		sim.run(false);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}		 
	}
	
	
	
	public void printConsolidated() {
		Sim sim = new Sim(getSrcFile());
 
		try {
			sim.readModel();	
	 		
			sim.build();
			sim.printFirstConsolidated();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
