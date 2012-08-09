/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.sim.LemsProcess;
import org.lemsml.sim.Sim;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

/**
 *
 * @author Padraig
 */
public class SimTest {


   String simpleModel = "examples/ex2dims.xml";
   String complexModel = "examples/example8.xml";
  

    public SimTest() {
    }


    /**
     * Test of readModel method, of class Sim.
     */
    @Test
    public void testReadAndPrintModel() throws Exception {

        LemsProcess sim = new Sim(new File(simpleModel));

        E.info("testReadModel()");
        sim.readModel();

        sim.print();
        
    }


    /**
     * Test of build method, of class Sim.
     */
    @Test
    public void testBuild() throws Exception {

        LemsProcess sim = new Sim(new File(complexModel));

        E.info("testBuild()");
        sim.readModel();

        E.info("Component types: " + sim.getLems().componentTypes.listAsText());
        E.info("Components: " + sim.getLems().components.listAsText());

        //sim.build();

        //sim.print();
    }

    @Test
    public void testStringRead() throws Exception {

        //String src = "<Lems>\n<Dimension name=\"time\" t=\"1\"/>\n</Lems>";
        String src = "<Lems>\n"
                /*+ "<DefaultRun component=\"sim1\"/>\n"*/
                + "<Include file=\"examples/example1.xml\" />\n"
                + "</Lems>";
        LemsProcess sim = new Sim(src);

        E.info("testStringRead()");
        sim.readModel();

        E.info("Components: "+sim.getLems().components.listAsText());

        /*sim.build();

        sim.print();*/


    }
    
   
    /**
     * Test of canonicalText method, of class Sim.
     */
    @Test
    public void testCanonicalText() throws ContentError {

        LemsProcess sim = new Sim(new File(simpleModel));

        E.info("testReadModel()");
        sim.readModel();

        String ct = sim.canonicalText();

        E.info(ct);

        assertTrue(ct.indexOf("<name>voltage</name>")>0);
    }


    public static void main(String[] args)
    {
        SimTest ct = new SimTest();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        MainTest.checkResults(r);

    }

}