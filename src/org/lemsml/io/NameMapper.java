package org.lemsml.io;

import java.util.HashMap;
import java.util.HashSet;

public  class NameMapper implements ImportNameMapper, ExportNameMapper {
	
	// TODO refactor - way too much generic code here
	
	HashMap<String, String> globalAttributeNameMap = new HashMap<String, String>();
	HashMap<String, String> outGlobalAttributeNameMap = new HashMap<String, String>();

	HashMap<String, String> inputElementNameMap = new HashMap<String, String>();

	 
	HashMap<Class<?>, String> outElementNameMap = new HashMap<Class<?>, String>();
	HashMap<Class<?>, HashMap<String, String>> outAttributeNameMap = new HashMap<Class<?>, HashMap<String, String>>();
	
	HashMap<Class<?>, HashMap<String, HashMap<String, String>>> eamap = new HashMap<Class<?>, HashMap<String, HashMap<String, String>>>();
	
	HashMap<Class<?>, HashMap<String, String>> autoAttributes = new HashMap<Class<?>, HashMap<String, String>>();
	
	HashMap<Class<?>, HashSet<String>> suppressAttributeHS = new HashMap<Class<?>, HashSet<String>>();
	
	HashMap<Class<?>, HashMap<String, String>> attEltMap = new HashMap<Class<?>, HashMap<String, String>>();
	
	HashMap<Class<?>, String> pushDowns = new HashMap<Class<?>, String>();
	
	boolean allNative = true;
 	
	// for use where other names are allowed in 
	// native LEMS xml. For more complex mappings, use 
	// the IOFace interface and a "face" layer 
	// as in the comodl package
	
	public NameMapper() {
		
	 	}
	
	
	
	
	
	public void addInputElementRename(String extname, String intname) {
		inputElementNameMap.put(extname, intname);
	 
	}
	
	
	public void addExportElementRename(Class<?> cls, String extname) {
		outElementNameMap.put(cls, extname);
	}
	
	public void addExportAutoAttribute(Class<?> cls, String attname, String attval) {
		if (!autoAttributes.containsKey(cls)) {
			autoAttributes.put(cls, new HashMap<String, String>());
			autoAttributes.get(cls).put(attname, attval);
		}
	}
	
	public void addExportSuppressAttribute(Class<?> cls, String tohide) {
		if (!suppressAttributeHS.containsKey(cls)) {
			suppressAttributeHS.put(cls, new HashSet<String>());
		}
		suppressAttributeHS.get(cls).add(tohide);
	}
	
	public void addGlobalAttributeRename(String extname, String intname) {
		globalAttributeNameMap.put(extname, intname);
		
		if (!outGlobalAttributeNameMap.containsKey(intname)) {
			outGlobalAttributeNameMap.put(intname, extname);
		}
	}
	
	public void addExportPushDown(Class<?> cls, String s) {
		pushDowns.put(cls, s);
	}
	
	public boolean pushesDown(Class<?> cls) {
		return pushDowns.containsKey(cls);
	}
	
	public String getPushDown(Class<?> cls) {
		return pushDowns.get(cls);
	}
	
	
	public void addAttributeToElementMap(Class<?> cls, String att, String elt) {
		if (!attEltMap.containsKey(cls)) {
			attEltMap.put(cls, new HashMap<String, String>());
		}
		attEltMap.get(cls).put(att, elt);
	}
	 	 
	
	public boolean elementizesAttribute(Class<?> cls, String att) {
		boolean ret = false;
		if (attEltMap.containsKey(cls) && attEltMap.get(cls).containsKey(att)) {
			ret = true;
		}
		return ret;
	}
	
	public String getAttributeToElementName(Class<?> cls, String att) {
		return attEltMap.get(cls).get(att);
	}
	
	
	
	public void addExportAttributeRename(Class<?> cls, String intname, String extname) {
		if (outAttributeNameMap.containsKey(cls)) {
			outAttributeNameMap.get(cls).put(intname, extname);
		} else {
			HashMap<String, String> hm = new HashMap<String, String>();
			outAttributeNameMap.put(cls, hm);
			hm.put(intname, extname);
		}
	}
	
	
	public void addExportAttributeValueMapping(Class<?> cls, String anm, String sint, String sext) {
		if (!eamap.containsKey(cls)) {
			eamap.put(cls, new HashMap<String, HashMap<String, String>>());
		}
		HashMap<String, HashMap<String, String>> hss = eamap.get(cls);
		if (!hss.containsKey(anm)) {
			hss.put(anm, new HashMap<String, String>());
		}
		hss.get(anm).put(sint, sext);
	}
	
	
	public String getMappedAttributeValue(Class<?> cls, String anm, String sint) {
		String ret = null;
		if (eamap.containsKey(cls)) {
			HashMap<String, HashMap<String, String>> hss = eamap.get(cls);
			if (hss.containsKey(anm)) {
				HashMap<String, String> hm = hss.get(anm);
				if (hm.containsKey(sint)) {
					ret = hm.get(sint);
				}
			}
		}
		return ret;
	}
	
	
	public boolean renames(String enm) {
		boolean ret = false;
		if (inputElementNameMap.containsKey(enm)) {
			ret = true;
		}
		return ret;
	}
	
	public String getInternalElementName(String enm) {
		String ret = enm;
		if (inputElementNameMap.containsKey(enm)) {
			allNative = false;
			ret = inputElementNameMap.get(enm);
		}
		return ret;
	}
	
	public String getInternalAttributeName(Class<?> cls, String attname) {
		String ret = attname;
		if (globalAttributeNameMap.containsKey(attname)) {
			ret = globalAttributeNameMap.get(attname);
		}
		return ret;
	}
	
	
	public String getExportElementName(Class<?> cls) {
		String ret = null; 
		if (outElementNameMap.containsKey(cls)) {
 			ret = outElementNameMap.get(cls);
		}
		return ret;
	}
	
	
	public String getExportAttributeName(Class<?> cls, String attnm) {
		String ret= attnm;
		
		boolean set = false;
		if (suppressAttributeHS.containsKey(cls) && suppressAttributeHS.get(cls).contains(attnm)) {
			ret = null;
			set = true;
		
		} else if (outAttributeNameMap.containsKey(cls)) {
			HashMap<String, String> m = outAttributeNameMap.get(cls);
			if (m.containsKey(attnm)) {
				ret = m.get(attnm);
				set= true;
			}
		}
		if (!set && outGlobalAttributeNameMap.containsKey(attnm)) {
			ret = outGlobalAttributeNameMap.get(attnm);
		}
		return ret;
	}
	
	
	
	
	
	public boolean appliedAny() {
		boolean ret = false;
		if (allNative) {
			ret = false;
		} else {
			// must have applied at least one;
			ret = true;
			
		}
		return ret;
	}
	
	 
	public boolean hasAutoAttributes(Class<?> cls) {
		boolean ret = false;
		if (autoAttributes.containsKey(cls)) {
			 ret = true;
		 }
		return ret;
	}

	
	public HashMap<String, String> getAutoAttributes(Class<?> cls) {
		return autoAttributes.get(cls);
	}
}
