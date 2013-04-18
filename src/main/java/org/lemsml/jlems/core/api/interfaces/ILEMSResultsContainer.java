/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import org.lemsml.jlems.core.api.LEMSExecutionException;
import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.out.ResultWriter;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSResultsContainer
{

	public DataViewer getDataViewer(String displayViewer);

	public ResultWriter getResultWriter(String resultWriter);

	public void closeResultWriters() throws LEMSExecutionException;

	public void advanceResultWriters(double t) throws LEMSExecutionException;

}
