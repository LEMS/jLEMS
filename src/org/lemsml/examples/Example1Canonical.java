package org.lemsml.examples;

import java.io.File;

import org.lemsml.sim.LemsProcess;
import org.lemsml.sim.Sim;
import org.lemsml.util.E;
import org.lemsml.util.FileUtil;

public class Example1Canonical {

	
	public static void main(String[] argv) {
		LemsProcess sim = new Sim(Example1Canonical.class, "example1.xml");
		
		try {
			sim.readModel();	
		
			String txt = sim.canonicalText();
			FileUtil.writeStringToFile(txt, new File("lems/ex1canonical.xml"));
		 
		
		} catch (Exception ex) {
			E.error(" " + ex);
			ex.printStackTrace();
		}
	}
    
    
}
