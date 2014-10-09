package com.gillessed.scanlater.ui;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.undo.EditListener;
import com.gillessed.scanlater.ui.undo.UndoListener;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ScanlaterFrame {
	
	private final WindowAdapter clickedCloseListener = new WindowAdapter() {

		public void windowClosing(WindowEvent e) {
			quit();
		}
	};
	
	private final EditListener editListener = new EditListener() {
		@Override
		public void editPerformed() {
			Globals.instance().saveUpToDate = false;
			String title = frame.getTitle();
			if(title.charAt(title.length() - 1) != '*') {
				frame.setTitle(title + "*");
			}
		}
	};
	
	private final ActionListener newActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int status = filechooser.showOpenDialog(ScanlaterFrame.this.frame);
			if(status == JFileChooser.APPROVE_OPTION) {
				File directory = filechooser.getSelectedFile();
				File previousSave = new File(directory.getAbsolutePath() + File.separatorChar + "translation.trf");
				boolean proceed = true;
				if(previousSave.exists()) {
					int value = JOptionPane.showConfirmDialog(ScanlaterFrame.this.frame,
							"A save file already exists in that directory. Would you like to overwrite it and create a new one?",
							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(value == JOptionPane.NO_OPTION) {
						proceed = false;
					}
				}
				if(proceed) {
					try {
						Project project = new Project(directory);
						setProject(project);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(ScanlaterFrame.this.frame, e1.getMessage(),
								"Error opening directory", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	};
	
	private final ActionListener saveActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				imagePanel.save();
				if(!Globals.instance().saveUpToDate) {
					String title = frame.getTitle();
					title = title.substring(0, title.length());
				}
				Globals.instance().saveUpToDate = true;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(ScanlaterFrame.this.frame, e1.getMessage(), "Error saving project", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	private final ActionListener openActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int status = filechooser.showOpenDialog(ScanlaterFrame.this.frame);
			if(status == JFileChooser.APPROVE_OPTION) {
				try {
					File directory = filechooser.getSelectedFile();
					File saveFile = new File(directory.getAbsolutePath() + File.separatorChar + Globals.SAVE_FILE);
					if(!saveFile.exists()) {
						JOptionPane.showMessageDialog(null,
								"No save file detected in \"" + directory + "\"",
								"Error loading file", JOptionPane.ERROR_MESSAGE);
					} else {
						ObjectInputStream os = new ObjectInputStream(new FileInputStream(saveFile));
						Project project = (Project)os.readObject();
						Point displacement = (Point)os.readObject();
						double scale = os.readDouble();
						os.close();
						
						PrintWriter wr = new PrintWriter(new FileWriter(new File("previous")));
						wr.println(project.getDirectory().getAbsolutePath());
						wr.close();
						
						project.loadImages();
						
						setProject(project);
						imagePanel.setValues(displacement, scale);
					}
				} catch (IOException | ClassNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error loading file", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	};
	
	private final ActionListener exitActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			quit();
		}
	};
	
	private UndoListener undoListener = new UndoListener() {
		@Override
		public void actionPerformed(int stackSize, int index) {
			undoItem.setEnabled(index >= 0);
			redoItem.setEnabled(stackSize > 0 && index < stackSize - 1);
		}
	};
	
	private ActionListener performUndoListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.instance().getUndoStack().undo(Globals.instance().getProject());
		}
	};
	
	private ActionListener performRedoListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.instance().getUndoStack().redo(Globals.instance().getProject());
		}
	};
	
	private final JFrame frame;
	private ScanlaterImagePanel imagePanel;
	private ScanlaterDataPanel dataPanel;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
	private JMenuItem saveItem;
	
	public ScanlaterFrame(Project project, Point displacement, double scale) {
		Globals.instance().setProject(project);
		frame = new JFrame();
		frame.setTitle("Scanlater");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.addWindowListener(clickedCloseListener);
		Container c = frame.getContentPane();
		CellConstraints cc = new CellConstraints();
		c.setLayout(new FormLayout("fill:pref:grow, pref", "fill:pref:grow"));
		
		imagePanel = new ScanlaterImagePanel(project, displacement, scale);
		c.add(imagePanel, cc.xy(1, 1));
		
		dataPanel = new ScanlaterDataPanel(project);
		c.add(dataPanel, cc.xy(2, 1));
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(ScanlaterFonts.normalSmall);
		JMenuItem newItem = new JMenuItem("New");
		newItem.setFont(ScanlaterFonts.normalSmall);
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		newItem.addActionListener(newActionListener);
		fileMenu.add(newItem);
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setFont(ScanlaterFonts.normalSmall);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		openItem.addActionListener(openActionListener);
		fileMenu.add(openItem);
		saveItem = new JMenuItem("Save");
		saveItem.setEnabled(project != null);
		saveItem.setFont(ScanlaterFonts.normalSmall);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveItem.addActionListener(saveActionListener);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setFont(ScanlaterFonts.normalSmall);
		exitItem.addActionListener(exitActionListener);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setFont(ScanlaterFonts.normalSmall);
		undoItem = new JMenuItem("Undo");
		undoItem.setFont(ScanlaterFonts.normalSmall);
		undoItem.setEnabled(false);
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		undoItem.addActionListener(performUndoListener);
		editMenu.add(undoItem);
		redoItem = new JMenuItem("Redo");
		redoItem.setFont(ScanlaterFonts.normalSmall);
		redoItem.setEnabled(false);
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
		redoItem.addActionListener(performRedoListener);
		editMenu.add(redoItem);
		menuBar.add(editMenu);

		Globals.instance().getUndoStack().addUndoListener(undoListener);
		Globals.instance().getUndoStack().addEditListener(editListener);
		
		frame.setJMenuBar(menuBar);
		
		frame.pack();
	}
	
	public void start() {
		frame.setVisible(true);
	}
	
	public void setProject(Project project) {
		if(project == null) {
			frame.setTitle("Scanlater");
			saveItem.setEnabled(false);
			undoItem.setEnabled(false);
			redoItem.setEnabled(false);
		} else {
			frame.setTitle("Scanlater - " + project.getName());
			saveItem.setEnabled(true);
			undoItem.setEnabled(true);
			redoItem.setEnabled(true);
		}
		imagePanel.setProject(project);
		dataPanel.setProject(project);
		Globals.instance().setProject(project);
	}
	
	private void quit() {
		if(!Globals.instance().saveUpToDate) {
			int result = JOptionPane.showConfirmDialog(frame,
					"You have unsaved changes. Would you like to save before quitting?",
					"Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(result == JOptionPane.YES_OPTION) {
				try {
					imagePanel.save();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(ScanlaterFrame.this.frame, e1.getMessage(), "Error saving project", JOptionPane.ERROR_MESSAGE);
				}
				System.exit(0);
			} else if(result == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}
	}
}
