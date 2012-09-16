package org.lemsml.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.lemsml.util.E;

/**
 * 
 * Test core functionality of lems
 * 
 * @author Padraig Gleeson, Robert Cannon
 * 
 */
public class MainTest {

	public static void main(String[] args) {
		E.info("Running the main LEMS  tests...");

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

		E.info("Finished  LEMS tests at " + formatter.format(now) + ".");

		checkResults(r);

	}

	public static void checkResults(Result r) {

		StringBuilder sb = new StringBuilder();
		if (!r.wasSuccessful()) {
			for (Failure f : r.getFailures()) {
				sb.append("Failure: " + f.getDescription() + "\n");
				sb.append("Exception: " + f.getMessage() + "\n");
				sb.append("Trace: " + f.getTrace() + "\n");
			}
		}

		sb.append("\n");
		sb.append("Results:\n");
		sb.append("     Total      " + r.getRunCount() + "\n");
		sb.append("     Ignored    " + r.getIgnoreCount() + "\n");
		sb.append("     Failed     " + r.getFailures().size() + "\n");
		sb.append("     Succeeded  " + (r.getRunCount() - r.getFailures().size() - r.getIgnoreCount()) + "\n");
		sb.append("\n");
		
		if (r.wasSuccessful()) {
			sb.append("All tests completed successfully\n");
		} else {
			sb.append("FAILED " + r.getFailures().size() + " test(s)\n");
		}
		E.info(sb.toString());
	}
}