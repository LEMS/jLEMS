package org.lemsml.jlems.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.core.expression.AndNode;
import org.lemsml.jlems.core.expression.OrNode;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.logging.DefaultLogger;

/**
 * 
 * @author Padraig
 */
public class ParserTest {

	@Test
	public void testEvaluatingDouble() throws ParseError, ContentError {
 		Parser p = new Parser();
 		String src1 = "a + b";
 		ParseTree pt1 = p.parseExpression(src1); 		
 		// DoubleEvaluator dev = 
 				pt1.makeFloatEvaluator();
 		
		String src = "3 + (1.3e-4 + 5) + (3 *4)+ 4/(4.*45) + 34.2E-2 + sin(a + b) / cos(b + c)";
		ParseTree pt = p.parseExpression(src);

		HashMap<String, Double> valHM = new HashMap<String, Double>();
		double a = 2;
		double b = 3;
		double c = 4;
		double d = 5;

		valHM.put("a", a);
		valHM.put("b", b);
		valHM.put("c", c);
		valHM.put("d", d);
		double eval = pt.makeFloatEvaluator().evalD(valHM);
		double res = 3 + (1.3e-4 + 5) + (3 * 4) + 4 / (4. * 45) + 34.2E-2 + Math.sin(a + b) / Math.cos(b + c);
		assertEquals(res, eval, 0);

		c = 14;
		d = 2;
		valHM.put("c", c);
		valHM.put("d", d);
		eval = pt.makeFloatEvaluator().evalD(valHM);
		res = 3 + (1.3e-4 + 5) + (3 * 4) + 4 / (4. * 45) + 34.2E-2 + Math.sin(a + b) / Math.cos(b + c);
		assertEquals(res, eval, 0);
		src = "ln(c)";
		pt = p.parseExpression(src);
		eval = pt.makeFloatEvaluator().evalD(valHM);
		res = Math.log(c);
		assertEquals(res, eval, 0);

		src = "5e24";
		pt = p.parseExpression(src);
		eval = pt.makeFloatEvaluator().evalD(valHM);
		res = 5e24;
		assertEquals(res, eval, 0);
		


		String[] tests = new String[] { "(v/VOLT)", "rate * exp((v - midpoint)/scale)", "(exp(73 * X))",
				"( 0.76 ) / TIME_SCALE", "sqrt(4)", "exp (x * (-0.059))", "1.2 * 5 * 5.0e-3 * 6e34" };

		for (String test : tests) {
			pt = p.parseExpression(test);
			E.info("Parsed to: " + pt.toString());
		}

	
		
		ParseTree de1 = p.parseExpression("(exp ( (1e-3 * (-1.5 + (-1)/(1+(exp ((v-(-40))/5)))) * (v-11) * 9.648e4) / (8.315*(273.16+ (celsius) )) ))");
		ParseTree de2 = p.parseExpression("(exp ( ( ( (-1.5) - 1/(1+(exp ((v-(-40))/5)))) * (v-11) * 96.48) / (8.315*(273.16+ (celsius) )) ))");
		  
		
		valHM.clear();
		valHM.put("v", -65d);
		valHM.put("celsius", 30d);
		double eval1 = de1.makeFloatEvaluator().evalD(valHM);
		E.info("1 evaluates to " + eval1);
		double eval2 = de2.makeFloatEvaluator().evalD(valHM);
		E.info("2 evaluates to " + eval2);

		assertEquals(eval1, eval2, 0);
	}

	@Test
	public void testEvaluatingBoolean() throws ParseError, ContentError {
		Parser p = new Parser();
		String src = "3 + 4^a .lt. 5 * b";
		ParseTree pt = p.parseCondition(src);

		HashMap<String, Double> valHM = new HashMap<String, Double>();
		valHM.put("a", 4.);
		valHM.put("b", 5.);
		boolean res = pt.makeBooleanEvaluator().evalB(valHM);
		assertFalse(src,res);

		valHM.put("a", 1.);
		valHM.put("b", 50.);
		res = pt.makeBooleanEvaluator().evalB(valHM);
		assertTrue(src, res);

		pt = p.parseCondition("a .geq. b");
		assertFalse(src, pt.makeBooleanEvaluator().evalB(valHM));
		pt = p.parseCondition("a .geq. a");
		assertTrue(src, pt.makeBooleanEvaluator().evalB(valHM));
		pt = p.parseCondition("b .neq. 33");
		assertTrue(src, pt.makeBooleanEvaluator().evalB(valHM));
		pt = p.parseCondition("b .leq. 33");
		assertFalse(src, pt.makeBooleanEvaluator().evalB(valHM));

		pt = p.parseCondition("b .geq. 33 " + AndNode.SYMBOL + " a .leq. b");
		assertTrue(src, pt.makeBooleanEvaluator().evalB(valHM));
		pt = p.parseCondition("b .leq. 33 " + AndNode.SYMBOL + " a .leq. b");
		assertFalse(src, pt.makeBooleanEvaluator().evalB(valHM));
		ParseTree ptOr = p.parseCondition("b .leq. 33 " + OrNode.SYMBOL + " a .leq. b");
		assertTrue(src, ptOr.makeBooleanEvaluator().evalB(valHM));
		ptOr = p.parseCondition("b .geq. 33 " + OrNode.SYMBOL + " a .geq. b");
		assertTrue(src, ptOr.makeBooleanEvaluator().evalB(valHM));

		ptOr = p.parseCondition("b .leq. 33 " + OrNode.SYMBOL + " a .geq. b");
		assertFalse(src, ptOr.makeBooleanEvaluator().evalB(valHM));

		src = "(X)  .gt.  (-0.046)";
		pt = p.parseCondition(src);
		valHM = new HashMap<String, Double>();
		valHM.put("X", -0.2);
		res = pt.makeBooleanEvaluator().evalB(valHM);
		assertFalse(src, res);
	}

	public static void main(String[] args) {
		DefaultLogger.initialize();
		ParserTest ct = new ParserTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		Main.checkResults(r);

	}

}