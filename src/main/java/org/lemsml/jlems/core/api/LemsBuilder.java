package org.lemsml.jlems.core.api;
 
import java.util.List;

import org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuilder;
import org.lemsml.jlems.core.api.interfaces.ILEMSInstance;
import org.lemsml.jlems.core.api.interfaces.ILEMSState;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.Lems;
 


// NB this should be the only way to access lems components from outside - 
// no direct access to Component or ComponentType objects so we can refactor 
// them and not worry about breaking the API.
public class LemsBuilder implements ILEMSBuilder{

	Lems lems;
	

	public LemsBuilder() {
		lems = new Lems();
	}
	
	
	public void addDimension(String name, DimensionValue ds) {
		 Dimension d = ds.buildDimension(name);
		 lems.addDimension(d);
	}
	
	public void addUnit(String s) {
		E.missing();
	}


	@Override
	public void addDocument(List<Lems> lemsDocument)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<ILEMSState> createLEMSStates(ILEMSBuildOptions options) throws LEMSBuildException
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ILEMSInstance createExecutableInstances() throws LEMSBuildException
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ILEMSInstance build() throws LEMSBuildException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
