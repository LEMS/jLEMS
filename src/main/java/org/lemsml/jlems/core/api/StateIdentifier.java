/**
 * 
 */
package org.lemsml.jlems.core.api;

import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;

/**
 * @author matteocantarelli
 *
 */
public class StateIdentifier implements IStateIdentifier
{

	private String _state;
	
	public StateIdentifier(String state)
	{
		super();
		this._state = state;
	}

	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.IStateIdentifier#getStatePath()
	 */
	@Override
	public String getStatePath()
	{
		return _state;
	}

}
