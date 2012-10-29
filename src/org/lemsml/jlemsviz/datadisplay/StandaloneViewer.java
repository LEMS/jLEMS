package org.lemsml.jlemsviz.datadisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import org.lemsml.jlems.display.DataViewPort;
import org.lemsml.jlems.display.DataViewer;
import org.lemsml.jlemsviz.plot.DataDisplay;
import org.lemsml.jlemsviz.plot.DisplayList;
import org.lemsml.jlemsviz.plot.DisplayListPainter;
import org.lemsml.jlemsviz.plot.PaintInstructor;
import org.lemsml.jlemsviz.plot.WorldCanvas;

public class StandaloneViewer implements ActionListener, DataViewer, DataViewPort {
	JFrame frame;

	DataDisplay dataDisplay;

	DisplayList displayList;

	JButton frameB;

	JPanel ptop = new JPanel();

	Color mainBackground = new Color(120, 120, 120);

	HashMap<String, String> traceInfo = new HashMap<String, String>();

	boolean setRange = false;
	double[] region;
	
	Long lastUpdate = 0l;

	public StandaloneViewer() {
		this("");
	}

	public String getTitle() {
		return frame.getTitle();
	}

	public StandaloneViewer(String title) {
		frame = new JFrame(title);
		frame.setPreferredSize(new Dimension(550, 450));
		Container ctr = frame.getContentPane();

		// viewer = new SceneGraphViewer();
		ctr.setLayout(new BorderLayout(2, 2));
		// ctr.add(viewer.getPanel(), BorderLayout.CENTER);

		JPanel jp = new JPanel();
		ptop.setLayout(new BorderLayout(2, 2));
		ptop.add(jp, BorderLayout.CENTER);
		ctr.add(ptop, BorderLayout.NORTH);

		jp.setLayout(new FlowLayout(FlowLayout.CENTER));

		ButtonGroup group = new ButtonGroup();
		String[] modes = { WorldCanvas.PAN, WorldCanvas.ZOOM, WorldCanvas.BOX, WorldCanvas.MULTI };
		String[] lbls = { "Pan", "Zoom", "Box", "Multi" };
		for (int i = 0; i < modes.length; i++) {
			JRadioButton jrb = new JRadioButton(lbls[i]);
			group.add(jrb);
			jp.add(jrb);
			jrb.setActionCommand(modes[i]);
			jrb.addActionListener(this);
			if (i == 0) {
				jrb.setSelected(true);
			}
		}
		frameB = new JButton("Frame");
		ptop.add(frameB, BorderLayout.EAST);
		frameB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				frameData();
			}
		});

		dataDisplay = new DataDisplay(500, 400);
		ctr.add(dataDisplay, BorderLayout.CENTER);

		dataDisplay.setBg(mainBackground);
		dataDisplay.setMode("antialias", true);

		dataDisplay.setMode("mouse", WorldCanvas.PAN);

		dataDisplay.setMode("labels", true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		displayList = new DisplayList();

		setData(displayList);

		show();
	}

	public void setRegion(double[] d) {
		if (d != null && d.length == 4) {
			region = d;
			dataDisplay.setXRange(d[0], d[1]);
			dataDisplay.setYRange(d[2], d[3]);
			setRange = true;
		}
	}
	
	
	public void setPainter(PaintInstructor pi) {
		dataDisplay.setPaintInstructor(pi);
	}

	public void show() {
		frame.pack();
		frame.setVisible(true);
	}

	public void frameData() {
		dataDisplay.frameData();
	}

	private void setData(DisplayList dl) {
		DisplayListPainter dlp = new DisplayListPainter(dl);
		dlp.setRepaintable(dataDisplay);
		setPainter(dlp);
	}

	public void actionPerformed(ActionEvent e) {
		String sev = e.getActionCommand();
		dataDisplay.setMode("mouse", sev);

	}

	public void addPoint(String s, double x, double y, String color) {
		displayList.addPoint(s, x, y, color);

		if (!traceInfo.containsKey(s)) {
			traceInfo.put(s, color);
			updateToolTip();
		}
		checkUpdate();
	}

	public void addPoint(String s, double x, double y) {
		addPoint(s, x, y, "#ffffff");
	}

	private void checkUpdate() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdate > 100) {
			lastUpdate = currentTime;
		 	
			if (!setRange) {
				frameData();
			}
			dataDisplay.requestRepaint();
		}
	}

	public static void main(String[] argv) {
		StandaloneViewer sv = new StandaloneViewer();

		int numPoints = 200;
		for (int i = 0; i < numPoints - 5; i++) {
			double maxR = 2 * Math.PI * 2;
			double r = maxR * i / numPoints;
			sv.addPoint("y = sin(x)", r, Math.sin(r), "#00FF00");
			sv.addPoint("y = cos(x)", r, Math.cos(r), "#0000FF");
			sv.addPoint("y = abs(x)", r, Math.abs(r));
		}

		sv.frameData();
		sv.show();
	}

	private void updateToolTip() {
		UIManager.put("ToolTip.background", new ColorUIResource(mainBackground));
		UIManager.put("ToolTip.foreground", new ColorUIResource(Color.white));

		StringBuilder sb = new StringBuilder("<html><b>Traces present:</b><br/>");
		for (String id : traceInfo.keySet()) {
			String c = traceInfo.get(id);

			sb.append("&nbsp;&nbsp;<font color=\"" + c + "\">" + id + "</font><br/>");
		}
		sb.append("</html>");

		ptop.setToolTipText(sb.toString());
		frameB.setToolTipText(sb.toString());

	}

	@Override
	public void showFinal() {
//		frameData();
//		show();
	}

}
