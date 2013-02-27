package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;


@ModelElement(info="Specifies that instances of components based on this class can containe a named collection of other instances. " +
		"This provides for containers for oprating on groups of instances with path and filter expressions defined in " +
		"components to operate over the instance tree.")
public class Collection implements Named {

	@ModelProperty(info="")
	public String name;
	
	public String getName() {
		return name;
	}

	public Collection makeCopy() {
		Collection ret = new Collection();
		ret.name = name;
		return ret;
	}
	
}
