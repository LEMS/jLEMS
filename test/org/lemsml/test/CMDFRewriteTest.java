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
import org.lemsml.io.CMDFMap;
import org.lemsml.run.ConnectionError;
import org.lemsml.serial.XMLSerializer;
import org.lemsml.sim.LemsProcess;
import org.lemsml.sim.Sim;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.FileUtil;
import org.lemsml.util.RuntimeError;

 
public class CMDFRewriteTest {

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
    public void testIZ() throws ContentError, ConnectionError, RuntimeError, ParseError, IOException  {
          executeExample("izhikevich-only.xml");
    }

    
    public void executeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException {
     
 		File fex = new File("examples");
 		File fcmdf = new File(fex, "cmdf");
		File fs = new File(fcmdf, filename);
		
		LemsProcess sim = new Sim(fs);
  
			sim.readMixedModel();	
	 		
			Lems lems = sim.getLems();
		
			String sout1 = XMLSerializer.serialize(lems, new CMDFMap());	 
		
			File fdirout = new File("tmp");
			File saveFile1 = new File(fdirout, "CMDF-save1.xml");
			FileUtil.writeStringToFile(sout1, saveFile1);
			
			LemsProcess sim2 = new Sim(saveFile1);
			sim2.readMixedModel();
			
			Lems lems2 = sim2.getLems();
			
			String sout2 = XMLSerializer.serialize(lems2, new CMDFMap());	 
	
			File saveFile2 = new File(fdirout, "CMDF-save2.xml");
			FileUtil.writeStringToFile(sout2, saveFile2);
			
			assert(sout1.equals(sout2));
			 
			
		 
	}
    
    
    


    public static void main(String[] args)
    {
        CMDFRewriteTest ct = new CMDFRewriteTest();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        MainTest.checkResults(r);

    }

}