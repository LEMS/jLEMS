package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.DBase;
import org.lemsml.jlems.core.eval.DCon;
import org.lemsml.jlems.core.eval.DVar;
import org.lemsml.jlems.core.eval.Plus;
import org.lemsml.jlems.core.eval.Times;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public class Flattener {

	ArrayList<FixedQuantity> fixedA = new ArrayList<FixedQuantity>();

    ArrayList<String> indepsA = new ArrayList<String>();
	
    ArrayList<PathDerivedVariable> pdvA = new ArrayList<PathDerivedVariable>();
	
    ArrayList<ExpressionDerivedVariable> edvA = new ArrayList<ExpressionDerivedVariable>();
	
    ArrayList<VariableROC> rocA = new ArrayList<VariableROC>();
	
    ArrayList<String> svA = new ArrayList<String>();
	
	
    HashMap<String, String> dimensions = new HashMap<String, String>();
	
	ArrayList<VariableAssignment> initializationAssignments = new ArrayList<VariableAssignment>();
	
	
	public void addIndependentVariable(String s, String d) throws ContentError {
		if (indepsA.contains(s)) {
			// fine - lots of children could 
		} else {
			indepsA.add(s);
			dimensions.put(s, d);
			if (d == null) {
				throw new ContentError("Null dimension for independent variable " + s);
			}
		}
	}
	
	
	public void add(PathDerivedVariable pdv) throws ContentError {
 		pdvA.add(pdv);
 		String d = pdv.getDimensionString();
 		dimensions.put(pdv.getVariableName(), d);
 		if (d == null) {
			throw new ContentError("Null dimension for path derived variable " + pdv);
		}
 //		E.info("added pdv " + pdv);
	}

	public void add(ExpressionDerivedVariable edv) throws ContentError {
		edvA.add(edv);
		String d = edv.getDimensionString();
		dimensions.put(edv.getVariableName(), d);
		if (d == null) {
			throw new ContentError("Null dimension for expression derived variable " + edv);
		}
		//		E.info("added edv " + edv);
	}

	public void add(VariableROC vroc) throws ContentError {
		rocA.add(vroc);	
		String d = vroc.getDimensionString();
		dimensions.put(vroc.getVariableName(), d);
		if (d == null) {
			throw new ContentError("Null dimension for rate variable " + vroc);
		}
	}

	public void addStateVariable(String sv, String dim) throws ContentError {
		svA.add(sv);
		if (dim == null) {
			throw new ContentError("Null dimension for stae variable " + sv);
		}
		dimensions.put(sv, dim);
//		E.info("Added sv " + sv);
	}

	public void resolvePaths() {
		resolvePathDerived();
		substitutePathDerived();
		removeLocalIndeps();

		sortExpressions();
	}
	
	
	private void sortExpressions() {
		ArrayList<ExpressionDerivedVariable> totalAdded = new ArrayList<ExpressionDerivedVariable>();
		HashSet<String> known = new HashSet<String>();
		known.addAll(indepsA);
		known.addAll(svA);
		for (VariableROC vr : rocA) {
			known.add(vr.getVariableName());
		}
			
		for (FixedQuantity fq : fixedA) {
			known.add(fq.getName());
		}
		
		ArrayList<ExpressionDerivedVariable> wksrc = new ArrayList<ExpressionDerivedVariable>();
		wksrc.addAll(edvA);
		
		int nadded = 1;
		while (nadded > 0) {
			nadded = 0;
			
			ArrayList<ExpressionDerivedVariable> justAdded = new ArrayList<ExpressionDerivedVariable>();
			for (ExpressionDerivedVariable edv : wksrc) {
				if (edv.onlyDependsOn(known)) {
					justAdded.add(edv);
					totalAdded.add(edv);
					known.add(edv.getVariableName());
					nadded += 1;
				}
			}
			// E.info("sort cycle nadded=" + nadded);
			wksrc.removeAll(justAdded);
		}
		
		if (totalAdded.size() == edvA.size()) {
			// OK - added them all;
		} else {
			E.error("Not added all expressions while sorting? total=" + edvA.size() + " added=" + totalAdded.size());
			E.info("Known are " + known);
			for (ExpressionDerivedVariable edv : wksrc) {
				E.info("   not added " + edv.getExpressionString());
			}
		}
		
		edvA = totalAdded;
	}
	
	
	
	private void removeLocalIndeps() {
		// one of the children may have added an indep which is actually our own state variable (not a parent)
		// need to remove these here
		// TODO - better - don't add in the first place
		ArrayList<String> wk = new ArrayList<String>();
		for (String s : indepsA) {
			if (svA.contains(s)) {
				E.info("Flattener removing indep " + s + " as it is a local state variable");
			} else {
				wk.add(s);
			}
		}
		indepsA = wk;
	}
	
	
	private void resolvePathDerived() {
		ArrayList<PathDerivedVariable> pa = new ArrayList<PathDerivedVariable>();
		
		for (PathDerivedVariable pdv : pdvA) {
			String path = pdv.getPath();
			if (path.indexOf("*") > 0) {
				
				String[] elts = expandWildcard(path);
				E.info("Resolving wildcard path " + path + " n matches=" + elts.length);
				
				if (elts.length == 0) {
					DCon dcon = new DCon(0.);
					DBase db = new DBase(dcon);
					edvA.add(new ExpressionDerivedVariable(pdv.getVariableName(), db, pdv.getDimensionString()));
					
				} else {
					AbstractDVal wk = new DVar(elts[0]);
				  
					 for (int i = 1; i < elts.length; i++) {
						 DVar dv = new DVar(elts[i]);
						 
						 if (pdv.isSum()) {
							 wk = new Plus(wk, dv);
						 
						 } else if (pdv.isProduct()) {
							 wk = new Times(wk, dv);
						 } else {
							 E.error("Unhandled operator " + pdv);
						 }
					}
					DBase db = new DBase(wk);
					edvA.add(new ExpressionDerivedVariable(pdv.getVariableName(), db, pdv.getDimensionString()));
				}
				
			 
				
				
			} else {
				pa.add(pdv);
			}
		} 
		pdvA = pa;
	}
	
	
	private void substitutePathDerived() {
		for (PathDerivedVariable pdv : pdvA) {
			String vnm = pdv.getVariableName();
			String pth = pdv.getPath();
			
			// E.info("ZZZ sub time " + pth + " " + vnm);
			
			if (vnm.equals(pth)) {
				// degenerate - just leave out
			} else {
				for (ExpressionDerivedVariable edv : edvA) {
					edv.substituteVariableWith(vnm, pth);
				}
				
				for (VariableROC vroc : rocA) {
					vroc.substituteVariableWith(vnm, pth);
				}
			}
		}
		
		//now discard the existing pdvs
		pdvA = new ArrayList<PathDerivedVariable>();
	}
	
	
	
	private String[] expandWildcard(String path) {
		ArrayList<String> wk = new ArrayList<String>();
		
		int ia = path.indexOf("*");
		String sa = path.substring(0, ia);
		String sb = path.substring(ia+1, path.length());
		
		ArrayList<String> avn = getVarNames();
		
		for (String vnm : avn) {
			if (vnm.startsWith(sa) && vnm.endsWith(sb)) {
				String inner = vnm.substring(sa.length(), vnm.length() - sb.length());
//				E.info("match " + sa + " * " + sb + " from " + vnm + " leaves " + inner);
					
				if (isNumeric(inner)) {
					wk.add(vnm);
				}
			}
			
		}
		
		return wk.toArray(new String[wk.size()]);
	}
	
	
	private ArrayList<String> getVarNames() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.addAll(svA);
		ret.addAll(indepsA);
		for (PathDerivedVariable pdv : pdvA) {
 			ret.add(pdv.getVariableName());
 		}
 		
		for (ExpressionDerivedVariable edv : edvA) {
 			ret.add(edv.getVariableName());
 		}
 		
		for (VariableROC vr : rocA) {
 			ret.add(vr.getVariableName());
 		}
		return ret;
	}
	
	
	
	private boolean isNumeric(String s) {
		boolean ret = true;
	 
		for (char c : s.toCharArray()) {
			if (!Character.isDigit(c)) {
				ret = false;
				break;
		    }
		}
		return ret;
	}
	

	public void addInitializationAssignment(VariableAssignment va) {
		initializationAssignments.add(va);
	}

	
	
	
	

	public void exportTo(StateType ret) throws ContentError {

		for (FixedQuantity fq : fixedA) {
			ret.addFixed(fq);
		}
		
		for (String s : indepsA) {
			ret.addIndependentVariable(s, dimensions.get(s));
		}
		
		for (String sv : svA) {
 			ret.addStateVariable(sv, dimensions.get(sv));
 		}
 		
		for (PathDerivedVariable pdv : pdvA) {
 			ret.addPathDerivedVariable(pdv);
 		}
 		
		for (ExpressionDerivedVariable edv : edvA) {
 			ret.addExpressionDerivedVariable(edv);
 		}
 		
		for (VariableROC vr : rocA) {
 			ret.addVariableROC(vr);
 		}
		
		if (initializationAssignments.size() > 0) {
			ActionBlock ab = new ActionBlock();
			for (VariableAssignment va : initializationAssignments) {
				ab.addVariableAssignment(va);
			}
			ret.addInitialization(ab);
		}
		
	}


	public void addFixed(FixedQuantity fqf) {
		 fixedA.add(fqf);
	}


	
	
	
}
