package org.lemsml.test;
 
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.io.FormatException;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;
import org.lemsml.xml.BuildException;
import org.lemsml.xml.ParseException;
import org.lemsml.xml.XMLReader;

/**
 * 
 * @author Padraig
 */
public class XMLReaderTest {

	public XMLReaderTest() {
	}

	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, FormatException {
		System.out.println("Testing readFromString()...");

		String testString = "<Lems>"
				+ "<Dimension name=\"voltage\" m=\"1\" l=\"2\" t=\"-3\" i=\"-1\"/>"
				+ "<Unit symbol=\"mV\" dimension=\"voltage\" powTen=\"-3\"/>"
				+ "</Lems>";

		XMLReader xmlReader = new XMLReader();
	    xmlReader.addSearchPackage(Lems.class.getPackage());
          
		Object obj = xmlReader.readFromString(testString);

		System.out.println("Object returned (" + obj.getClass() + "): " + obj);

		assertTrue(obj instanceof Lems);
	}

	public static void main(String[] args) {
		XMLReaderTest ct = new XMLReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}