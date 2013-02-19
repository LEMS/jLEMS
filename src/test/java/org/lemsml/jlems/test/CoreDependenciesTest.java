/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.jlems.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.Result;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.util.FileUtil;
import org.lemsml.jlems.logging.E;

public class CoreDependenciesTest {

	@Test
	public void testCoreDependencies() throws IOException, DependencyException {
		File fsrc = new File("src/main/java/org/lemsml/jlems");
		int nfiles = checkDirectory(fsrc);
		E.info("Checked dependencies in " + nfiles + " source files");
	}

	private int checkDirectory(File dir) throws IOException,
			DependencyException {
		int ret = 0;
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				ret += checkDirectory(f);
			} else if (f.getName().endsWith(".java")) {
				checkFile(f);
				ret += 1;
			}
		}
		return ret;
	}

	private void checkFile(File f) throws IOException, DependencyException {
		String src = FileUtil.readStringFromFile(f);
		String[] lines = src.split("\n");
		for (String s : lines) {
			if (s.indexOf("import") == 0) {
				String error = "Dependency problem: Illegal import " + s + " in " + f.getName() + " - " + f.getAbsolutePath();
				Assert.assertFalse(error, s.indexOf(".reflect") >= 0);
				Assert.assertFalse(error, s.indexOf(".jlems.io.") >= 0);
				Assert.assertFalse(error, s.indexOf(".File") >= 0);
				Assert.assertFalse(error, s.indexOf("StreamTokenizer") >= 0);
				Assert.assertFalse(error, s.indexOf("*") >= 0 && !f.getName().equals("LemsFactory.java"));
				Assert.assertFalse(error, s.indexOf("Date") >= 0);
			}
		}

	}

	public static void main(String[] args) {
		DefaultLogger.initialize();

		CoreDependenciesTest ct = new CoreDependenciesTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}