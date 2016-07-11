package org.lemsml.jlems.api.interfaces;

import java.util.Collection;

import org.lemsml.jlems.api.LEMSBuildOptionsEnum;

public interface ILEMSBuildOptions
{

	public Collection<LEMSBuildOptionsEnum> getOptions();
	
	public void addBuildOption(LEMSBuildOptionsEnum option);
	
	public boolean isOn(LEMSBuildOptionsEnum option);
}
