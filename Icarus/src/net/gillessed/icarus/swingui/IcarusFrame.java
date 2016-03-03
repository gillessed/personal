package net.gillessed.icarus.swingui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Icarus;
import net.gillessed.icarus.event.NewFlameListener;
import net.gillessed.icarus.export.FlameExportDialog;
import net.gillessed.icarus.fileIO.FlameFileFilter;
import net.gillessed.icarus.fileIO.FlameReader;
import net.gillessed.icarus.fileIO.FlameWriter;
import net.gillessed.icarus.fileIO.IOUtils;
import net.gillessed.icarus.fileIO.ImageFileView;
import net.gillessed.icarus.geometry.Symmetry;
import net.gillessed.icarus.swingui.explorer.ExplorerFrame;
import net.gillessed.icarus.swingui.flame.FlamePanel;
import net.gillessed.icarus.swingui.function.FunctionFrame;
import net.gillessed.icarus.swingui.function.GradientEditorFrame;
import net.gillessed.icarus.swingui.gif.CreateGifFrame;
import net.gillessed.icarus.swingui.gradient.ColorProvider;
import net.gillessed.icarus.swingui.gradient.GradientColorProvider;
import net.gillessed.icarus.swingui.gradient.GradientFrame;
import net.gillessed.icarus.swingui.help.HelpFrame;
import net.gillessed.icarus.swingui.transform.TransformFrame;

import com.gillessed.gradient.Gradient;
import com.gillessed.gradient.GradientProvider;

public class IcarusFrame {

	private final FunctionFrame functionFrame;
	private final TransformFrame transformFrame;
	private final GradientFrame showGradientFrame;
	private final FlameExportDialog flameExporter;
	private final GradientProvider gradientProvider;
	private final GradientEditorFrame gradientEditorFrame;
	private final HelpFrame helpFrame;
	private final CreateGifFrame createGifFrame;
	private final ExplorerFrame explorerFrame;

	//SWING UI WIDGETS
	private final JFrame frame;
	private final JMenuBar menuBar;
	private final JToolBar toolBar;
	private final JMenu fileMenu;
	private final JMenu editMenu;
	private final JMenu toolsMenu;
	private final JMenu helpMenu;
	private final JMenu symmetrySubMenu;
	private final ButtonGroup bg;
	private final JButton renderFlameButton;
	private final JMenuItem transformsMenuItem;
    private final JMenuItem transformExplorerMenuItem;
	private final JMenuItem functionMenuItem;
	private final JMenuItem showGradientMenuItem;
	private final JMenuItem saveMenuItem;
	private final JMenuItem exportMenuItem;
	private final JMenuItem createGifMenuItem;
	private final JMenuItem gradientEditorMenuItem;
	private final JMenuItem helpMenuItem;
	private final JSpinner qualitySpinner;
	private final JSpinner gammaSpinner;
	private final JCheckBox blurCheckbox;
	private final JProgressBar progressBar;
	private final FlamePanel flamePanel;
	private final Map<String, JRadioButtonMenuItem> symmetryButtons = new HashMap<String, JRadioButtonMenuItem>();
	//END SWING UI WIDGETS

	private final FlameModelContainer flameModelContainer;

	/**
	 * This listens for the quit button in the file menu. It shuts down the program when performed.
	 */
	private final ActionListener quitListener = new ActionListener() {
		@Override
        public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	/**
	 * This listens for the new button in the file menu. It creates a new uniquely named flame model
	 * and adds its panel to the tabbed panes.
	 */
	private final ActionListener newListener = new ActionListener() {
		@Override
        public void actionPerformed(ActionEvent e) {
			String newName = "untitled.flm";
			FlameModel flameModel = new FlameModel(
					newName,
					new GradientColorProvider(gradientProvider.getGradientList().get(0)),
					Icarus.symmetryDefault);
			flameModelContainer.setFlameModel(flameModel);
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
				FlameReader fr = new FlameReader(f);
				try {
					FlameModel flameModel = fr.read();
					flameModel.setName(f.getName());
					flameModel.setFile(f);
					flameModelContainer.setFlameModel(flameModel);
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
			}
		}
	};
	/**
	 * Listens for the save button in the file menu. If the file has not been saved already,
	 * it pops up a file chooser and allows the user to select where they want to save the file.
	 * If it has been saved already, it will save any new changes to the location already set.
	 */
	private final ActionListener saveListener = new ActionListener() {
		@Override
        public void actionPerformed(ActionEvent e) {
			File test = flameModelContainer.getFlameModel().getFile();
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
						flameModelContainer.getFlameModel().setName(f.getName());
						flameModelContainer.getFlameModel().setFile(f);
						FlameWriter flameWriter = new FlameWriter(flameModelContainer.getFlameModel());
						try{
							flameWriter.write(f);
						} catch(IOException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			} else {
				FlameWriter flameWriter = new FlameWriter(flameModelContainer.getFlameModel());
				try{
					flameWriter.write(test);
				} catch(IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
	};

	private final ActionListener exportListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			flameExporter.setFlameModel(flameModelContainer.getFlameModel());
			flameExporter.show();
		}
	};

	private final ActionListener createGifListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(flameModelContainer.getFlameModel().getColorProvider() == null) {
				JOptionPane.showMessageDialog(IcarusFrame.this.frame, "Flame color is null. "
						+ "Please pick a color for your flame.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				createGifFrame.show();
			}
		}
	};

	private final ActionListener functionListener = new ActionListener() {
		@Override
        public void actionPerformed(ActionEvent e) {
			functionFrame.show();
		}
	};

	private final ActionListener transformListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			transformFrame.show();
		}
	};

    private final ActionListener transformExplorerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            explorerFrame.show();
        }
    };

	private ActionListener showGradientListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			showGradientFrame.show();
		}
	};

	private ActionListener gradientEditorListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			gradientEditorFrame.show();
		}
	};

	private ActionListener helpListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			helpFrame.show();
		}
	};

	/**
	 * This starts the fractal algorithm thread and then the supersampling thread. This redraws the fractal
	 * according to new starting values.
	 */
	private final ActionListener renderFlameListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(flameModelContainer.getFlameModel().getColorProvider() == null) {
				JOptionPane.showMessageDialog(IcarusFrame.this.frame, "Flame color is null. "
						+ "Please pick a color for your flame.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
                    flamePanel.runFractalAlgorithm();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error rendering flame: " + e1.fillInStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
				flameModelContainer.getFlameModel().setQuality(((SpinnerNumberModel)sm).getNumber().intValue());
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
				flameModelContainer.getFlameModel().setGamma(((SpinnerNumberModel)sm).getNumber().doubleValue());
			}
		}
	};

	/**
	 * This toggles the blur portion of rendering for the fractal.
	 */
	private final ChangeListener blurChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			flameModelContainer.getFlameModel().setBlur(blurCheckbox.isSelected());
		}
	};

	/**
	 * This is fired when we change tabs. It handles two things:
	 * 		1) When the first tab is added, either through new, open, etc... it enabled the use of many of the
	 *	 	menu functions.
	 *		2) Changes the model and updates the UI accordingly to the new model.
	 */
	private final NewFlameListener flameChangeListener = new NewFlameListener() {

		@Override
		public void newFlame(FlameModel flameModel) {
			exportMenuItem.setEnabled(true);
			createGifMenuItem.setEnabled(true);
			symmetrySubMenu.setEnabled(true);
			functionMenuItem.setEnabled(true);
			transformsMenuItem.setEnabled(true);
            transformExplorerMenuItem.setEnabled(true);
			showGradientMenuItem.setEnabled(true);
			for(JRadioButtonMenuItem rbmi : symmetryButtons.values()) {
				rbmi.setSelected(false);
			}
			symmetryButtons.get(flameModelContainer.getFlameModel().getSymmetry().toString()).setSelected(true);
			if(!saveMenuItem.isEnabled()) {
				saveMenuItem.setEnabled(true);
			}
			qualitySpinner.setValue(flameModelContainer.getFlameModel().getQuality());
			qualitySpinner.setEnabled(true);
			gammaSpinner.setValue(flameModelContainer.getFlameModel().getGamma());
			gammaSpinner.setEnabled(true);
			blurCheckbox.setEnabled(flameModelContainer.getFlameModel().isBlur());
			blurCheckbox.setEnabled(true);
			renderFlameButton.setEnabled(true);

			frame.setTitle("Icarus - " + flameModelContainer.getFlameModel().getName());
		}
	};

	private class SymmetrySelectionListener implements ActionListener {
		private final String name;

		public SymmetrySelectionListener(String name) {
			this.name = name;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			flameModelContainer.getFlameModel().setSymmetry(name);
			flameModelContainer.flameModified();
		}
	}

	/**
	 * Constructs the default frame and the UI in the frame.
	 */
	public IcarusFrame() {
		flameModelContainer = new FlameModelContainer();
		flameModelContainer.addNewFlameListener(flameChangeListener);
		gradientProvider = new GradientProvider("resources" + File.separator + "gradients.dat");

		frame = new JFrame();
		flameExporter = new FlameExportDialog(frame);

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
		saveMenuItem.setEnabled(false);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.addActionListener(saveListener);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		exportMenuItem = new JMenuItem("Export to file");
		exportMenuItem.setEnabled(false);
		exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exportMenuItem.addActionListener(exportListener);
		fileMenu.add(exportMenuItem);

		createGifMenuItem = new JMenuItem("Create GIF");
		createGifMenuItem.setEnabled(false);
		createGifMenuItem.addActionListener(createGifListener);
		fileMenu.add(createGifMenuItem);

		fileMenu.addSeparator();
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(quitListener);
		fileMenu.add(quitMenuItem);
		menuBar.add(fileMenu);

		editMenu = new JMenu("Edit");

		functionMenuItem = new JMenuItem("Create Functions");
		functionMenuItem.setEnabled(false);
		functionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		functionMenuItem.addActionListener(functionListener);
		editMenu.add(functionMenuItem);

		transformsMenuItem = new JMenuItem("Change Transformations");
		transformsMenuItem.setEnabled(false);
		transformsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		transformsMenuItem.addActionListener(transformListener);
		editMenu.add(transformsMenuItem);

        transformExplorerMenuItem = new JMenuItem("Transformation Explorer");
        transformExplorerMenuItem.setEnabled(false);
        transformExplorerMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        transformExplorerMenuItem.addActionListener(transformExplorerListener);
        editMenu.add(transformExplorerMenuItem);

		showGradientMenuItem = new JMenuItem("Pick Gradient");
		showGradientMenuItem.setEnabled(false);
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

		Container cp = frame.getContentPane();
		toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		renderFlameButton = new JButton("Redraw");
		renderFlameButton.setEnabled(false);
		renderFlameButton.addActionListener(renderFlameListener);

		qualitySpinner = new JSpinner(new SpinnerNumberModel(FlameModel.INITIAL_QUALITY, 1, Integer.MAX_VALUE, 1));
		qualitySpinner.setEnabled(false);
		qualitySpinner.setPreferredSize(new Dimension(100,20));
		qualitySpinner.addChangeListener(spinnerChangeListener);

		gammaSpinner = new JSpinner(new SpinnerNumberModel(FlameModel.INITIAL_GAMMA, 1, Double.MAX_VALUE, 0.1));
		gammaSpinner.setEnabled(false);
		gammaSpinner.setPreferredSize(new Dimension(100,20));
		gammaSpinner.addChangeListener(gammaChangeListener);

		blurCheckbox = new JCheckBox();
		blurCheckbox.setSelected(false);
		blurCheckbox.setEnabled(false);
		blurCheckbox.addChangeListener(blurChangeListener);

		toolBar.add(renderFlameButton);
		toolBar.add(new JLabel("Quality:"));
		toolBar.add(qualitySpinner);
		toolBar.add(new JLabel("Gamma:"));
		toolBar.add(gammaSpinner);
		toolBar.add(new JLabel("Blur:"));
		toolBar.add(blurCheckbox);
		cp.add(toolBar, BorderLayout.NORTH);

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		cp.add(progressBar, BorderLayout.SOUTH);

		flamePanel = new FlamePanel(flameModelContainer, progressBar);
		cp.add(flamePanel);

		frame.setJMenuBar(menuBar);
		frame.setSize(1024,768);

		functionFrame = new FunctionFrame(frame, flameModelContainer);
		transformFrame = new TransformFrame(frame, flameModelContainer);
		explorerFrame = new ExplorerFrame(frame, flameModelContainer);
		List<ColorProvider> colorProviderList = new ArrayList<ColorProvider>();
		for(Gradient gradient : gradientProvider.getGradientList()) {
			colorProviderList.add(new GradientColorProvider(gradient));
		}
		showGradientFrame = new GradientFrame(frame, flameModelContainer, colorProviderList);
		gradientEditorFrame = new GradientEditorFrame(frame, gradientProvider);
		createGifFrame = new CreateGifFrame(frame, flameModelContainer);
		helpFrame = new HelpFrame(frame);

		FlameModel flameModel = new FlameModel(
				"untitled.flm",
				new GradientColorProvider(gradientProvider.getGradientList().get(0)),
				Icarus.symmetryDefault);
		flameModelContainer.setFlameModel(flameModel);
	}

	/**
	 * Sets the JFrame to visible.
	 */
	public void show() {
		frame.setVisible(true);
	}
}
