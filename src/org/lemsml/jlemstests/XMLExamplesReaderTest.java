package org.lemsml.jlemstests;
 
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.BuildException;
import org.lemsml.jlems.xml.ParseException;
import org.lemsml.jlems.xml.XMLElement;
import org.lemsml.jlems.xml.XMLElementReader;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.FileUtil;
import org.lemsml.jlemsio.logging.MessagePrintlnHandler;
 
 


public class XMLExamplesReaderTest {

	public XMLExamplesReaderTest() {
	}

	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, FormatException, XMLException, IOException {

		File fdir = new File("examples");
		for (File fx : fdir.listFiles()) {
			String stxt = FileUtil.readStringFromFile(fx);
			XMLElementReader xmlReader = new XMLElementReader(stxt); 
			XMLElement xe = xmlReader.getRootElement();
			
			String rewrite= xe.serialize();

			String snc = XMLElementReader.deComment(stxt);
			String sns = XMLElementReader.deSpace(snc);
			String tns = XMLElementReader.deSpace(rewrite);
			
			int snl = sns.length();
			int tnl = tns.length();
			if (snl == tnl) {
				E.info("Read/write OK for " + fx);
			} else {
				E.info("Read/write failure for " + fx.getName());
				E.info("Source:    " + sns);
				E.info("Rewritten: " + tns);
				E.info("N.B. To pass this test, childless elements MUST be written " +
				"'<Element a=\"val\"/>' and not '<Element a=\"val\"></Element>'");
			}
	 		
			assertTrue(sns.length() == tns.length());
		}
	}
	

	public static void main(String[] args) {
		MessagePrintlnHandler.initialize();
		XMLExamplesReaderTest ct = new XMLExamplesReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}