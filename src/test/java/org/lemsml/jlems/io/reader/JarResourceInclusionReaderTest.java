package org.lemsml.jlems.io.reader;

import org.lemsml.jlems.core.expression.*;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.test.Main;

/**
 *
 * @author Padraig
 */
public class JarResourceInclusionReaderTest {


	@Test
	public void testFindingFiles() throws ParseError, ContentError {
 		

        JarResourceInclusionReader jrir = new JarResourceInclusionReader("target/jlems-0.9.8.4.jar");
                
        String[] paths = new String[]{"target/classes/org/lemsml/../../../../lems","../jLEMS/lems"};
        for (String path: paths){        
            System.out.println(">> "+path+" -> "+jrir.getRelativeContent(JarResourceInclusionReader.FILE,path));
        }

    }


	public static void main(String[] args) {
		DefaultLogger.initialize();
		JarResourceInclusionReaderTest ct = new JarResourceInclusionReaderTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		Main.checkResults(r);

	}

}