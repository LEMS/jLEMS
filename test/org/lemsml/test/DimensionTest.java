/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.DimensionalQuantity;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.type.QuantityReader;
import org.lemsml.jlems.type.Unit;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.DimensionsExport;
import org.lemsml.jlems.util.E;

/**
 * 
 * @author padraig
 */
public class DimensionTest {

	public DimensionTest() {
	}

	@Test
	public void testDimensions() throws ParseError, ContentError {
		Dimension current = new Dimension("current", 0, 0, 0, 1);
		Dimension current2 = new Dimension("current2", 0, 0, 0, 1);

		assertTrue(current.matches(current2));

		Dimension temp = new Dimension("temperature", 0, 0, 0, 0, 1, 0);

		assertFalse(current.matches(temp));

		assertEquals(DimensionsExport.getSIUnit(temp), "K");

		Unit kelv = new Unit("kelvin", "K", temp, 0, 1);

		Unit celc = new Unit("celsius", "degC", temp, 0, 1, -273.15);
		Unit farn = new Unit("farenheit", "degF", temp, 0, 5 / 9., -459.67);

		Dimension l = new Dimension("length", 0, 1, 0, 0);
		Dimension l2 = new Dimension("area", 0, 2, 0, 0);

		Unit m = new Unit("meter", "m", l);

		Unit f = new Unit("foot", "f", l, 0, 0.3048);

		Unit m2 = new Unit("meter2", "m2", l2, 0, 1);
		Unit cm2 = new Unit("centimeter2", "cm2", l2, -4, 1);

		LemsCollection<Unit> units = new LemsCollection<Unit>();
		units.add(kelv);
		units.add(celc);
		units.add(m);
		units.add(f);
		units.add(m2);
		units.add(cm2);

		double len = 123.456;
		DimensionalQuantity dq = QuantityReader.parseValue(len + " f", units);

		E.info("" + dq);

		assertEquals(len, dq.getValueInUnit(f), len / 100000.);

		double a = 10;
		DimensionalQuantity area = QuantityReader.parseValue(a + " m2", units);
		E.info("" + area);
		E.info(area.getValueInUnit(cm2) + " " + cm2.getSymbol());

		assertEquals(100 * 100 * a, area.getValueInUnit(cm2), a / 100000.0);

		double temps[] = new double[] { -40, 0, 37, 100, 232.7777777 };

		for (double t : temps) {
			dq = QuantityReader.parseValue(t + " degC", units);
			E.info(dq.getOriginalText() + " - " + dq + " " +
					(float) dq.getValueInUnit(kelv) + " " + kelv.getSymbol() + " " + 
					(float) dq.getValueInUnit(farn) + " " + farn.getSymbol());
			assertEquals(t, dq.getValueInUnit(celc), t * 1e-6);
		}
	}

	public static void main(String[] args) {
		DimensionTest ct = new DimensionTest();
		Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
		MainTest.checkResults(r);

	}

}