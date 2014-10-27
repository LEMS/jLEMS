/**
 * 
 */
package org.lemsml.jlems.core.api;

import org.lemsml.jlems.core.api.interfaces.ILEMSBuildConfiguration;

/**
 * @author matteocantarelli
 *
 */
public class LEMSBuildConfiguration implements ILEMSBuildConfiguration
{

	private String _target;
	
	public LEMSBuildConfiguration(String target)
	{
		super();
		this._target = target;
	}

	public LEMSBuildConfiguration()
	{
	}
	
	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSBuildConfiguration#getSpecifiedTarget()
	 */
	@Override
	public String getSpecifiedTarget()
	{
		return _target;
	}

}
