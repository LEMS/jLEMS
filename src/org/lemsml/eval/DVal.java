package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class DVal {

	public abstract double eval();

	public abstract void recAdd(ArrayList<DVar> val);

    @Override
	public abstract String toString();

	public abstract String toString(String prefix, ArrayList<String> ignore);

	public abstract DVal makeCopy();

	public abstract DVal makePrefixedCopy(String pfx, HashSet<String> stetHS);

	public abstract void substituteVariableWith(String vnm, String pth);

	public abstract boolean variablesIn(HashSet<String> known);
	 
}
