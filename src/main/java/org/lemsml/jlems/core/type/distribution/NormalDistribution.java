package org.lemsml.jlems.core.type.distribution;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.DimensionalQuantity;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.type.QuantityReader;
import org.lemsml.jlems.core.type.Unit;

public class NormalDistribution extends Distribution {
	
	public String mean;
	
	public String sd;
 
	private double p_mean;
	private double p_sd;
	

	@Override
	public void resolve(LemsCollection<Unit> units) throws ParseError, ContentError {
		DimensionalQuantity dq = QuantityReader.parseValue(mean, units);
		r_dimension = dq.getDimension();
		p_mean = dq.getDoubleValue();
		 
		DimensionalQuantity dqsd = QuantityReader.parseValue(sd, units);
		if (dqsd.dimensionsMatch(r_dimension)) {
			p_sd = dqsd.getDoubleValue();
		} else {
			throw new ContentError("Mismatched dimensions on normal distribution mean=" + mean + ", sd=" + sd);
		}
	}


	 




}
