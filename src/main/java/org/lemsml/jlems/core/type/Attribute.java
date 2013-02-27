
package org.lemsml.jlems.core.type;



public interface Attribute extends NameValuePairOnly {

	void setFlag();
	
	void clearFlag();
	
    boolean flagged();
	
}
