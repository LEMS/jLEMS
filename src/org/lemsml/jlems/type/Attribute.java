
package org.lemsml.jlems.type;



public interface Attribute extends NameValuePairOnly {

	public void setFlag();
	public void clearFlag();
	public boolean flagged();
	
}
