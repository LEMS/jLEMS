/**
 * 
 */
package org.lemsml.jlems.core.api;

/**
 * @author matteocantarelli
 *
 */
public class LEMSDoubleValue extends ALEMSValue
{

	private double _value;

	public LEMSDoubleValue(double value)
	{
		_value=value;
	}

	@Override
	public String getStringValue()
	{
		return Double.toString(_value);
	}
	
	/**
	 * @return
	 */
	public double getAsDouble()
	{
		return _value;
	}
	
	
}
