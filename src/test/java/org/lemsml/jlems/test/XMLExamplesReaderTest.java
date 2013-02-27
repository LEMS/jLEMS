package org.lemsml.jlems.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.core.xml.XMLElementReader;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.util.FileUtil;

public class XMLExamplesReaderTest {

	@Test
	public void testReadFromString() throws ParseException, BuildException,
			ContentError, XMLException, IOException {

		URL url = this.getClass().getResource("/");
		File fdir = new File(url.getFile());

		for (File fx : fdir.listFiles()) {
			if (fx.isFile()) {
				String stxt = FileUtil.readStringFromFile(fx);
				XMLElementReader xmlReader = new XMLElementReader(stxt);
				XMLElement xe = xmlReader.getRootElement();

				String rewrite = xe.serialize();

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
					E.info("N.B. To pass this test, childless elements MUST be written "
							+ "'<Element a=\"val\"/>' and not '<Element a=\"val\"></Element>'");
				}

				assertEquals("Rewrite matches", sns.length(), tns.length());
			}
		}
	}

	public static void main(String[] args) {
		DefaultLogger.initialize();
		XMLExamplesReaderTest ct = new XMLExamplesReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}