package org.lemsml.jlems.viz.datadisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.io.reader.FileInclusionReader;

public class ControlPanel implements ActionListener {
	
	JFrame frame;
	JPanel pmain = new JPanel();
	JLabel statusLabel = new JLabel("");
	
	File workingFile;
	Sim simulation;
	
	Map<Integer, RunConfig> runConfigs = new HashMap<Integer, RunConfig>();
	Map<String, Rectangle> viewerRects = new HashMap<String, Rectangle>();
	
	Dimension windowDimension = new Dimension(200, 300);
	
	ExecutorService multiThreadService = Executors.newFixedThreadPool(1);
	
	private static ControlPanel instance;
	
	public static ControlPanel getInstance() {
		if(instance == null)
			instance = new ControlPanel();
		
		return instance;
	}

	private ControlPanel() {
		
		frame = new JFrame("jLEMS");
		frame.setPreferredSize(windowDimension);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container ctr = frame.getContentPane();
	 
		// Set up the menu items
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
		
		// Set up the status bar at the bottom of the window
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusLabel.setVerticalAlignment(SwingConstants.TOP);
		statusLabel.setFont(new Font(statusLabel.getFont().getFontName(), 10, 10));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.add(statusLabel);
		
		ctr.add(pmain, BorderLayout.SOUTH);
		ctr.add(statusPanel, BorderLayout.SOUTH);
		
		createToolbar();
		
		show();
	}
	
	/**
	 * The control panel handles one simulation at a time, this should be "registered" using this method.
	 * Load all the windows, one per display
	 * @param sim - simulation object
	 * @param simFile - new file to load (can be null)
	 * @throws ConnectionError
	 * @throws ContentError
	 * @throws RuntimeError
	 * @throws ParseError
	 */
	public void registerSimulation(Sim sim, File simFile) 
			throws ConnectionError, ContentError, RuntimeError, ParseError {
		simulation = sim;
		
		// if we're reloading the same file, we would expect simFile to be null
		if(simFile != null) {
			workingFile = simFile;
			statusLabel.setText(workingFile.getName());
		}
		
		loadRunConfigsFromSimulation();
		
		positionViewers();
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	/**
	 * The toolbar for the control panel - open, layer and run
	 * The buttons have matching menu items performing the same actions 
	 */
	private void createToolbar() {
		
		int iconSize = 20;
		
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
	    buttonOpen.setActionCommand("open");
	    buttonOpen.addActionListener(this);
	    toolbar.add(buttonOpen);
	    
	    imgURL = getClass().getResource("/org/lemsml/jlems/viz/datadisplay/layer.png");
	    ImageIcon iconBringToFront = new ImageIcon(imgURL);
	    img = iconBringToFront.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
	    iconBringToFront.setImage(img);
	    JButton buttonBringToFront = new JButton(iconBringToFront);
	    buttonBringToFront.setSize(iconSize,iconSize);
	    buttonBringToFront.setToolTipText("Bring Windows to Front");
	    buttonBringToFront.setActionCommand("bring to front");
	    buttonBringToFront.addActionListener(this);
	    toolbar.add(buttonBringToFront);
	    
	    imgURL = getClass().getResource("/org/lemsml/jlems/viz/datadisplay/run.png");
	    ImageIcon iconReloadAndRun = new ImageIcon(imgURL);
	    img = iconReloadAndRun.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
	    iconReloadAndRun.setImage(img);
	    JButton buttonReloadAndRun = new JButton(iconReloadAndRun);
	    buttonReloadAndRun.setSize(iconSize,iconSize);
	    buttonReloadAndRun.setToolTipText("Reload and Run");
	    buttonReloadAndRun.setActionCommand("reload and run");
	    buttonReloadAndRun.addActionListener(this);
	    toolbar.add(buttonReloadAndRun);
	    
	    frame.add(toolbar, BorderLayout.NORTH);
	}
	
	/**
	 * load the runConfigs from the simulation into the runConfigs map.
	 * The runConfigs map is indexed for easy referencing
	 */
	private void loadRunConfigsFromSimulation() {
		int index = -1;
		for(RunConfig conf : simulation.getRunConfigs()) {			
			runConfigs.put(index++, conf);
		}
	}

	/**
	 * Lay out the StandaloneViewer windows in a  
	 */
	private void positionViewers() {
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
				
				// If there's a viewer Rect with this key (we're reloading an existing file) let's use it
				// instead of putting it back in the default position
				if(viewerRects.containsKey(key)) {
					sViewer.setViewerRectangle(viewerRects.get(key));
					// use show() here to repaint the window and bring it to the foreground
					// to get a nice cascading effect of the windows
					sViewer.show();
					continue;
				}
				
				// 
				sViewer.setViewerRectangle(new Rectangle(cursor_x, cursor_y, 
						(int)sViewer.getDimensions().getWidth(), (int)sViewer.getDimensions().getHeight()));
				// use show() here to repaint the window and bring it to the foreground
				// to get a nice cascading effect of the windows
				sViewer.show();
				
				cursor_y += sViewer.getDimensions().getHeight() + borderWidth;
				
				// first try to place a window below the last one, if that'll go over the bottom edge,
				// then place it to the right. If to the right goes over the right edge then
				// begin again with a slight offset
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
				}
			}
		}
		
		//give focus to control panel for easy spamming of the F6 key!
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
	
	/**
	 * When simulation.run() is called from the actionPerformed method below, it holds up the 
	 * Java Swing display thread and we don't get the nice animation, so call run() in its own thread here.
	 */
	private void runSimulationInNewThread() {
		for(final Entry<Integer, RunConfig> conf : runConfigs.entrySet()) {
			multiThreadService.execute(new Runnable() {

				@Override
				public void run() {
					try {
						simulation.run(conf.getValue(), false);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(new JFrame(), 
								String.format("Failed to run simulation : %s", ex.getMessage()), 
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			});
		}
	}

	public void actionPerformed(ActionEvent e) {
		String sev = e.getActionCommand();
		
		if (sev.equals("open")) {
			importNewFile();
			runSimulationInNewThread();
		} else if (sev.equals("exit")) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		} else if(sev.equals("bring to front")) {
			bringAllViewersToForeground();
		} else if (sev.equals("reload and run")) {
			if(simulation != null) {
				clearCurrentSimulation();
				importFile();
				runSimulationInNewThread();
			}
		}
	}
	
	/*
	 * For each StandaloneViewer window, store the size and location in viewerRects to be remembered
	 * when reloading. Then close the window.
	 */
	private void clearCurrentSimulation() {
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer viewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				viewerRects.put(key, viewer.getViewerRectangle());
				viewer.close();
			}
		}
	}
	
	/*
	 * Bring all the StandaloneViewer windows to the foreground
	 */
	private void bringAllViewersToForeground() {
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer viewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				viewer.show();
			}
		}
		
		//give focus to control panel
		frame.setVisible(true);

	}
	
	private void importNewFile() {
		//importing new file so reset the windows
		File newfile = SwingDialogs.getInstance().getFileToRead();
		
		if(newfile == null)
			return;
		
		workingFile = newfile;
		
		statusLabel.setText(workingFile.getName());
		
		clearCurrentSimulation();
		viewerRects.clear();
		runConfigs.clear();
		
		importFile();
	}
	
	private void importFile() {
		FileInclusionReader fir = new FileInclusionReader(workingFile);
		
		try {
			Sim sim = new Sim(fir.read());
            
	        sim.readModel();
	        sim.build();
	        
	        ControlPanel.getInstance().registerSimulation(sim, null);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), 
					String.format("Unexpected error while opening model file with message : %s", e.getMessage()), 
					"Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	
}
