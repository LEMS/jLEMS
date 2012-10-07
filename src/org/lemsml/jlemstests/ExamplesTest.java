/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.jlemstests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.LemsProcess;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsio.reader.FileInclusionReader;


/**
 *
 * @author Padraig
 */
public class ExamplesTest {

 
    @Test
    public void testExample1() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException  {
          executeExample("example1.xml");
    }

    @Test
    public void testExample2() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
            executeExample("example2.xml");
    }
    
    @Test
    public void testExample3() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException  {
          executeExample("example3.xml");
    }
    
    @Test
    public void testExample4() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
          executeExample("example4.xml");
    }
    @Test
    public void testExample5() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
           executeExample("example5.xml");
    }
    @Test
    public void testExample6() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
         executeTreeExample("example6.xml");
    }
    @Test
    public void testExample7() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
         executeExample("example7.xml");
    }
    @Test
    public void testExample8() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException, XMLException   {
           executeExample("example8.xml");
    }
    @Test
    public void testExample9() throws ContentError, ConnectionError, RuntimeError, ParseError, ParseException, BuildException, XMLException   {
           executeProcessExample("example9.xml");
    }

    public void executeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException {
    	File fdir = new File("examples");
    	File f = new File(fdir, filename);
    	FileInclusionReader fir = new FileInclusionReader(f);
    	Sim sim = new Sim(fir.read());

        sim.readModel();
        
        sim.setMaxExecutionTime(1000);
        
        sim.build();
        sim.run();
        
        E.info("OK - executed " + filename);
    }
    
    public void executeTreeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException {
    	File fdir = new File("examples");
    	File f = new File(fdir, filename);
    	FileInclusionReader fir = new FileInclusionReader(f);
    	Sim sim = new Sim(fir.read());

        sim.readModel();
        
        sim.setMaxExecutionTime(1000);
        
        sim.build();
        sim.runTree();
        
        E.info("OK - executed " + filename);
    }
    
    public void executeProcessExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, ParseException, BuildException,  XMLException {
    	File fdir = new File("examples");
    	
    	File f = new File(fdir, filename);
   
    		FileInclusionReader fir = new FileInclusionReader(f);
    		
    		LemsProcess lemsp = new LemsProcess(fir.read());
   	 
	 
			lemsp.readModel();	
	 		lemsp.process();
		
        E.info("OK - executed " + filename);
    }
    

    public static void main(String[] args) {
    	DefaultLogger.initialize();
    	
    	ExamplesTest ct = new ExamplesTest();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        MainTest.checkResults(r);

    }

}