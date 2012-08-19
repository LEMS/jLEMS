package org.lemsml.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * 
 * Test core functionality of lems
 * 
 * @author Padraig Gleeson, Robert Cannon
 * 
 */
public class MainTest {

	public static void main(String[] args) {
		System.out.println("Running the main LEMS  tests...");

		Result r = null;

		r = org.junit.runner.JUnitCore.runClasses(
				org.lemsml.test.XMLReaderTest.class,
				org.lemsml.test.ParserTest.class,
				org.lemsml.test.DimensionTest.class,
				org.lemsml.test.ComponentTypeTest.class,
				org.lemsml.test.LemsTest.class,
				org.lemsml.test.SimTest.class,
				org.lemsml.test.ExamplesTest.class);

		Date now = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss, EEE dd-MMM-yyyy");

		System.out.println("\n\nFinished  LEMS tests at " + formatter.format(now) + ".");

		checkResults(r);

	}

	public static void checkResults(Result r) {

		if (!r.wasSuccessful()) {
			for (Failure f : r.getFailures()) {
				System.out.println("Failure: " + f.getDescription());
				System.out.println("Exception: " + f.getMessage());
				System.out.println("Trace: " + f.getTrace());
			}
		}

		System.out.println("");
		System.out.println("Results:");
		System.out.println("     Total      " + r.getRunCount());
		System.out.println("     Ignored    " + r.getIgnoreCount());
		System.out.println("     Failed     " + r.getFailures().size());
		System.out.println("     Succeeded  " + (r.getRunCount() - r.getFailures().size() - r.getIgnoreCount()));
		System.out.println("");

		if (r.wasSuccessful()) {
			System.out.println("All tests completed successfully");
		} else {
			System.out.println("FAILED " + r.getFailures().size() + " test(s)");
		}

	}
}