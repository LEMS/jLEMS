/**
 * 
 */
package org.lemsml.jlems.api;

import org.lemsml.jlems.api.interfaces.IStateIdentifier;

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

	@Override
	public String toString()
	{
		return _state;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_state == null) ? 0 : _state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		StateIdentifier other = (StateIdentifier) obj;
		if(_state == null)
		{
			if(other._state != null) return false;
		}
		else if(!_state.equals(other._state)) return false;
		return true;
	}
	

}
