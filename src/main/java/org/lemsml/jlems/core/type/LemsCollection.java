package org.lemsml.jlems.core.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.sim.ContentError;

public class LemsCollection<T> implements Iterable<T> {

 
	private transient ArrayList<T> contents = new ArrayList<T>();
	
	private transient HashMap<String, T> nameHM;
	private transient HashMap<String, T> pseudoNameHM;
	private transient HashMap<String, T> idHM;
	
	public Iterator<T> iterator() {
		return contents.iterator();
	}


	public boolean add(final T arg) {
		nameHM = null;
		pseudoNameHM = null;
		contents.add(arg);	
		
		clearMaps();
		
		return true;
	}
	
	
	private void clearMaps() {
		nameHM = null;
		pseudoNameHM = null;
		idHM = null;
	}
	
	
	
	public void addIfNew(final T arg) throws ContentError {
		if (arg instanceof Named) {
			String nm = ((Named)arg).getName();
			if (hasName(nm)) {
				// don't add 
			} else {
				add(arg);
			}
		} else {
			add(arg);
		}
	}


	public T getByName(final String name) throws ContentError {
		checkCreateNameHM();
		T ret = null;
		if (nameHM.containsKey(name)) {
			ret = nameHM.get(name);
		}
		return ret;
		
	}
	
	private void checkCreateNameHM() throws ContentError {
	if (nameHM == null) {
		nameHM = new HashMap<String, T>();
		for (T t : contents) {
			if (t instanceof Named) {
				final String tnm = ((Named)t).getName();
				if (nameHM.containsKey(tnm)) {
					throw new ContentError("Duplicate name for " + t + ": " + tnm + "\n" + "Contents: " + contents);
					
				} else {
					nameHM.put(tnm, t);
				}
			}
		}
	}
	}
	
	
	public boolean hasName(String pvn) throws ContentError   {
		checkCreateNameHM();
		boolean ret = false;
		if (nameHM.containsKey(pvn)) {
			ret = true;
		}
		return ret;
	}

	
	
	
	
	// TODO the following three methods duplicate the above with a different interface - could make it generic
	
	public T getByPseudoName(String name) throws ContentError {
		checkCreatePseudoNameHM();
		T ret = null;
		if (pseudoNameHM.containsKey(name)) {
			ret = pseudoNameHM.get(name);
		}
		return ret;
		
	}
	
	private void checkCreatePseudoNameHM() throws ContentError {
	if (pseudoNameHM == null) {
		pseudoNameHM = new HashMap<String, T>();
		for (T t : contents) {
			if (t instanceof PseudoNamed) {
				String pn = ((PseudoNamed)t).getPseudoName();
				if (pseudoNameHM.containsKey(pn)) {
					throw new ContentError("duplicate name for " + t);
				} else {					
					pseudoNameHM.put(pn, t);
				}
			}
		}
	}
	}
	
	
	public boolean hasPseudoName(String pvn) throws ContentError {
		checkCreatePseudoNameHM();
		boolean ret = false;
		if (pseudoNameHM.containsKey(pvn)) {
			ret = true;
		}
		return ret;
	}

	
	
	
	
	public T getByID(String sid) throws ContentError {
		checkCreateIDHM();
		T ret = null;
		if (idHM.containsKey(sid)) {
			ret = idHM.get(sid);
		}
		return ret;
		
	}
	
	private void checkCreateIDHM() throws ContentError {
	if (idHM == null) {
		idHM = new HashMap<String, T>();
		for (T t : contents) {
			if (t instanceof IDd) {
				String pn = ((IDd)t).getID();
				if (pn != null && idHM.containsKey(pn)) {
					throw new ContentError("Duplicate id: " + pn+"\nidHM: "+ idHM);
				} else {
					idHM.put(pn, t);
				}
			}
		}
	}
	}
	
	
	public boolean hasID(String sid) throws ContentError {
		checkCreateIDHM();
		boolean ret = false;
		if (idHM.containsKey(sid)) {
			ret = true;
		}
		return ret;
	}

	
	
	

	public int size() {
		return contents.size();
	}
    
	public boolean isEmpty() {
		return contents.isEmpty();
	}
	
    @Override
	public String toString() {
         
		StringBuilder sb = new StringBuilder();
		for (T t : contents) {
			sb.append(t.toString() + "\n");
		}
		return sb.toString();
	 
	//	return listAsText(" ");
    }


	public String listAsText() {
		return listAsText("");
	}
	
	
	public String listAsText(String sep) {
		StringBuilder sb = new StringBuilder();
		if (!contents.isEmpty()) {
			String scn = contents.get(0).getClass().getName();
			scn = scn.substring(scn.lastIndexOf(".") + 1, scn.length());
			sb.append(scn + "s:\n");
			for (T t : contents) {
				if (t instanceof Summaried) {
					sb.append("    " +  ((Summaried)t).summary() + "\n");
				} else {
					sb.append("    " +  t.toString() + "\n");
				}	
				sb.append(sep);
			}
		}
		return sb.toString();
	}


	public T first() {
		 return contents.get(0);
	}


	public T getOnly() throws ContentError {
		T ret = null;
		if (contents.size() == 1) {
			ret = first();
		} else {
			throw new ContentError("must have exactly one item in collection " + " (got " + contents.size() + ")");
		}
		return ret;
	}
	
	 

	public void deduplicate() throws ContentError {
		HashMap<String, T> xidHM = new HashMap<String, T>();
		ArrayList<T> ddContents = new ArrayList<T>();
		for (T t : contents) {
			String st = getXID(t);
			if (xidHM.containsKey(st)) {
				T pt = xidHM.get(st);
				if (t instanceof DataMatchable) {
					if (((DataMatchable)t).dataMatches(pt)) {
					// OK
					} else {
						String msg = "Two elements with the same identifier but different properties: " + t + "\nand " + pt;
						throw new ContentError(msg);
					}
				} else {
					throw new ContentError("non data matchable types in deduplicat: " + t);
				}
				
			} else {
				xidHM.put(st, t);
				ddContents.add(t);
			}
			
		}
		contents = ddContents;
	}
	
	
	public String getXID(T t) throws ContentError {
		String ret = "";
		if (t instanceof Named) {
			ret = ((Named)t).getName();
		} else if (t instanceof PseudoNamed) {
			ret = ((PseudoNamed)t).getPseudoName();
		} else if (t instanceof IDd) {
			ret = ((IDd)t).getID();
		} else {
			throw new ContentError("no identification for " + t);
		}
		return ret;
	}


	public void addAll(LemsCollection<T> ts) {
		for (T t : ts) {
			add(t);
		}
	}


	public ArrayList<T> getContents() {
		return contents;
	}


	public void clear() {
		contents.clear();
		nameHM = null;
		pseudoNameHM = null;
		idHM = null;
	}
	
 


	public HashMap<String, T> getMap() throws ContentError {
		HashMap<String, T> ret= new HashMap<String, T>();
		for (T t : contents) {
			ret.put(getXID(t), t);
		}
		return ret;
	}


	public T get(int i) {
		return contents.get(i);
	}

}
