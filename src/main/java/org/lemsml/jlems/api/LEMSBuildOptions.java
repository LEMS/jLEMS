/**
 * 
 */
package org.lemsml.jlems.api;

import java.util.ArrayList;
import java.util.Collection;

import org.lemsml.jlems.api.interfaces.ILEMSBuildOptions;

/**
 * @author matteocantarelli
 *
 */
public class LEMSBuildOptions implements ILEMSBuildOptions
{

	private Collection<LEMSBuildOptionsEnum> _options=new ArrayList<LEMSBuildOptionsEnum>();

	@Override
	public Collection<LEMSBuildOptionsEnum> getOptions()
	{
		return _options;
	}

	@Override
	public void addBuildOption(LEMSBuildOptionsEnum option)
	{
		_options.add(option);
	}

	@Override
	public boolean isOn(LEMSBuildOptionsEnum option)
	{
		return _options.contains(option);
	}

}
