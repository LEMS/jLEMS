/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.jlems.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.reader.FileInclusionReader;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.XMLException;

 
public class Example1Test {

 
    @Test
    public void testExample1() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException, XMLException  {
          boolean ret = executeExample("example1.xml");
          assertTrue("example 1", ret);
    }

   
    public boolean executeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException, XMLException {
		URL url = this.getClass().getResource("/"+filename);
		File f = new File(url.getFile());

    	FileInclusionReader fir = new FileInclusionReader(f);
    	Sim sim = new Sim(fir.read());

        sim.readModel();
        
        sim.setMaxExecutionTime(1000);
        
        sim.build();
        sim.run();
        
        E.info("OK - executed " + filename);
        return true;
    }
    
    

    public static void main(String[] args) {
    	DefaultLogger.initialize();
    	
    	Example1Test ct = new Example1Test();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        MainTest.checkResults(r);

    }

}