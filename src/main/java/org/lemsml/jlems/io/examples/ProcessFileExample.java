package org.lemsml.jlems.io.examples;

import java.io.File;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.LemsProcess;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.reader.FileInclusionReader;


public class ProcessFileExample {
 
	File froot;
	String filename;
	
	public ProcessFileExample(File fdir, String fnm) {
		froot = fdir;
		filename = fnm;
	}
	
		public void process() throws ContentError, ParseError, ParseException, BuildException, XMLException, ConnectionError, RuntimeError {
	 		File fs = new File(froot, filename);
	 		FileInclusionReader fir = new FileInclusionReader(fs);
			LemsProcess lemsp = new LemsProcess(fir.read());
	 
		 
				lemsp.readModel();	
		 		lemsp.process();
			
			 
			 
		}
	    
}
