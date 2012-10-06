package org.lemsml.jlemstests;
 
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.sim.LemsProcess;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.BuildException;
import org.lemsml.jlems.xml.ParseException;
import org.lemsml.jlems.xml.XMLElementReader;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.FileInclusionReader;
import org.lemsml.jlemsio.logging.MessagePrintlnHandler;
import org.lemsml.jlemsio.xmlio.XMLSerializer;
 
 


public class LemsExamplesReaderTest {

	public LemsExamplesReaderTest() {
	}

	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, FormatException, XMLException, IOException, ParseError {

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
	 		
				assertTrue(sout.equals(sout2));
			}
		}
	}
	

	public static void main(String[] args) {
		MessagePrintlnHandler.initialize();
		LemsExamplesReaderTest ct = new LemsExamplesReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}