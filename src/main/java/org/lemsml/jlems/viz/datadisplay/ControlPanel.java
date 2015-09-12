package org.lemsml.jlems.viz.datadisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.io.reader.FileInclusionReader;

public class ControlPanel implements ActionListener {
	JFrame frame;
	
	File workingFile;
	
	Sim simulation;
	Map<Integer, RunConfig> runConfigs = new HashMap<Integer, RunConfig>();
	Map<String, Rectangle> viewerBounds = new HashMap<String, Rectangle>();
	
	JPanel pmain = new JPanel();
	Dimension windowDimension = new Dimension(200, 300);

	Color mainBackground = new Color(120, 120, 120);
	// RCC displayList contains all the data that traceInfo was saving
	
	boolean setRange = false;
	double[] region;
	
	Long lastUpdate = 0l;
	
	private static ControlPanel instance;
	
	public static ControlPanel getInstance(String title) {
		if(instance == null)
			instance = new ControlPanel(title);
		
		return instance;
	}

	public ControlPanel registerSimulation(Sim sim, File simFile) {
		simulation = sim;
		
		if(simFile != null) {
			workingFile = simFile;
		}
		
		loadRunConfigsFromSimulation();
		
		positionViewers();
		
		return this;
	}
	
	public String getTitle() {
		return frame.getTitle();
	}

	private ControlPanel(String title) {
		
		frame = new JFrame(title);
		frame.setPreferredSize(windowDimension);
		Container ctr = frame.getContentPane();

	 
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("File");
		String[] actions = {"Open", "Exit"};
		addToMenu(actions, jm);
		jmb.add(jm);
		
		JMenu jvm = new JMenu("View");
		String[] viewActions = {"Bring to Front"};
		addToMenu(viewActions, jvm);
		jmb.add(jvm);
		
		JMenu jmsimulation = new JMenu("Simulation");
		addToMenuWithShortcut("Reload and Run", jmsimulation, KeyEvent.VK_F6, 0 );
		jmb.add(jmsimulation);	
		
		frame.setJMenuBar(jmb);
		
		ctr.add(pmain, BorderLayout.SOUTH);
		
		createToolbar();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		show();
	}
	
	public void createToolbar() {
		
		int iconSize = 30;
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
	    toolbar.setRollover(true);
	    toolbar.setPreferredSize(new Dimension(0,iconSize+20));
	    
	    URL imgURL = getClass().getResource("/org/lemsml/jlems/viz/datadisplay/open.png");
	    ImageIcon iconOpen = new ImageIcon(imgURL);
	    Image img = iconOpen.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
	    iconOpen.setImage(img);
	    JButton buttonOpen = new JButton(iconOpen);
	    buttonOpen.setSize(iconSize,iconSize);
	    buttonOpen.setToolTipText("Open");
	    toolbar.add(buttonOpen);
	    
	    imgURL = getClass().getResource("/org/lemsml/jlems/viz/datadisplay/layer.png");
	    ImageIcon iconBringToFront = new ImageIcon(imgURL);
	    img = iconBringToFront.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
	    iconBringToFront.setImage(img);
	    JButton buttonBringToFront = new JButton(iconBringToFront);
	    buttonBringToFront.setSize(iconSize,iconSize);
	    buttonBringToFront.setToolTipText("Bring Windows to Front");
	    toolbar.add(buttonBringToFront);
	    
	    imgURL = getClass().getResource("/org/lemsml/jlems/viz/datadisplay/run.png");
	    ImageIcon iconReloadAndRun = new ImageIcon(imgURL);
	    img = iconReloadAndRun.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
	    iconReloadAndRun.setImage(img);
	    JButton buttonReloadAndRun = new JButton(iconReloadAndRun);
	    buttonReloadAndRun.setSize(iconSize,iconSize);
	    buttonReloadAndRun.setToolTipText("Load and Run");
	    toolbar.add(buttonReloadAndRun);
	    
	    frame.add(toolbar, BorderLayout.NORTH);
	}
	
	public void loadRunConfigsFromSimulation() {
		
		int index = -1;
		for(RunConfig conf : simulation.getRunConfigs()) {			
			runConfigs.put(index++, conf);
		}
		
	}
	
	public void positionViewers() {
		int borderWidth = 10;
		int layerWidth = 30;
		int start_cursor_x = (int)windowDimension.getWidth() + borderWidth;
		int start_cursor_y = 0;
		
		frame.setLocation(0, 0);
		
		int screenWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
		int screenHeight = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
		
		int cursor_x = start_cursor_x;
		int cursor_y = start_cursor_y;
		
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer sViewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				
				if(viewerBounds.containsKey(key)) {
					sViewer.setViewerRectangle(viewerBounds.get(key));
					sViewer.show();
					continue;
				}
				
				sViewer.setPosition(cursor_x, cursor_y);
				sViewer.show();
				
				cursor_y += sViewer.getDimensions().getHeight() + borderWidth;
				
				if((cursor_y + sViewer.getDimensions().getHeight()) > screenHeight - start_cursor_y) {
					cursor_y = start_cursor_y;
					if((cursor_x + sViewer.getDimensions().getWidth()) > screenWidth - start_cursor_x) {
						start_cursor_y += layerWidth;
						start_cursor_x += layerWidth;
						cursor_y = start_cursor_y;
						cursor_x = start_cursor_x;
					} else {
						cursor_x += sViewer.getDimensions().getWidth() + borderWidth;
					}
				} else {
					cursor_y += sViewer.getDimensions().getHeight() + borderWidth;
				}
			}
		}
		
		//give focus to control panel
		frame.setVisible(true);

	}

	private void addToMenu(String[] actions, JMenu jm) {
	for (String s : actions) {
		JMenuItem jmi = new JMenuItem(s);
		jmi.setActionCommand(s.toLowerCase());
		jmi.addActionListener(this);
		jm.add(jmi);
	}
	}
	
	/**
	 * 
	 * @param action - The name of the action item
	 * @param jm - the menu for this item to be added to
	 * @param key - int representing the ID of KeyEvent (eg KeyEvent.VK_F6)
	 * @param modifier - int representing the ID of ActionEvent (eg ActionEvent.ALT_MASK , 0 for no modifier)
	 */
	private void addToMenuWithShortcut(String action, JMenu jm, int key, int modifier) {
		JMenuItem jmi = new JMenuItem(action);
		jmi.setActionCommand(action.toLowerCase());
		jmi.addActionListener(this);
		jmi.setAccelerator(KeyStroke.getKeyStroke(key, modifier));
		jm.add(jmi);
	}
	
	public void show() {
		frame.pack();
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String sev = e.getActionCommand();
		
		if (sev.equals("open")) {
			importNewFile();
			try {
				simulation.run();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(new JFrame(), 
						String.format("Failed to run simulation : %s", ex.getMessage()), 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
		} else if (sev.equals("exit")) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		} else if(sev.equals("bring to front")) {
			bringAllViewersToForeground();
		} else if (sev.equals("reload and run")) {
			if(simulation != null)
				try {
					clearCurrentSimulation();
					importFile();
					for(Entry<Integer, RunConfig> conf : runConfigs.entrySet()) {
						simulation.run(conf.getValue(), false);
					}
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(new JFrame(), 
							String.format("Failed to run simulation : %s", ex.getMessage()), 
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	
	public void clearCurrentSimulation() {
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer viewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				viewerBounds.put(key, viewer.getViewerRectangle());
				viewer.close();
			}
		}
	}
	
	public void bringAllViewersToForeground() {
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer viewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				viewer.show();
			}
		}
		
		//give focus to control panel
		frame.setVisible(true);

	}
	
	public void importNewFile() {
		//importing new file so reset the windows
		File newfile = SwingDialogs.getInstance().getFileToRead();
		
		if(newfile == null)
			return;
		
		workingFile = newfile;
		
		clearCurrentSimulation();
		viewerBounds.clear();
		runConfigs.clear();
		
		importFile();
	}
	
	public void importFile() {
		FileInclusionReader fir = new FileInclusionReader(workingFile);
		
		try {
			Sim sim = new Sim(fir.read());
            
	        sim.readModel();
	        sim.build();
	        
	        ControlPanel.getInstance("jLEMS").registerSimulation(sim, null);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), 
					String.format("Unexpected error while opening model file with message : %s", e.getMessage()), 
					"Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	
}
