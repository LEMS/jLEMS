package org.lemsml.jlemsio.examples;

import java.io.File;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.LemsProcess;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.reader.FileInclusionReader;


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
