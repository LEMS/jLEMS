package org.lemsml.jlems.core.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.test.Main;

/**
 * 
 * @author Padraig
 */
public class ExpressionTest {
    
    
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

		testEval("5e24", 5e24, valHM);
		testEval("5e-24", 5e-24, valHM);
		

		testEval("ceil(1.00001)", 2, null);
		testEval("ceil(0)", 0, null);
		testEval("ceil(b/a)", 2, valHM);
		

		testEval("abs(2)", 2, valHM);
		testEval("abs(-2)", 2, valHM);

		testEval("factorial(2)", 2, valHM);
		testEval("factorial(9)", 362880, valHM);
		checkFailure("factorial(-10)", 0, null);

		testEval("log(100)", Math.log(100), null);
		checkFailure("log(-10)", 6, null);

		testEval("sqrt(9)", 3, valHM);

		checkFailure("sqrt(-10)", 0, null);
	

		String[] tests = new String[] { "(v/VOLT)", "rate * exp((v - midpoint)/scale)", "(exp(73 * X))",
				"( 0.76 ) / TIME_SCALE", "sqrt(4)", "exp (x * (-0.059))", "1.2 * 5 * 5.0e-3 * 6e34", "ceil(2.4)" };

		for (String test : tests) {
			E.info("Parsing: " + test);
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
        
        
        testEval("2^3", 8, null);
        testEval("2.5^2.5", Math.pow(2.5, 2.5), null);
        checkFailure("(-2.5)^2.5", Math.pow(-2.5, 2.5), null);
        testEval("2.5^-2.5", Math.pow(2.5, -2.5), null);
		
	}

	private void checkFailure(String expr, double val, HashMap<String, Double> valHM)
	{
		try {
	 		Parser p = new Parser();
			E.info("- Parsing: "+expr+"; = "+val+"?");
			ParseTree pt = p.parseExpression(expr);
			if (valHM==null) valHM = new HashMap<String, Double>();
			double eval = pt.makeFloatEvaluator().evalD(valHM);
			E.info("- Parsed to: " + pt.toString()+", eval: "+eval);
			if (Double.isNaN(eval))
				return;
			fail("Expression: "+expr+" was supposed to lead to an exception, but didn't!");
		} catch (Exception e) {
			//e.printStackTrace();
		} 
	}
	
	
	private void testEval(String expr, double val, HashMap<String, Double> valHM) throws ParseError, ContentError
	{
 		Parser p = new Parser();
		E.info("- Parsing: "+expr+"; = "+val+"?");
		ParseTree pt = p.parseExpression(expr);
		if (valHM==null) valHM = new HashMap<String, Double>();
		double eval = pt.makeFloatEvaluator().evalD(valHM);
		E.info("- Parsed to: " + pt.toString()+", eval: "+eval);
		assertEquals(val, eval, 0);
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
		

		src = "S1 .gt. 1.5E-5";
		pt = p.parseCondition(src);
		valHM = new HashMap<String, Double>();
		valHM.put("S1", -0.2);
		res = pt.makeBooleanEvaluator().evalB(valHM);
		assertFalse(src, res);
	}
    
	@Test
	public void testEvaluatingDimensional() throws ParseError, ContentError {
        
        HashMap<String, Dimensional> adml = new HashMap<String, Dimensional>();
        
        
        
        ExprDimensional a = new ExprDimensional();
        a.setDoubleValue(1);
        a.setT(1);
        adml.put("a", a);
        
        ExprDimensional b = new ExprDimensional();
        b.setDoubleValue(2);
        b.setT(1); 
        adml.put("b", b);
        
        ExprDimensional b2 = new ExprDimensional();
        b2.setDoubleValue(2);
        //b2.setT(2); 
        adml.put("b2", b2);
        
        
        Parser p = new Parser();
 		String src1 = "a .lt. b";
 		ParseTree pt1 = p.parseCondition(src1); 
        System.out.println("pt1: "+pt1);
        AbstractComparisonNode node = (AbstractComparisonNode)pt1.root;
        Dimensional d = node.dimop(a, b);
        System.out.println("d: "+d);
        try {
            d = node.dimop(a, b2);
            fail("Should be incompatible: "+a+" and "+b2);
        } catch (ContentError e) {
            System.out.println("Correctly incompatible: "+a+" and "+b2);
        }
        
        
        
    }
    

	public static void main(String[] args) {
		DefaultLogger.initialize();
		ExpressionTest ct = new ExpressionTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		Main.checkResults(r);

	}

}