package org.lemsml.jlems.run;

import java.util.ArrayList;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;

public class PathDerivedVariable {

    String varname;
    String path;
    
   
    String func;
    String tgtvar;
    boolean simple = false;
  
    double fbase = 0;
    final static int SUM = 1;
    final static int PROD = 2;
    int mode;
    
    boolean required;
    

    public PathDerivedVariable(String snm, String p, String f, boolean rd, String reduce) {
        varname = snm;
        path = p;
        func = f;
        required = rd;
      
        if (reduce != null) {
        	if (reduce.equals("add")) {
        		mode = SUM;
        	} else if (reduce.equals("multiply")) {
        		mode = PROD;
        	} else {
        		E.warning("Unrecognized reduce value: " + reduce);
        	}
        }
         
        String[] bits = path.split("/");
        tgtvar = bits[bits.length - 1];
        

        if (path.indexOf("[") > 0 || path.indexOf("*") > 0) {
            simple = false; 
            parseFunc(p);
         
        } else {
            simple = true;
        }
    }

    
    private void parseFunc(String p) {
    	 if (func == null) {
             E.error("no reduce function specified with multi-selector " + p);
         }
         if (func.equals("sum") || func.equals("add") || func.equals("plus") || func.equals("+")) {
             mode = SUM;
             fbase = 0;

         } else if (func.equals("product") || func.equals("times") || func.equals("multiply") || func.equals("*")) {
             mode = PROD;
             fbase = 1;

         } else {
             E.error("unrecognized function " + path);
         }
    }
    
    
    @Override
    public String toString() {
        return "PathDerivedVariable{" + "varname=" + varname + ", path=" + path + ",  func=" + func + ", tgtvar=" + tgtvar + '}';
    }
    
    

    public String getVarName() {
        return varname;
    }

    public double eval(StateInstance sin) throws RuntimeError, ContentError {
        double ret = Double.NaN;
        StateInstance tgt = null;
        try {
            if (simple) {
                tgt = sin.getPathStateInstance(path);
                if (tgt == null) {
                	if (mode == SUM) {
                		ret = 0;
                		// its fine to have a sum of no matches - just return zero.
                		// anything else is an error
                	} else {
                		if (!required &&  mode == PROD) {
                			ret = 1;
                	
                		} else {
                			throw new ContentError("Not a sum and no variable at path " + path + " in " + sin + " seeking " + tgtvar + " mode=" + mode);
                		}
                	}
                } else {
                	ret = tgt.getVariable(tgtvar);
                }
            } else {
                // E.info("seeking psa " + path);
            	// this calls getTargetArray below the first time, then caches
            	// the array of targets for future use
                ArrayList<StateInstance> asa = sin.getPathStateArray(path);

                ret = fbase;
                for (StateInstance sa : asa) {
                    double var = sa.getVariable(tgtvar);
                    if (mode == SUM) {
                        ret += var;
                    } else if (mode == PROD) {
                        ret *= var;
                    }
                }
            }
        } catch (RuntimeError rte) {
            throw new RuntimeError("Error at PathDerivedVariable eval(): tgt: " + tgt + "; sin: " + sin + "; tgtvar: " + tgtvar + "; path: " + path, rte);
        }
        return ret;
    }

    /*
    bits = rest.split("/");
    int nbit = bits.length;
    slice = new boolean[nbit];
    for (int i = 0; i < nbit - 1; i++) {
        String b = bits[i];
        if (b.endsWith("[*]")) {
            bits[i] = b.substring(0, b.length() - 3);
            slice[i] = true;
        
        } else if (b.indexOf("[") > 0) {
        	E.missing("Can't process predicate: " + b);
        	
        } else {
            slice[i] = false;
        }
    }
*/

    public StateInstance getTargetState(StateInstance uin) throws ContentError {
        StateInstance ret = null;

        StateInstance wkinst = uin;
        String[] bits = path.split("/");
        for (int i = 0; i < bits.length - 1; i++) {
            try {
                wkinst = wkinst.getChildInstance(bits[i]);
            } catch (ContentError e) {
                throw new ContentError("Problem getting Child instance",e);
            }
        }
        ret = wkinst;

        return ret;
    }

    public ArrayList<StateInstance> getTargetArray(StateInstance base) throws ContentError {
        ArrayList<StateInstance> ret = new ArrayList<StateInstance>();

        ArrayList<StateInstance> wka = new ArrayList<StateInstance>();
        wka.add(base);
        
        String[] bits = path.split("/");
        
        for (int i = 0; i < bits.length - 1; i++) {
            String bit = bits[i];
            ArrayList<StateInstance> swka = new ArrayList<StateInstance>();
            
            for (StateInstance par : wka) {
            	if (bit.indexOf("[") > 0) { 
            		int iob = bit.indexOf("[");
            		int icb = bit.indexOf("]");
            		if (icb < iob) {
            			throw new ContentError("Can't parse " + path);
            		}
            		String cnm = bit.substring(0, iob);
            		
            		MultiInstance mi = par.getMultiInstance(cnm);
            		
            		if (mi == null) {
                        // No need for this warning...
            			//E.warning("No match to path element '" + cnm + "' in " + path+" on "+ base+", parent: "+base.parent);
            		} else {
                    	 String pred = bit.substring(iob + 1, icb);
                    	 if (pred.equals("*")) {
                    		 swka.addAll(mi.getStateInstances());                    		 
                    	 } else {
                    		 String[] sab = parsePredicate(pred);
                    		 String satt = sab[0];
                    	     String sval = sab[1];
                    	     
                    	     for (StateInstance asi : mi.getStateInstances()) {
                                 if (asi.hasTypeParam(satt)) {
                                     String stv = asi.getTypeParam(satt);
                                     if (stv != null && stv.equals(sval)) {
                                         //E.log("Adding match (" + satt + "  = " + sval + ") on " + asi+ " for <<" + path+">>");
                                         swka.add(asi);
                                     } else {
                                         //E.log("Not adding match (" + satt + "  = " + sval + ") on " + asi+ " for <<" + path+">>, stv = "+stv);
                                     }
                                 } else {
                                     //E.log("Not adding match (" + satt + "  = " + sval + ") on " + asi+ " for <<" + path+">>");
                                 }
                    	    	 
                    	     }
                    	 }
                     }
            	} else {
            		swka.add(par.getChildInstance(bit));
            	}
            	wka = swka;            	
            }
        }
        ret = wka;    
       
        //E.log("---- Selecting path got " + ret.size() + " for <<" + path+">>");
        
        return ret;
    }

    
    
    public String getPath() {
        return path;
    }

    public boolean isSimple() {
        return simple;
    }

  
    
    private String[] parsePredicate(String pred) throws ContentError {
		String[] ret = new String[2];
		// for now just handle propert='val' type predicates
		String[] bits = pred.split("=");
		if (bits.length == 2) {
			String prop = bits[0];
			String sval = deQuote(bits[1]);
		 	ret[0] = prop;
		 	ret[1] = sval;
			
		} else {
			throw new ContentError("cant parse redicate " + pred);
		}
		return ret;
	}

	
	private String deQuote(String sq) throws ContentError {
		String ret = sq;
		if (sq.startsWith("'") && sq.endsWith("'")) {
			ret = sq.substring(1, sq.length() - 1);
			
		} else if (sq.startsWith("\"") && sq.endsWith("\"")) {
			ret = sq.substring(1, sq.length() - 1);
		
		} else {
			throw new ContentError("String not quoted properly? " + sq);
		}
		return ret;
	}


	public String[] getBits() {
		return path.split("/");
	}


	public PathDerivedVariable makeFlat(String pfx) {
		String modeString = null;
		// TODO cleaner constructor
		if (mode == SUM) {
			modeString = "add";
		} else if (mode == PROD) {
			modeString = "multiply";
		}
		PathDerivedVariable ret = new PathDerivedVariable(pfx + varname, flattenPath(pfx), 
				func, required, modeString); 
		return ret;
	}
	
    private String flattenPath(String pfx) {
    	String ret = null;
    	if (simple) {
    		ret = pfx + path.replaceAll("/", "_");
    	} else {
    		
    		String wc = "[*]/";
    		if (path.indexOf(wc) > 0) {
    			int iwc = path.indexOf(wc);
    			String ba = path.substring(0, iwc);
    			String bb = path.substring(iwc + wc.length(), path.length());
    			ret = pfx + ba + "*_" + bb;
    			E.info("Partially flattened " + path + " to " + ret);
    		} else {
    			E.missing("Cant flattten " + path);
    		}
    	}
    	return ret;
    }


	public String getOperatorSymbol() {
		String ret = "";
		if (mode == SUM) {
			ret = "+";
		} else if (mode == PROD) {
			ret = "*";
		}
		return ret;
	}


	public boolean isSum() {
		boolean ret = false;
		if (mode == SUM) {
			ret = true;
		}
		return ret;
	}
	
	public boolean isProduct() {
		boolean ret = false;
		if (mode == PROD) {
			ret = true;
		}
		return ret;
	}


	public boolean isRequired() {
		return required;
	}
	
}
