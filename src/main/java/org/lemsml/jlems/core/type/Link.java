package org.lemsml.jlems.core.type;
import org.lemsml.jlems.core.annotation.ModelElement;

@ModelElement(info="Like a ComponentRef, but resolved relative to the enclosing object so the target must already be " +
		"in the model. One or the other should be deprecated. " +
		"The Link element has the same properties as ComponentRef. The Link element just " +
		"establishes a connection with the target component, but leaves it in its existing place in the " +
		"hierarchy. Variables in the target component can be accessed via the name of the link element.  ")
public class Link extends ComponentReference {

	
	public Link() {
		super();
		// TODO - only one
	}
	
	public Link(String name, String typeName, ComponentType t) {
		super(name, typeName, t);
	}


    @Override
	public boolean isLocal() {
		return true;
	}
	
	
	public Link makeLinkCopy() {
		 return new Link(name, type, r_type);
	}
}
