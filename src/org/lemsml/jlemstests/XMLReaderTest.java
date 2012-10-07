package org.lemsml.jlemstests;
 
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.XMLElement;
import org.lemsml.jlems.xml.XMLElementReader;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.logging.DefaultLogger;
 
/**
 * 
 * @author Padraig
 */
public class XMLReaderTest {

	public XMLReaderTest() {
	}

	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, XMLException {
 
		String testString = "<Lems>"
				+ "<Dimension name=\"voltage\" m=\"1\" l=\"2\" t=\"-3\" i=\"-1\"/>"
				+ "<Unit symbol=\"mV\" dimension=\"voltage\" powTen=\"-3\"/>"
				+ "</Lems>  ";
		
		E.info("Testing read of simple XML string");
	
		XMLElementReader xmlReader = new XMLElementReader(testString); 
		XMLElement xe = xmlReader.getRootElement();
	 
		assertTrue(xe.getTag().equals("Lems"));
	}
	

	public static void main(String[] args) {
		DefaultLogger.initialize();
		
		XMLReaderTest ct = new XMLReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		
		MainTest.checkResults(r);

	}

}