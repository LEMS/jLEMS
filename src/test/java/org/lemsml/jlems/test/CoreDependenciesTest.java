/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.jlems.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.util.FileUtil;
import org.lemsml.jlems.logging.E;

 
public class CoreDependenciesTest {

 
    @Test
    public void testCoreDependencies() throws IOException, DependencyException {
    	File fsrc = new File("src/org/lemsml/jlems");
    	int nfiles = checkDirectory(fsrc);
    	E.info("Checked dependencies in " + nfiles + " source files");
    }
    
    private int checkDirectory(File dir) throws IOException, DependencyException {
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
    			if (s.indexOf(".reflect") >= 0 ||
    				s.indexOf(".jlemsio.") >= 0 ||	
    				s.indexOf(".File") >= 0 || 
    				s.indexOf("StreamTokenizer") >= 0 ||
    				(s.indexOf("*") >= 0 && !f.getName().equals("LemsFactory.java")) || 
    				s.indexOf("Date") >= 0) {
    					E.info("Illegal import " + s);
    					throw new DependencyException("Illegal import " + s + " in " + f.getName() + " - " + f.getAbsolutePath());
    			}
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