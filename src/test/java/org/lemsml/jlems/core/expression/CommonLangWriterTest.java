/**
 * 
 */
package org.lemsml.jlems.core.expression;

import org.junit.Test;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

/**
 * @author boris
 *
 */
public class CommonLangWriterTest {

	@Test
	public void test() throws ParseError, ContentError {
 		Parser p = new Parser();
  		ParseTree pt;
  		String ccode, pcode, mcode;

 		CWriter cwriter = new CWriter();
 		PythonWriter pwriter = new PythonWriter();
 		MathMLWriter mwriter = new MathMLWriter();

 		String[] tests = new String[] { "(v/VOLT)", "rate * exp((v - midpoint)/scale)",
 				"(exp(73 * X))", "-(0.2 - -8.8) + 3^2" };

 		for (String test : tests) {
			E.info("Parsing: " + test + "\n");
			pt = p.parseExpression(test);
			ccode =  cwriter.serialize(pt);
			pcode =  pwriter.serialize(pt);
			//mcode =  mwriter.serialize(pt);
			E.info("C code generated: " + ccode + "\n");
			E.info("Python code generated: " + pcode + "\n");
			//E.info("MathML code generated: " + mcode + "\n");
			//E.info("exp evals to " + pt.makeFloatEvaluator().evalD(null));
 		}


	}

}
