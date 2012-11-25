package org.lemsml.jlemstests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlemsio.logging.DefaultLogger;

public final class MainTest {

	
	private MainTest() {
		
	}
	
	public static void main(String[] args) {
		DefaultLogger.initialize();
		
		E.info("Running tests");		
		
		Result r = null;

		r = org.junit.runner.JUnitCore.runClasses(
				org.lemsml.jlemstests.ParserTest.class,
				org.lemsml.jlemstests.DimensionTest.class,
				org.lemsml.jlemstests.XMLReaderTest.class,
				org.lemsml.jlemstests.XMLExamplesReaderTest.class,
				org.lemsml.jlemstests.LemsExamplesReaderTest.class,
				org.lemsml.jlemstests.ExamplesTest.class,
				org.lemsml.jlemstests.ComponentFlatteningTest.class,
				org.lemsml.jlemstests.CoreDependenciesTest.class);

		Date now = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss, EEE dd-MMM-yyyy", Locale.ENGLISH);

		E.info("Finished tests at " + formatter.format(now) + ".");

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