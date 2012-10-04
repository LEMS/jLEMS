package org.lemsml.test;
 
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.BuildException;
import org.lemsml.jlems.xml.ParseException;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.xmlio.ReflectionInstantiator;
import org.lemsml.jlemsio.xmlio.XMLReader;


/**
 * 
 * @author Padraig
 */
public class XMLReaderTest {

	public XMLReaderTest() {
	}

	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, FormatException, XMLException {
		E.info("Testing readFromString()...");

		String testString = "<Lems>"
				+ "<Dimension name=\"voltage\" m=\"1\" l=\"2\" t=\"-3\" i=\"-1\"/>"
				+ "<Unit symbol=\"mV\" dimension=\"voltage\" powTen=\"-3\"/>"
				+ "</Lems>";

		XMLReader xmlReader = new XMLReader(new ReflectionInstantiator()); 
	  //  xmlReader.addSearchPackage(Lems.class.getPackage());
          
		Object obj = xmlReader.readFromString(testString);

		E.info("Object returned (" + obj.getClass() + "): " + obj);

		assertTrue(obj instanceof Lems);
	}

	public static void main(String[] args) {
		XMLReaderTest ct = new XMLReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}