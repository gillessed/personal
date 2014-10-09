package net.gillessed.icarus.swingui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Icarus;
import net.gillessed.icarus.event.AbstractProgressListener;
import net.gillessed.icarus.event.ProgressChangeEvent;
import net.gillessed.icarus.export.FlameExporter;
import net.gillessed.icarus.fileIO.FlameFileFilter;
import net.gillessed.icarus.fileIO.FlameReader;
import net.gillessed.icarus.fileIO.FlameWriter;
import net.gillessed.icarus.fileIO.IOUtils;
import net.gillessed.icarus.fileIO.ImageFileView;
import net.gillessed.icarus.geometry.Symmetry;
import net.gillessed.icarus.swingui.color.GradientFrame;

import com.gillessed.gradient.GradientProvider;

public class IcarusFrame {
	/**
	 * A default initial quality value for any newly created flame models.
	 */
	private final static int INITIAL_QUALITY = 6;
	
	/**
	 * A default initial gamma value for any newly created flame models.
	 */
	private final static double INITIAL_GAMMA = 2.2;
	
	private FunctionFrame functionFrame;
	private AffineTransformFrame transformFrame;
	private GradientFrame showGradientFrame;
	private FlameExporter flameExporter;
	private final GradientProvider gradientProvider;
	
	//SWING UI WIDGETS
	private final JFrame frame;
	private final JMenuBar menuBar;
	private final JToolBar toolBar;
	private final JTabbedPane flamePanes;
	private final JMenu fileMenu;
	private final JMenu editMenu;
	private final JMenu toolsMenu;
	private final JMenu helpMenu;
	private final JMenu symmetrySubMenu;
	private final ButtonGroup bg;
	private final JButton redrawFlame;
	private final JMenuItem transformsMenuItem;
	private final JMenuItem functionMenuItem;
	private final JMenuItem showGradientMenuItem;
	private final JMenuItem saveMenuItem;
	private final JMenuItem exportMenuItem;
	private final JMenuItem createGifMenuItem;
	private final JMenuItem gradientEditorMenuItem;
	private final JMenuItem helpMenuItem;
	private final JSpinner qualitySpinner;
	private final JSpinner gammaSpinner;
	private final JProgressBar progressBar;
	private final List<JMenuItem> editItems = new ArrayList<JMenuItem>();
	private final Map<String, JRadioButtonMenuItem> symmetryButtons = new HashMap<String, JRadioButtonMenuItem>();
	//END SWING UI WIDGETS
	
	private final List<FlamePanel> flamePanels;
	private final List<FlameModel> flameModels;
	
	/**
	 * This listens for the quit button in the file menu. It shuts down the program when performed.
	 */
	private final ActionListener quitListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	/**
	 * This listens for the new button in the file menu. It creates a new uniquely named flame model
	 * and adds its panel to the tabbed panes.
	 */
	private final ActionListener newListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Set<String> names = new HashSet<String>();
			for(FlameModel fm : flameModels) {
				names.add(fm.getName());
			}
			int i = 1;
			String newName = "untitled" + i + ".flm";
			while(names.contains(newName)) { i++; newName = "untitled" + i + ".flm"; }
			FlameModel fm = new FlameModel(newName, 
					gradientProvider.getGradientList().get(0), Icarus.symmetryDefault);
			flameModels.add(fm);
			FlamePanel fp = new FlamePanel(fm, progressBar, INITIAL_QUALITY, INITIAL_GAMMA);
			flamePanels.add(fp);
			flamePanes.addTab(newName, fp);
		}
	};
	/**
	 * Listens for the open button in the file menu. It will read the file and open up a corresponding
	 * pane in the tabbed panes. When you save changes to any files opened this way, it will not
	 * ask for a location.
	 */
	private final ActionListener openListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileView(new ImageFileView());
			chooser.setCurrentDirectory(new File(IOUtils.getDirectory()));
			String ext = "flm";
			FlameFileFilter filter = new FlameFileFilter(ext, "Flame Data");
			chooser.setFileFilter(filter);
			int result = chooser.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getPath();
				if(!path.contains(".flm")) {
					path = path + ".flm";
				}
				File f = new File(path);
				FlameReader fr = new FlameReader(f, gradientProvider);
				FlameModel fm = null;
				try {
					fm = fr.read();
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
				fm.setName(f.getName());
				fm.setFile(f);
				flameModels.add(fm);
				FlamePanel fp = new FlamePanel(fm, progressBar, fm.getQuality(), fm.getGamma());
				flamePanels.add(fp);
				flamePanes.addTab(fm.getName(), fp);
			}
		}
	};
	/**
	 * Listens for the save button in the file menu. If the file has not been saved already,
	 * it pops up a file chooser and allows the user to select where they want to save the file.
	 * If it has been saved already, it will save any new changes to the location already set.
	 */
	private final ActionListener saveListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			File test = getSelectedPanel().getFlameModel().getFile();
			if(test == null) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileView(new ImageFileView());
				chooser.setCurrentDirectory(new File(IOUtils.getDirectory()));
				String ext = "flm";
				FlameFileFilter filter = new FlameFileFilter(ext, "Flame Data");
				chooser.setFileFilter(filter);
				int result = chooser.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					String path = chooser.getSelectedFile().getPath();
					if(!path.contains(".flm")) {
						path = path + ".flm";
					}
					File f = new File(path);
					boolean proceedWithWrite = true;
					if(f.exists()) {
						result = JOptionPane.showConfirmDialog(null, "The file " + path + " already exists. " +
								"Do you wish to overwrite it?" , "Warning", JOptionPane.OK_CANCEL_OPTION);
						if(result == JOptionPane.CANCEL_OPTION) {
							proceedWithWrite = false;
						}
					}
					if(proceedWithWrite) {
						getSelectedPanel().getFlameModel().setName(f.getName());
						flamePanes.repaint();
						getSelectedPanel().getFlameModel().setFile(f);
						FlameWriter fw = new FlameWriter(getSelectedPanel().getFlameModel());
						try{
							fw.write(f);
						} catch(IOException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			} else {
				FlameWriter fw = new FlameWriter(getSelectedPanel().getFlameModel());
				try{
					fw.write(test);
				} catch(IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
	};
	private final ActionListener exportListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			flameExporter.setFlameToExport(getSelectedPanel());
			flameExporter.show();
		}
	};
	private final ActionListener createGifListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(getSelectedPanel().getFlameModel().getColorProvider().getGradient() == null) {
				JOptionPane.showMessageDialog(IcarusFrame.this.frame, "Flame gradient is null. "
						+ "Please pick a gradient for your flame.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				CreateGifFrame createGifFrame = new CreateGifFrame(frame, getSelectedPanel().getFlameModel());
				createGifFrame.show();
			}
		}
	};
	private final ActionListener functionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			FlamePanel fp = getSelectedPanel();
			functionFrame = new FunctionFrame(frame,fp.getFlameModel());
			functionFrame.getFrame().addWindowListener(editWindowListener);
			functionFrame.show();
			for(JMenuItem menuItem : editItems) {
				menuItem.setEnabled(false);
			}
		}
	};
	private final ActionListener transformListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			FlamePanel fp = getSelectedPanel();
			transformFrame = new AffineTransformFrame(frame, fp.getFlameModel());
			transformFrame.getFrame().addWindowListener(editWindowListener);
			transformFrame.show();
			for(JMenuItem menuItem : editItems) {
				menuItem.setEnabled(false);
			}
		}
	};
	private ActionListener showGradientListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			showGradientFrame = new GradientFrame(frame, getSelectedPanel().getFlameModel(), gradientProvider.getGradientList());
			showGradientFrame.getFrame().addWindowListener(editWindowListener);
			showGradientFrame.show();
			for(JMenuItem menuItem : editItems) {
				menuItem.setEnabled(false);
			}
		}
	};
	private ActionListener gradientEditorListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			GradientEditorFrame gradientEditorFrame = new GradientEditorFrame(frame, gradientProvider);
			gradientEditorFrame.show();
		}
	};
	private ActionListener helpListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			HelpFrame helpFrame = new HelpFrame(frame);
			helpFrame.show();
		}
	};
	private final WindowListener editWindowListener = new EditWindowListener(editItems);
	/**
	 * This starts the fractal algorithm thread and then the supersampling thread. This redraws the fractal
	 * according to new starting values.
	 */
	private final ActionListener redrawListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(getSelectedPanel().getFlameModel().getColorProvider().getGradient() == null) {
				JOptionPane.showMessageDialog(IcarusFrame.this.frame, "Flame gradient is null. "
						+ "Please pick a gradient for your flame.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				FlamePanel fp = getSelectedPanel();
				fp.addProgressListener(progressListener);
				redrawFlame.setEnabled(false);
				fp.runFractalAlgorithm();
			}
		}
	};
	/**
	 * This changes the quality of the current fractal when the corresponding spinner is changed.
	 */
	private final ChangeListener spinnerChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			SpinnerModel sm = qualitySpinner.getModel();
			if(sm instanceof SpinnerNumberModel) {
				getSelectedPanel().setQuality(((SpinnerNumberModel)sm).getNumber().intValue());
			}
		}
	};
	/**
	 * This changes the gamma of the current fractal when the corresponding spinner is changed.
	 */
	private final ChangeListener gammaChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			SpinnerModel sm = gammaSpinner.getModel();
			if(sm instanceof SpinnerNumberModel) {
				getSelectedPanel().setGamma(((SpinnerNumberModel)sm).getNumber().doubleValue());
			}
		}
	};
	/**
	 * This is fired when we change tabs. It handles two things:
	 * 		1) When the first tab is added, either through new, open, etc... it enabled the use of many of the
	 *	 	menu functions.
	 *		2) Changes the model and updates the UI accordingly to the new model.
	 */
	private final ChangeListener tabChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			if(!exportMenuItem.isEnabled()) {
				exportMenuItem.setEnabled(true);
				createGifMenuItem.setEnabled(true);
				symmetrySubMenu.setEnabled(true);
			}
			for(JRadioButtonMenuItem rbmi : symmetryButtons.values()) {
				rbmi.setSelected(false);
			}
			symmetryButtons.get(getSelectedPanel().getFlameModel().getSymmetry().toString()).setSelected(true);
			if(!saveMenuItem.isEnabled()) {
				saveMenuItem.setEnabled(true);
				for(JMenuItem menuItem : editItems) {
					menuItem.setEnabled(true);
				}
			}
			qualitySpinner.setValue(getSelectedPanel().getQuality());
			qualitySpinner.setEnabled(true);
			gammaSpinner.setValue(getSelectedPanel().getGamma());
			gammaSpinner.setEnabled(true);
			Object o = progressListener.getHolder();
			if(o != null && o instanceof FlamePanel) {
				FlamePanel fp = (FlamePanel)o;
				fp.setRemoveProgressListener(progressListener);
			}
			FlamePanel fp = getSelectedPanel();
			if(fp.isThreadRunning()) {
				fp.addProgressListener(progressListener);
			}
			redrawFlame.setEnabled(!getSelectedPanel().isThreadRunning());
			progressBar.setValue(getSelectedPanel().getProgress());
			progressBar.setString(Math.max(0, getSelectedPanel().getProgress()) + "% - " + getSelectedPanel().getThreadState());
		}
	};
	/**
	 * This listens to progress changes given by the threads drawing the flames.
	 */
	private final AbstractProgressListener progressListener = new AbstractProgressListener() {
		@Override
		public void progressChangeEventPerformed(ProgressChangeEvent e) {
			if(e.isEngineDone()) {
				progressBar.setValue(100);
				FlamePanel fp = getSelectedPanel();
				fp.repaint();
				redrawFlame.setEnabled(true);
			} else {
				progressBar.setValue(e.getProgress());
			}
			progressBar.setString(e.getProgress() + "% - " + getSelectedPanel().getThreadState());
		}
	};
	
	/**
	 * Constructs the default frame and the UI in the frame.
	 */
	public IcarusFrame() {
		gradientProvider = new GradientProvider("resources" + File.separator + "gradients.dat");
		
		flamePanels = new ArrayList<FlamePanel>();
		flameModels = new ArrayList<FlameModel>();
		
		frame = new JFrame();
		flameExporter = new FlameExporter(frame);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Icarus");
		Image iconImage = null;
		try {
			iconImage = ImageIO.read(new File("resources" + File.separator + "flameicon.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		frame.setIconImage(iconImage);
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMenuItem.addActionListener(newListener);
		fileMenu.add(newMenuItem);
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMenuItem.addActionListener(openListener);
		fileMenu.add(openMenuItem);
		saveMenuItem = new JMenuItem("Save");
		if(flameModels.isEmpty()) {
			saveMenuItem.setEnabled(false);
		} 
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.addActionListener(saveListener);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		exportMenuItem = new JMenuItem("Export to file");
		if(flameModels.isEmpty()) {
			exportMenuItem.setEnabled(false);
		} 
		exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exportMenuItem.addActionListener(exportListener);
		fileMenu.add(exportMenuItem);

		createGifMenuItem = new JMenuItem("Create GIF");
		if(flameModels.isEmpty()) {
			createGifMenuItem.setEnabled(false);
		} 
		createGifMenuItem.addActionListener(createGifListener);
		fileMenu.add(createGifMenuItem);
		
		fileMenu.addSeparator();
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(quitListener);
		fileMenu.add(quitMenuItem);
		menuBar.add(fileMenu);
		
		editMenu = new JMenu("Edit");
		functionMenuItem = new JMenuItem("Create Functions");
		functionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		functionMenuItem.addActionListener(functionListener);
		editMenu.add(functionMenuItem);
		transformsMenuItem = new JMenuItem("Change Transformations");
		transformsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		transformsMenuItem.addActionListener(transformListener);
		editMenu.add(transformsMenuItem);
		showGradientMenuItem = new JMenuItem("Pick Gradient");
		showGradientMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		showGradientMenuItem.addActionListener(showGradientListener);
		editMenu.add(showGradientMenuItem);
		symmetrySubMenu = new JMenu("Add Symmetry");
		symmetrySubMenu.setEnabled(false);
		bg = new ButtonGroup();
		for(Symmetry s : Icarus.symmetryMap.values()) {
			JRadioButtonMenuItem rbmi = new JRadioButtonMenuItem(s.toString());
			rbmi.addActionListener(new SymmetrySelectionListener(s.toString()));
			if("None".equals(s.toString())) {
				rbmi.setSelected(true);
			}
			bg.add(rbmi);
			symmetryButtons.put(s.toString(), rbmi);
			symmetrySubMenu.add(rbmi);
		}
		
		editMenu.add(symmetrySubMenu);
		menuBar.add(editMenu);
		
		toolsMenu = new JMenu("Tools");
		gradientEditorMenuItem = new JMenuItem("Gradient Editor");
		gradientEditorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		gradientEditorMenuItem.addActionListener(gradientEditorListener);
		toolsMenu.add(gradientEditorMenuItem);
		menuBar.add(toolsMenu);
		
		helpMenu = new JMenu("Help");
		helpMenuItem = new JMenuItem("How to use");
		helpMenuItem.addActionListener(helpListener);
		helpMenu.add(helpMenuItem);
		menuBar.add(helpMenu);
		
		editItems.add(functionMenuItem);
		editItems.add(transformsMenuItem);
		editItems.add(showGradientMenuItem);
		
		Container cp = frame.getContentPane();
		toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		redrawFlame = new JButton("Redraw");
		redrawFlame.setEnabled(false);
		redrawFlame.addActionListener(redrawListener);
		if(flameModels.isEmpty()) {
			qualitySpinner = new JSpinner(new SpinnerNumberModel(INITIAL_QUALITY,1,Integer.MAX_VALUE,1));
			qualitySpinner.setEnabled(false);
		} else {
			qualitySpinner = new JSpinner(new SpinnerNumberModel(getSelectedPanel().getQuality(),1,Integer.MAX_VALUE,1));
		}
		qualitySpinner.setPreferredSize(new Dimension(100,20));
		qualitySpinner.addChangeListener(spinnerChangeListener);
		
		if(flameModels.isEmpty()) {
			gammaSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_GAMMA,1,Double.MAX_VALUE,0.1));
			gammaSpinner.setEnabled(false);
		} else {
			gammaSpinner = new JSpinner(new SpinnerNumberModel(getSelectedPanel().getGamma(),1,Integer.MAX_VALUE,1));
		}
		gammaSpinner.setPreferredSize(new Dimension(100,20));
		gammaSpinner.addChangeListener(gammaChangeListener);
		
		toolBar.add(redrawFlame);
		toolBar.add(new JLabel("Quality:"));
		toolBar.add(qualitySpinner);
		toolBar.add(new JLabel("Gamma:"));
		toolBar.add(gammaSpinner);
		cp.add(toolBar, BorderLayout.NORTH);
		
		flamePanes = new JTabbedPane();
		flamePanes.addChangeListener(tabChangeListener);
		cp.add(flamePanes);
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		cp.add(progressBar, BorderLayout.SOUTH);
		
		frame.setJMenuBar(menuBar);
		frame.setSize(1024,768);
		
		if(flamePanels.isEmpty()) {
			for(JMenuItem menuItem : editItems) {
				menuItem.setEnabled(false);
			}
		}
	}
	/**
	 * Sets the JFrame to visible.
	 */
	public void show() {
		frame.setVisible(true);
	}
	/**
	 * Gets the panel that is currently being shown by the JTabbedPane. It throws an error if the
	 * selected component in the JTabbedPane is not a FlamePanel.
	 * @return The panel that is currently being shown by the JTabbedPane. Returns null if there is nothing.
	 */
	private FlamePanel getSelectedPanel() {
		Component p = flamePanes.getSelectedComponent();
		if(p == null) return null;
		if(!(p instanceof FlamePanel)) {
			System.err.println("You have something that isn't a flame panel in the FlamePanes");
			System.exit(0);
		}
		FlamePanel fp = (FlamePanel)p;
		return fp;
	}
	
	private class SymmetrySelectionListener implements ActionListener {
		private final String name;

		public SymmetrySelectionListener(String name) {
			this.name = name;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			getSelectedPanel().getFlameModel().setSymmetry(name);
		}
	}
}
