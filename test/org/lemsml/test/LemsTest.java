/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.serial.XMLSerializer;
import org.lemsml.jlems.sim.LemsProcess;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.BuildException;
import org.lemsml.jlems.xml.ParseException;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.FileInclusionReader;
import org.lemsml.jlemsio.FileUtil;

/**
 * 
 * @author padraig
 */
public class LemsTest {

	public static boolean showGui = false;

	public LemsTest() {
	}

	@Test
	public void testLoad() throws ParseException, BuildException, ContentError, ConnectionError, ParseError, IOException, FormatException, XMLException {

		File f = new File("examples/example1.xml");
		E.info("Loading LEMS file from: " + f.getAbsolutePath());

		FileInclusionReader fir = new FileInclusionReader(f);
		LemsProcess sim = new Sim(fir.read());

		sim.readModel();
		// sim.build();

		Lems lems = sim.getLems();

		E.info("Found " + lems.getComponentTypes().size() + " Component Types:\n");


		File fdir = new File("tmp");
		fdir.mkdirs();
		File saveFile = new File(fdir, "Lems-saved.xml");

		saveFile.delete();

		String sout = XMLSerializer.serialize(lems);	 
		FileUtil.writeStringToFile(sout, saveFile);
		assertTrue(saveFile.exists());

		
		FileInclusionReader fir2 = new FileInclusionReader(saveFile);
		LemsProcess sim2 = new Sim(fir2.read());
		sim2.readModel();
		Lems lems2 = sim2.getLems();
		
		String sout2 = XMLSerializer.serialize(lems2);	 
		
		
		if (!sout2.equals(sout)) {
			File saveFile2 = new File(fdir, "Lems-saved2.xml");
			FileUtil.writeStringToFile(sout2, saveFile2);
			E.info("Reread produced different output: see tmp/" + saveFile.getName() + " and tmp/" + saveFile2.getName());
		}
		
		assertTrue(sout2.equals(sout));

		// TODO - read the saved file in, write it out again and check we have exactly the same thing again
	}

	public static void main(String[] args) {
		LemsTest ct = new LemsTest();
		showGui = true;
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);
	}

}