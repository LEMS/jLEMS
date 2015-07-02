package org.lemsml.jlems.core.type.distribution;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.DimensionalQuantity;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.type.QuantityReader;
import org.lemsml.jlems.core.type.Unit;

public class GammaDistribution extends Distribution {

	public String mean;
	
	public String shape;
	
	private double p_mean;
	private double p_shape;
	 
	
	@Override
	public void resolve(LemsCollection<Unit> units) throws ParseError, ContentError {
		DimensionalQuantity dq = QuantityReader.parseValue(mean, units);
		r_dimension = dq.getDimension();
		p_mean = dq.getDoubleValue();
		p_shape = Double.parseDouble(shape);
	}
}
