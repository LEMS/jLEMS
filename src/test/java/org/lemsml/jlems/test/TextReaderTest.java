package org.lemsml.jlems.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.xml.TextElementReader;
import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.util.FileUtil;

public class TextReaderTest
{

	//@Test 
	//FIXME Not passing, example1.txt doesn't exist
	public void testReadFromString() throws ParseException, BuildException, ContentError, XMLException, IOException
	{

		URL url = this.getClass().getResource("/example1.txt");
		Assert.assertNotNull("Resource file not found", url);
		File fex = new File(url.getFile());
		String testString = FileUtil.readStringFromFile(fex);

		E.info("reading test string " + testString.length());

		TextElementReader textReader = new TextElementReader(testString);
		XMLElement xe = textReader.getRootElement();

		E.info(xe.serialize());
	}

	public static void main(String[] args)
	{
		DefaultLogger.initialize();

		TextReaderTest ct = new TextReaderTest();

		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}