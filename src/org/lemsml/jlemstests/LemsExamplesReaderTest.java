package org.lemsml.jlemstests;
  
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.LemsProcess;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.xml.XMLElementReader;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsio.reader.FileInclusionReader;
import org.lemsml.jlemsio.xmlio.XMLSerializer;
 


public class LemsExamplesReaderTest {
 
	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, XMLException, IOException, ParseError {

		File fdir = new File("examples");
		for (File fx : fdir.listFiles()) {
			
			if (fx.getName().startsWith("example")) {
				FileInclusionReader fir = new FileInclusionReader(fx);
				String fullText = fir.read();
				
				LemsProcess lemsProcess = new LemsProcess(fullText);

				lemsProcess.readModel();
				Lems lems = lemsProcess.getLems();

				String sout = XMLSerializer.serialize(lems);	 
		 
				LemsProcess lp2 = new LemsProcess(sout);
				lp2.readModel();
				Lems lems2 = lp2.getLems();
			
				String sout2 = XMLSerializer.serialize(lems2);	 		
	 
				if (sout.equals(sout2)) {
					E.info("Lems Read/write OK for " + fx);
				} else {
					E.info("Read/write failure for " + fx.getName());
					E.info("Exported:  " + XMLElementReader.deSpace(sout));
					E.info("Reloaded:  " + XMLElementReader.deSpace(sout2));
					E.info("Reread failed on " + sout);
				}
	 		
				assertEquals("string read", sout, sout2);
			}
		}
	}
	

	public static void main(String[] args) {
		DefaultLogger.initialize();
		LemsExamplesReaderTest ct = new LemsExamplesReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}