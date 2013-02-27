package org.lemsml.examplemains;

import java.io.File;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.examples.ProcessFileExample;
import org.lemsml.jlems.io.logging.DefaultLogger;
 

public final class Example9 {
	
	private Example9() {
		
	}
	
	public static void main(String[] argv) throws ContentError, ParseError, ParseException, BuildException, XMLException, ConnectionError, RuntimeError {
	
		DefaultLogger.initialize();
		 
		File fdir = new File("../jLEMS");
		File fpd = new File(fdir, "examples");
		ProcessFileExample fe = new ProcessFileExample(fpd, "example9.xml");
		 
			fe.process();
		 
	}
	 
    
    
}
