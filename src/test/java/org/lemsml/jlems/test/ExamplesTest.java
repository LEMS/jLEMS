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
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.LemsProcess;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.reader.FileInclusionReader;
 
public class ExamplesTest {

 
    @Test
    public void testExample1() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException  {
          boolean ok = executeExample("example1.xml");
          assertTrue("Example1", ok);
    }

    @Test
    public void testExample2() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
            boolean ok = executeExample("example2.xml");
            assertTrue("Example2", ok);
    }
    
    @Test
    public void testExample3() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException  {
    	 boolean ok = executeExample("example3.xml");
    	 assertTrue("Example 3" , ok);
    }
    
    @Test
    public void testExample4() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
          boolean ok = executeExample("example4.xml");
          assertTrue("Example 4", ok);
    }
    @Test
    public void testExample5() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
           boolean ok = executeExample("example5.xml");
           assertTrue("Example 5", ok);
    }
    @Test
    public void testExample6() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
         boolean ok = executeTreeExample("example6.xml");
         assertTrue("Example 6", ok);
    }
    @Test
    public void testExample7() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException   {
         boolean ok = executeExample("example7.xml");
         assertTrue("Example 7", ok);
    }
    @Test
    public void testExample8() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException, XMLException   {
           boolean ok = executeExample("example8.xml");
           assertTrue("Example 8", ok);
    }
    @Test
    public void testExample9() throws ContentError, ConnectionError, RuntimeError, ParseError, ParseException, BuildException, XMLException   {
           boolean ok = executeProcessExample("example9.xml");
         assertTrue("Example 9", ok); 
    }
    
    @Test
    public void testExample10() throws ContentError, ConnectionError, RuntimeError, ParseError, ParseException, BuildException, XMLException, IOException   {
          boolean ok = executeTreeExample("example10_Q10.xml");
          assertTrue("Example 10", ok);
    }
    
    
    public boolean executeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException {
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
    
    public boolean executeTreeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException,  XMLException {   	
		URL url = this.getClass().getResource("/"+filename);
		File f = new File(url.getFile());
		
    	FileInclusionReader fir = new FileInclusionReader(f);
    	Sim sim = new Sim(fir.read());

        sim.readModel();
        
        sim.setMaxExecutionTime(1000);
        
        sim.build();
        sim.runTree();
        
        E.info("OK - executed " + filename);
        return true;
    }
    
    public boolean executeProcessExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, ParseException, BuildException,  XMLException {
		URL url = this.getClass().getResource("/"+filename);
		File f = new File(url.getFile());
   
    		FileInclusionReader fir = new FileInclusionReader(f);
    		
    		LemsProcess lemsp = new LemsProcess(fir.read());
   	 
	 
			lemsp.readModel();	
	 		lemsp.process();
		
        E.info("OK - executed " + filename);
        return true;
    }
    

    public static void main(String[] args) {
    	DefaultLogger.initialize();
    	
    	ExamplesTest ct = new ExamplesTest();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        Main.checkResults(r);

    }

}