package org.lemsml.jlemstests;
 
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.TextElementReader;
import org.lemsml.jlems.xml.XMLElement;
import org.lemsml.jlems.xml.XMLElementReader;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsio.util.FileUtil;
 
 
public class TextReaderTest {

	public TextReaderTest() {
	}

	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, XMLException, IOException {
  
		File fex = new File("build/compact-text-examples/example1.txt");
		String testString = FileUtil.readStringFromFile(fex);
		
		E.info("reading test string " + testString.length());
		
		TextElementReader textReader = new TextElementReader(testString); 
		XMLElement xe = textReader.getRootElement();
	  
		E.info(xe.serialize());
	}
	

	public static void main(String[] args) {
		DefaultLogger.initialize();
		
		TextReaderTest ct = new TextReaderTest(); 
		
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());		
		MainTest.checkResults(r);

	}

}