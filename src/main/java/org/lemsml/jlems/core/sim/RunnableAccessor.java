package org.lemsml.jlems.core.sim;

import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.MultiInstance;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.run.StateWrapper;

public class RunnableAccessor {

	StateRunnable root;
	
	
	public RunnableAccessor(StateRunnable sr) {
		root = sr;
	}


	public StateWrapper getStateWrapper(String path) throws ConnectionError {
		StateWrapper ret = null;
		
		// E.info("seeking sw for " + path);
		
		String spath = path;
		spath = spath.replace("[", "/[");
		
		String[] bits = spath.split("/");
 		
		StateRunnable wk = root;
		for (int i = 0; i < bits.length-1; i++) {
			// E.srcinfo("seeking child " + bits[i] + " in " + wk);
 		
			if (bits[i].trim().length() == 0) {
				// skip it
			} else {
				wk = wk.getChild(bits[i]);
				if (wk == null) {
					break;
				}
			}
		}
		
		if (wk != null) {
			ret = wk.getWrapper(bits[bits.length-1]);
		}
		if (ret == null) {
			E.info("starting from " + root);
			StateRunnable pwk = root;
			for (int i = 0; i < bits.length-1; i++) {
				// E.srcinfo("seeking child " + bits[i] + " in " + wk);
				
				pwk = pwk.getChild(bits[i]);
				if (pwk == null) {
					E.info("failed to get child " + bits[i]);
					break;
				} else {
					E.info("got child " + pwk);
				}
			}
			if (pwk != null) {
				E.info("now need wrapper for " + bits[bits.length-1] + " within " + pwk);
			}
            
			throw new ConnectionError("Can't parse " + spath + " (orig: "+path+").\nLast component: " + wk+" ("+wk.getVariables()+"), root: "+root);
		}
		
		return ret;
	}


	public StateRunnable getStateInstance(String path) throws ConnectionError {
		return getRelativeStateInstance(root, path);
	}
	
	
	
	public StateRunnable getRelativeStateInstance(StateRunnable base, String path) throws ConnectionError {
		StateRunnable ret = null;
		
		String spath = path;
		spath = spath.replace("[", "/[");
		
		String[] bits = spath.split("/");

        //E.info("getRelativeStateInstance on "+base+", path: "+path);
		
		StateRunnable wk = base;
		for (int i = 0; i < bits.length-1; i++) {

            //E.info("Bit: "+bits[i]);
            if (bits[i].equals("..")) {
                wk = wk.getParent();
            } else {
                wk = wk.getChild(bits[i]);
            }
            //E.info("wk: "+wk);
			if (wk == null) {
				break;
			}
		}
		
		String lastbit = bits[bits.length-1];
		if (lastbit.indexOf("[") < 0) {
			ret = wk.getChild(lastbit);
			
		} else {
		 
//			if (wk instanceof MultiInstance) {
//				ret = ((MultiInstance)wk).getPredicateInstance(lastbit);
		// can't happen any longer, but could be useful?
			
		  if (wk.hasSingleMI()) {
				ret = wk.getSingleMI().getPredicateInstance(lastbit);
				
			} else {
				String msg = "Can't get predicate " + lastbit + " from " + wk + ", original path: " + path  + "\n";
				msg += "component has singeMI=" + wk.hasSingleMI() + "";
				throw new ConnectionError(msg);
			}
		}
		
	 
		if (ret == null) {
			throw new ConnectionError("Can't parse " + spath + " Last component: " + wk);
		}
		return ret;
	}
	
	
	
	public ArrayList<StateRunnable> getStateInstances(String path) throws ConnectionError {
		ArrayList<StateRunnable> ret = null;
		
		String[] bits = path.split("/");
		
		StateRunnable wk = root;
		for (int i = 0; i < bits.length-1; i++) {
			wk = wk.getChild(bits[i]);
			if (wk == null) {
				break;
			}
		}
		if (wk == null) {
			throw new ConnectionError("cant find instances at " + path);
		}  
		if (wk instanceof StateInstance) {
			ret = new ArrayList<StateRunnable>();
			ret.add((StateInstance)wk);
		} else if (wk instanceof MultiInstance) {
			ret = ((MultiInstance)wk).getStateInstances();
		} else {
			throw new ConnectionError("need instances, but resolved path to " + wk);
		}
			
		return ret;
	}

}
