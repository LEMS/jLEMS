package org.lemsml.jlems.type;

import java.util.HashMap;

import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.expression.Evaluable;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;

public class Assertion {

	public String dimension;
	
	private Dimension r_about;
	
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
		 
	 
			 Evaluable ev = parser.parse(matches);
			 
			 Dimensional dm = ev.evaluateDimensional(adml);
			 
			 if (dm.matches(r_about)) {
				 E.info("OK (dimension check): " + dimension + " matches " + matches);
			 } else {
				 E.error("ERROR (dimension check): " + dimension + " does not match " + matches);
			 }
			 
		 

	 }
	 
	 
}
