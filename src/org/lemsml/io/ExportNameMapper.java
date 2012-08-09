package org.lemsml.io;

import java.util.HashMap;

public interface ExportNameMapper {

	public String getExportAttributeName(Class<?> cls, String attnm);
	
	public String getExportElementName(Class<?> cls);

	public boolean hasAutoAttributes(Class<?> cls);

	public HashMap<String, String> getAutoAttributes(Class<?> cls);

	public boolean elementizesAttribute(Class<?> cls, String att);
		 
	public String getAttributeToElementName(Class<?> cls, String att);

	public boolean pushesDown(Class<?> cls);
	
	public String getPushDown(Class<?> cls);
	
	public String getMappedAttributeValue(Class<?> cls, String anm, String sint);
	
}
