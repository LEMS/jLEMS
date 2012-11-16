package org.lemsml.jlemstests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.lemsml.jlems.codger.ComponentGenerator;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.flatten.ComponentFlattener;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsio.reader.FileInclusionReader;
import org.lemsml.jlemsio.xmlio.XMLSerializer;


// N.B. - not a test yet, just for development


public class CodeGenerationTest {

	
	
	 
	 
    public static void main(String[] args) throws ContentError, ParseError, ConnectionError, RuntimeError, IOException {
    	DefaultLogger.initialize();
       
    	try {
    		CodeGenerationTest cft = new CodeGenerationTest();
    		cft.runExample1();
    		
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    @Test
    public void runExample1() throws ContentError, ConnectionError, ParseError, IOException, RuntimeError, ParseException, BuildException, XMLException {
    	File f1 = new File("examples/example1.xml");
 		generateFromFile(f1, "na");
    }
    
    
    
    
    public void generateFromFile(File f, String tgtid) throws ContentError,
    		ConnectionError, ParseError, IOException, RuntimeError, ParseException, 
    		BuildException, XMLException {
    	E.info("Loading LEMS file from: " + f.getAbsolutePath());

        FileInclusionReader fir = new FileInclusionReader(f);
        Sim sim = new Sim(fir.read());

        sim.readModel();
     
        sim.build();

        //sim.run();

        Lems lems = sim.getLems();
        
        ComponentGenerator cg = new ComponentGenerator();
        for (Component cpt : lems.getComponents()) {
        	E.info("Adding cpt " + cpt.getID());
        	cg.addComponentBehavior(cpt.getComponentBehavior());
        }
        
        
		
		String srcCode = cg.getCombinedJavaSource();
		
				// cg.getJavaSource(tgtid);
		
		E.info("Generated code:\n\n" + srcCode);
		
		
		
    }
}
