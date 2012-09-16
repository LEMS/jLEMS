/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.expression.ParseError;
import org.lemsml.run.ConnectionError;
import org.lemsml.sim.LemsProcess;
import org.lemsml.sim.Sim;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;


/**
 *
 * @author Padraig
 */
public class ExamplesTest {

	boolean cacheDebug;
	
    @Before
    public void setUp() {
    	cacheDebug = E.getDebug();
    	E.setDebug(false);
    }
    
    @After
    public void restore() {
      	E.setDebug(cacheDebug);
    }

    @Test
    public void testExample1() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException  {
          executeExample("example1.xml");
    }

    @Test
    public void testExample2() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException   {
            executeExample("example2.xml");
    }
    
    @Test
    public void testExample3() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException  {
          executeExample("example3.xml");
    }
    
    @Test
    public void testExample4() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException   {
          executeExample("example4.xml");
    }
    @Test
    public void testExample5() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException   {
           executeExample("example5.xml");
    }
    @Test
    public void testExample6() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException   {
         executeExample("example6.xml");
    }
    @Test
    public void testExample7() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException   {
         executeExample("example7.xml");
    }
    @Test
    public void testExample8() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException   {
           executeExample("example8.xml");
    }
    @Test
    public void testExample9() throws ContentError, ConnectionError, RuntimeError, ParseError   {
           executeProcessExample("example9.xml");
    }

    public void executeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException {
    	File fdir = new File("examples");
    	
    	File f = new File(fdir, filename);
    	Sim sim = new Sim(f);

        sim.readModel();
        sim.build();
        sim.run();
        
        E.info("  ****  Successfully ran example from file " + filename);
    }
    
    
    
    public void executeProcessExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError {
    	File fdir = new File("examples");
    	
    	File f = new File(fdir, filename);
    	 
    //	try {
    		LemsProcess lemsp = new LemsProcess(f);
   	 
	 
			lemsp.readModel();	
	 		lemsp.process();
		
        
     //   } catch (Exception ex) {
     //           throw new RuntimeError(ex.getMessage());
     //   }

        E.info("  ****  Successfully ran example from file " + filename);
    }
    

    public static void main(String[] args)
    {
        ExamplesTest ct = new ExamplesTest();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        MainTest.checkResults(r);

    }

}