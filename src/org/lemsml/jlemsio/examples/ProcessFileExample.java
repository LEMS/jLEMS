package org.lemsml.jlemsio.examples;

import java.io.File;

import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.LemsProcess;
import org.lemsml.jlemsio.reader.FileInclusionReader;


public class ProcessFileExample {
 
	File froot;
	String filename;
	
	public ProcessFileExample(File fdir, String fnm) {
		froot = fdir;
		filename = fnm;
	}
	
		public void process() throws ContentError {
	 		File fs = new File(froot, filename);
	 		FileInclusionReader fir = new FileInclusionReader(fs);
			LemsProcess lemsp = new LemsProcess(fir.read());
	 
			try {
				lemsp.readModel();	
		 		lemsp.process();
			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			 
		}
	    
}
