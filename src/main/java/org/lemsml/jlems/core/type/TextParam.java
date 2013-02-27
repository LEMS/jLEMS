package org.lemsml.jlems.core.type;

public class TextParam {

	String name;
	String value;
	
	public TextParam(String snm, String sval) {
		name = snm;
		value = sval;
	}

        @Override
        public String toString() {
            return value;
        }



	public String getName() {
		return name;
	}
	
	public String getText() {
		 return value;
	}
	
	
}
