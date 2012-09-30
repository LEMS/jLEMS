package org.lemsml.jlems.io;

public interface ImportNameMapper {
 
	public String getInternalAttributeName(Class<?> cls, String attnm);

	public boolean renames(String scl);

	public String getInternalElementName(String scl);
	 
}
