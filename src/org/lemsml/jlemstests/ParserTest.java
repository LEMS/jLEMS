package org.lemsml.jlemstests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.AndNode;
import org.lemsml.jlems.expression.BooleanEvaluable;
import org.lemsml.jlems.expression.DoubleEvaluable;
import org.lemsml.jlems.expression.OrNode;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlemsio.logging.MessagePrintlnHandler;

/**
 * 
 * @author Padraig
 */
public class ParserTest {

	@Test
	public void testEvaluatingDouble() throws ParseError {
 		Parser p = new Parser();
		String src = "3 + (1.3e-4 + 5) + (3 *4)+ 4/(4.*45) + 34.2E-2 + sin(a + b) / cos(b + c)";
		DoubleEvaluable root = p.parseExpression(src);

		HashMap<String, Double> valHM = new HashMap<String, Double>();
		double a = 2;
		double b = 3;
		double c = 4;
		double d = 5;

		valHM.put("a", a);
		valHM.put("b", b);
		valHM.put("c", c);
		valHM.put("d", d);
		double eval = root.evalD(valHM);
		double res = 3 + (1.3e-4 + 5) + (3 * 4) + 4 / (4. * 45) + 34.2E-2 + Math.sin(a + b) / Math.cos(b + c);
		assertEquals(res, eval, 0);

		c = 14;
		d = 2;
		valHM.put("c", c);
		valHM.put("d", d);
		eval = root.evalD(valHM);
		res = 3 + (1.3e-4 + 5) + (3 * 4) + 4 / (4. * 45) + 34.2E-2 + Math.sin(a + b) / Math.cos(b + c);
		assertEquals(res, eval, 0);
		src = "ln(c)";
		root = p.parseExpression(src);
		eval = root.evalD(valHM);
		res = Math.log(c);
		assertEquals(res, eval, 0);

		src = "5e24";
		root = p.parseExpression(src);
		eval = root.evalD(valHM);
		res = 5e24;
		assertEquals(res, eval, 0);

		String[] tests = new String[] { "(v/VOLT)", "rate * exp((v - midpoint)/scale)", "(exp(73 * X))",
				"( 0.76 ) / TIME_SCALE", "sqrt(4)", "exp (x * (-0.059))" };

		for (String test : tests) {
			root = p.parseExpression(test);
			// E.info("Parsed to: " +
			// root.toString()+", ("+root.getClass()+")");
		}
	}

	@Test
	public void testEvaluatingBoolean() throws ParseError {
		Parser p = new Parser();
		String src = "3 + 4^a .lt. 5 * b";
		BooleanEvaluable bev = p.parseCondition(src);

		HashMap<String, Double> valHM = new HashMap<String, Double>();
		valHM.put("a", 4.);
		valHM.put("b", 5.);
		boolean res = bev.evalB(valHM);
		assertFalse(res);

		valHM.put("a", 1.);
		valHM.put("b", 50.);
		res = bev.evalB(valHM);
		assertTrue(res);

		bev = p.parseCondition("a .geq. b");
		assertFalse(bev.evalB(valHM));
		bev = p.parseCondition("a .geq. a");
		assertTrue(bev.evalB(valHM));
		bev = p.parseCondition("b .neq. 33");
		assertTrue(bev.evalB(valHM));
		bev = p.parseCondition("b .leq. 33");
		assertFalse(bev.evalB(valHM));

		bev = p.parseCondition("b .geq. 33 " + AndNode.SYMBOL + " a .leq. b");
		assertTrue(bev.evalB(valHM));
		bev = p.parseCondition("b .leq. 33 " + AndNode.SYMBOL + " a .leq. b");
		assertFalse(bev.evalB(valHM));
		BooleanEvaluable bevOr = p.parseCondition("b .leq. 33 " + OrNode.SYMBOL + " a .leq. b");
		assertTrue(bevOr.evalB(valHM));
		bevOr = p.parseCondition("b .geq. 33 " + OrNode.SYMBOL + " a .geq. b");
		assertTrue(bevOr.evalB(valHM));

		bevOr = p.parseCondition("b .leq. 33 " + OrNode.SYMBOL + " a .geq. b");
		assertFalse(bevOr.evalB(valHM));

		src = "(X)  .gt.  (-0.046)";
		bev = p.parseCondition(src);
		valHM = new HashMap<String, Double>();
		valHM.put("X", -0.2);
		res = bev.evalB(valHM);
		assertFalse(res);
	}

	public static void main(String[] args) {
		MessagePrintlnHandler.initialize();
		ParserTest ct = new ParserTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}