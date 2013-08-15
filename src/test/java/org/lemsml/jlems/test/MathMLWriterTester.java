package org.lemsml.jlems.test;

import org.junit.runner.Result;
import org.lemsml.jlems.core.expression.MathMLWriter;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.logging.DefaultLogger;
 
// not really a test - just for dev as yet


public class MathMLWriterTester {

	String[] expressions = {
			"a + b",
			"3 + (1.3e-4 + 5) + (3 *4)+ 4/(4.*45) + 34.2E-2 + sin(a + b) / cos(b + c)",
			"(v/VOLT)", 
			"rate * exp((v - midpoint)/scale)", 
			"(exp(73 * X))",
			"( 0.76 ) / TIME_SCALE", 
			"sqrt(4)", 
			"exp (x * (-0.059))", 
			"1.2 * 5 * 5.0e-3 * 6e34",
			"(exp ( (1e-3 * (-1.5 + (-1)/(1+(exp ((v-(-40))/5)))) * (v-11) * 9.648e4) / (8.315*(273.16+ (celsius) )) ))",
			"6"};
	
	
	public void generatMathML() throws ParseError, ContentError {
		
		StringBuilder sb = new StringBuilder();
		MathMLWriter mmw = new MathMLWriter();

		Parser p = new Parser();
		for (String s : expressions) {
			ParseTree pt = p.parseExpression(s);
			
			E.info("Source Expression: " + s);
			E.info("Mathml:");
			E.info(mmw.serialize(pt));
			
			sb.append("Source Expression: " + s + "\n");
			sb.append("Mathml:\n");
			sb.append(mmw.serialize(pt));
		}
		
	
	}
	
	
	
	
	 

 

	  

	public static void main(String[] args) {
		DefaultLogger.initialize();
		MathMLWriterTester wt = new MathMLWriterTester();
		try {
			wt.generatMathML();
		} catch (ParseError pe) {
			pe.printStackTrace();
		} catch (ContentError ce) {
			ce.printStackTrace();
		}
	}
	
	
	public void runJUnit() {	
		MathMLWriterTester ct = new MathMLWriterTester();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		Main.checkResults(r);

	}

}