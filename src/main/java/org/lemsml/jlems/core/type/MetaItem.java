package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.sim.ContentError;

 

public class MetaItem implements BodyValued, MetaContainer {

	public String name;
	public String value;

	public MetaItem meta_tag;
	public MetaItem meta_name;
	public MetaItem meta_value;
	public MetaItem meta_property;


	Meta meta;


	public void addMetaItem(MetaItem mi) throws ContentError {
		if (meta == null) {
			meta = new Meta();
		}
		meta.add(mi);
	}

	public MetaItem(String n) {
		name = n;

	}

	public void setName(String s) {
		name = s;
	}

	public void setValue(String s) {
		value = s;
	}

	public void setBodyValue(String s) {
		value = s;
	}

	public String getBodyValue() {
		return value;
	}
	
}
