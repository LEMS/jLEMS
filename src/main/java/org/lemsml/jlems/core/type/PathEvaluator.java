package org.lemsml.jlems.core.type;

import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;


// TODO this is just a quick hack as a POC for a few special cases. It needs a proper parser as for expressions.


public class PathEvaluator {

	Lems base;
	Component root;
	String path;
	
	
	public PathEvaluator() {
		this(null, null, null);
	}
	
	public PathEvaluator(Lems lems, Component cpt, String p) {
		base = lems;
		root = cpt;
		path = p;
	}


	public static double getValue(Lems lems, Component cpt, String path) throws ContentError {
		PathEvaluator pv = new PathEvaluator(lems, cpt, path);
		return pv.eval();
	}

	 

	private double eval() throws ContentError {
 		int ils = path.lastIndexOf("/");
		String cpath = path.substring(0, ils);
		String pname = path.substring(ils+1, path.length());
		
		Component c = getComponent(cpath);
		ParamValue v = c.getParamValue(pname); 
		if (v == null) {
			throw new ContentError("no such parameter value " + pname + " in " + c);
		}
		double ret = v.getDoubleValue();
 		return ret;
	}
	
	
	
	
	public String getStringValue() throws ContentError {

        //E.info("Evaluating path " + path + " relative to " + root);
 		int ils = path.lastIndexOf("/");
 		String ret = null;
 		if (ils < 0) {
 			ret = root.getStringValue(path);
 		} else {
 			String cpath = path.substring(0, ils);
 			String pname = path.substring(ils+1, path.length());
		
 			Component c = getComponent(cpath);
            if (c == null) {
                throw new ContentError("Problem evaluating path " + path + " relative to " + root);
            }
 			ret = c.getStringValue(pname); 
	
 		}
 		if (ret == null) {
			throw new ContentError("No such String parameter " + path + " relative to " + root);
		}
		return ret;
	}
	
	
	
	
	private Component getComponent(String cpatha) throws ContentError {
		String cpath = cpatha;
		if (cpath.startsWith("//")) {
			cpath = "ALLTYPE:" + cpath.substring(2, cpath.length());
		}
		return getComponent(root, cpath);
	}
	
	
	
	public Component getComponent(Component wk0, String cpath) throws ContentError {
		ArrayList<String> bits = new ArrayList<String>();
		
		int bktlev = 0;
		String bit = "";
//		StringTokenizer st = new StringTokenizer(cpath, "/");
	//	while (st.hasMoreTokens()) {
	//		String tok = st.nextToken();
	
		for (String tok : cpath.split("/")) {
			bit += tok;
			bktlev += count(tok, "[");
			bktlev -= count(tok, "]");
			if (bktlev == 0) {
				bits.add(bit);
				bit = "";
			} else {
				bit += "/";
			}
		}
		
		int nbit = bits.size();
		Component wk = wk0;
		for (int i = 0; i < nbit; i++) {
			String rp = bits.get(i);
		 
			if (rp.indexOf("[") < 0) {
				wk = getRelativeComponent(wk, rp);
			} else {
				wk = getPredicateComponent(wk, rp);
			}
 		}
 		return wk;
	}


	


	private Component getRelativeComponent(Component wk, String rp) throws ContentError {
		Component ret = null; 
		if (rp.equals(".")) {
			ret = wk;
		} else if (rp.equals("..")) {
			ret = wk.getParent();
		} else {
			ret = wk.getChild(rp);
		}
		
		return ret;
	}
	
	private Component getPredicateComponent(Component wk, String rp) throws ContentError {
 		 int iob = rp.indexOf("[");
		 String cnm = rp.substring(0, iob);
		 ArrayList<Component> ac = null;
		 if (cnm.startsWith("ALLTYPE:")) {
			 ac = base.getAllByType(cnm.substring("ALLTYPE:".length(), cnm.length()));
			 
		 } else {
			ac = wk.getChildrenAL(cnm);
		 }
		 if (ac == null) {
			 throw new ContentError("no such children list " + cnm + " in " + wk);
		 }
		 
		 
		 String pred = rp.substring(iob+1, rp.indexOf("]"));
		 
		 Component ret = null;
		 if (pred.indexOf("=") > 0) {
			 int ieq = pred.indexOf("=");
			 String attname = pred.substring(0, ieq).trim();
			 String attval = pred.substring(ieq+1, pred.length()).trim();
			 
			 Component avc = getComponent(attval);
			 
			 for (Component cwk : ac) {
				 Component ctry = getComponent(cwk, attname);
				 if (ctry.equals(avc)) {
					 ret = cwk;
				 }
			 }
			 
		 } else {
			 E.missing("cant handle predicate form yet: " + pred);
		 }
		 
		 return ret;
	}

	
	private int count(String str, String c) {
		int ret = 0;
		int p = -1;
		while (true) {
			p = str.indexOf(c, p+1);
			if (p >= 0) {
				ret += 1;
			} else {
				break;
			}
		}
		return ret;
	}
	
	
}
