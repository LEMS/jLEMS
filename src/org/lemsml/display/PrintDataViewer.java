package org.lemsml.display;

import org.lemsml.util.E;

public class PrintDataViewer implements DataViewer {

	@Override
	public void addPoint(String line, double x, double y) {
		E.info("point: " + line + " " + x + " " + y);
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
