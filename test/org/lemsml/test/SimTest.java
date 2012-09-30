/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.sim.LemsProcess;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.BuildException;
import org.lemsml.jlems.xml.ParseException;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.FileInclusionReader;

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
    	File f = new File(simpleModel);
    	FileInclusionReader fir = new FileInclusionReader(f);
        LemsProcess sim = new Sim(fir.read());

        E.info("testReadModel()");
        sim.readModel();

        sim.print();
        
    }


    /**
     * Test of build method, of class Sim.
     */
    @Test
    public void testBuild() throws Exception {
  
    	File f = new File(complexModel);
    	FileInclusionReader fir = new FileInclusionReader(f);
        
        LemsProcess sim = new Sim(fir.read());

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
     * @throws XMLException 
     * @throws FormatException 
     * @throws BuildException 
     * @throws ParseException 
     * @throws ParseError 
     */
    @Test
    public void testCanonicalText() throws ContentError, ParseError, ParseException, BuildException, FormatException, XMLException {
    	File f = new File(simpleModel);
    	FileInclusionReader fir = new FileInclusionReader(f);
        LemsProcess sim = new Sim(fir.read());

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