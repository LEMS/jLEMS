package org.lemsml.jlems.io.examples;

import java.io.File;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.XMLException;
 

public final class Example9 {
	
	private Example9() {
		
	}
	
	
	public static void main(String[] argv) throws ContentError, ParseError, ParseException, BuildException, XMLException, ConnectionError, RuntimeError {
	
			
		File fdir = new File("../jLEMS");
		
		 
			ProcessFileExample fe = new ProcessFileExample(fdir, "example9.xml");
			fe.process();
		 
	}
	 
    
    
}
