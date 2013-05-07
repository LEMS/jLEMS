package org.lemsml.jlems.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.xml.XMLElementReader;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.util.FileUtil;



/**
 * 
 * @author padraig
 */
public class VersionTest {
 

	@Test
	public void testVersions() throws ParseError, ContentError, IOException {
		E.info("Testing version usage...");

    	String lemsScript = FileUtil.readStringFromFile(new File("lems"));
    	
    	assert(lemsScript.contains(org.lemsml.jlems.io.Main.VERSION));

    	String lemsBatScript = FileUtil.readStringFromFile(new File("lems.bat"));
    	
    	assert(lemsBatScript.contains(org.lemsml.jlems.io.Main.VERSION));
    	
    	String pomFile = FileUtil.readStringFromFile(new File("pom.xml"));

    	XMLElementReader xer = new XMLElementReader(pomFile);
    	assertEquals(org.lemsml.jlems.io.Main.VERSION, xer.getRootElement().getElement("version").getBody());
    	

		E.info("Version usage ok...");
	}

	public static void main(String[] args) {
		DefaultLogger.initialize();
		VersionTest ct = new VersionTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		Main.checkResults(r);

	}

}