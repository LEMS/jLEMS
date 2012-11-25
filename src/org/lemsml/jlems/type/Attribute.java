
package org.lemsml.jlems.type;



public interface Attribute extends NameValuePairOnly {

	void setFlag();
	
	void clearFlag();
	
    boolean flagged();
	
}
