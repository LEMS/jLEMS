package org.lemsml.jlems.core.display;

import org.lemsml.jlems.core.logging.E;

public class PrintDataViewer implements DataViewer {

	String title;
	
	int nrep = 0;
	
	public PrintDataViewer(String s) {
		title = s;
	}
	
	
	@Override
	public void addPoint(String line, double x, double y) {
		nrep += 1;
		if (nrep <= 4) {
			E.info("" + nrep + " point: " + line + " " + x + " " + y);
		}
	}

	@Override
	public void addPoint(String line, double x, double y, String scol) {
		addPoint(line, x, y);
	}

	@Override
	public void showFinal() {
		// TODO Auto-generated method stub
		
	}

}
