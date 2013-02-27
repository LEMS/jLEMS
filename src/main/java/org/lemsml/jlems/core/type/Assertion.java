package org.lemsml.jlems.core.type;

import java.util.HashMap;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="Assertions are not strictly part of the model, but can be included in a file " +
		"as a consistency check.")
public class Assertion {

	@ModelProperty(info="The name of a dimension")
	public String dimension;
	
	private Dimension r_about;
	
	@ModelProperty(info="An expression involving dimensions. The dimensionality of the expression " +
			"should match the dimensionality of the dimension reference.")
	public String matches;
	
	
	 public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
	       
	        Dimension d = dimensions.getByName(dimension);
	        if (d != null) {
	            r_about = d;
	            //	E.info("resolved unit " + name + " " + symbol);
	        } else {
	            throw new ContentError("no such dimension: " + dimension);
	        }

	    }

	
	 public void check(LemsCollection<Dimension> dimensions, Parser parser) throws ContentError, ParseError {
		 
		 HashMap<String, Dimension> adm = dimensions.getMap();

		 HashMap<String, Dimensional> adml = new HashMap<String, Dimensional>();
		 for (String s : adm.keySet()) {
			 adml.put(s, adm.get(s));
		 }
		 
	 
			 ParseTree pt = parser.parse(matches);
			 
			 Dimensional dm = pt.evaluateDimensional(adml);
			 
			 if (dm.matches(r_about)) {
			//	 E.info("OK (dimension check): " + dimension + " matches " + matches);
			 } else {
				 E.error("ERROR (dimension check): " + dimension + " does not match " + matches);
			 }
			 
		 

	 }
	 
	 
}
