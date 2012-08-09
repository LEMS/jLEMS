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
import org.lemsml.sim.Sim;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;

 
public class CMDFExamplesTest {

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
    public void testExample1() throws ContentError, ConnectionError, RuntimeError, ParseError  {
          executeExample("izhikevich.xml");
    }

    
    public void executeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError {
    	File fdir = new File("examples");
    	File fcmdf = new File(fdir, "cmdf");
    	
    	
    	File f = new File(fcmdf, filename);
    	Sim sim = new Sim(f);

        sim.readMixedModel();
        sim.build();
        try {
             sim.run(false);
        } catch (IOException ex) {
                throw new RuntimeError(ex.getMessage());
        }

        E.info("  ****  Successfully ran example from file " + filename);

    }


    public static void main(String[] args)
    {
        CMDFExamplesTest ct = new CMDFExamplesTest();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        MainTest.checkResults(r);

    }

}