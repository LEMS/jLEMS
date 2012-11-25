package org.lemsml.jlemsio.reader;

import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.AbstractInclusionReader;
import org.lemsml.jlemsio.util.JUtil;

public class PathInclusionReader extends AbstractInclusionReader {
 
	
	Class<?> root;
	String rootFnm;
	
	
	
	public PathInclusionReader(Class<?> cr, String crf) {
		super();
		root = cr;
		rootFnm = crf;
	}


	public String getRelativeContent(String s) throws ContentError {
		return JUtil.getRelativeResource(root, s);
	}

 
	public String getRootContent() throws ContentError {
		return JUtil.getRelativeResource(root, rootFnm);
	}
	

}
