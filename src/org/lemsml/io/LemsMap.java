package org.lemsml.io;

public class LemsMap extends NameMapper {
	 
		
	public LemsMap() {
		addGlobalAttributeRename("class", "type");
		addGlobalAttributeRename("extends", "eXtends");
	}
}
