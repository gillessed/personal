package net.gillessed.icarus.export;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.EngineMonitor;
import net.gillessed.icarus.engine.FractalEngine;
import net.gillessed.icarus.event.ProgressChangeEvent;
import net.gillessed.icarus.fileIO.FlameFileFilter;
import net.gillessed.icarus.fileIO.IOUtils;
import net.gillessed.icarus.geometry.ViewRectangle;
import net.gillessed.icarus.swingui.FlamePanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * This class will create a dialog asking for options
 * to export a flame to a file, then perform the render.
 * 
 * @author Gregory Cole
 */

public class FlameExporter implements EngineMonitor {
	private JFrame parent;
	private final JDialog jDialog;
	private final JButton start;
	private final JButton cancel;
	private final JTextField widthField;
	private final JTextField heightField;
	private final EngineMonitor em = this;
	private final JProgressBar progressBar;
	private FlamePanel flameToExport;
	private File fileToWriteTo;
	private BufferedImage canvas;
	private FractalEngine fe;
	
	private ActionListener exportListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			FlameModel flame = flameToExport.getFlameModel();
			if(flame.getColorProvider().getGradient() == null) {
				JOptionPane.showMessageDialog(FlameExporter.this.parent, "Flame gradient is null. "
						+ "Please pick a gradient for your flame.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(IOUtils.getDirectory()));
				String ext = "png";
				FlameFileFilter filter = new FlameFileFilter(ext, "PNG Images");
				chooser.setFileFilter(filter);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getPath();
					if(!path.contains(".png")) {
						path = path + ".png";
					}
					int pixelWidth = Integer.parseInt(widthField.getText());
					int pixelHeight = Integer.parseInt(heightField.getText());
					ViewRectangle viewRectangle = new ViewRectangle(ViewRectangle.DEFAULT_VALUE, (double) pixelHeight / (double) pixelWidth, null);
					canvas = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_INT_RGB);
					fe = new FractalEngine(em, flameToExport.getFlameModel(), pixelWidth, pixelHeight, viewRectangle, canvas);
					fileToWriteTo = new File(path);
					start.setEnabled(false);
					fe.run();
				}
			}
		}
	};
	
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			jDialog.setVisible(false);
		}
	};

	public FlameExporter(JFrame parent) {
		this.parent = parent;
		jDialog = new JDialog(parent);
		jDialog.setTitle("Export");
		jDialog.setSize(270,190);
		jDialog.setResizable(false);
		jDialog.setLocation(new Point(200,200));

		Container c = jDialog.getContentPane();
		
		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout("10dlu, pref, 10dlu, pref, 10dlu",
											"10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu");
		JPanel panel = new JPanel(layout);
		
		panel.add(new JLabel("Width:"), cc.xy(2, 2));
		widthField = new JTextField(8);
		panel.add(widthField, cc.xy(4, 2));

		panel.add(new JLabel("Height:"), cc.xy(2, 4));
		heightField = new JTextField(8);
		panel.add(heightField, cc.xy(4, 4));
		
		start = new JButton("Start");
		start.addActionListener(exportListener);
		panel.add(start, cc.xy(2, 6));
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(cancelListener);
		panel.add(cancel, cc.xy(4, 6));
		
		c.add(panel);
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		c.add(progressBar, BorderLayout.SOUTH);
	}
	public void show() {
		jDialog.setVisible(true);
	}
	public void setFlameToExport(FlamePanel flameToExport) {
		this.flameToExport = flameToExport;
	}
	public FlamePanel getFlameToExport() {
		return flameToExport;
	}
	@Override
	public void fireProgressChangeEvent(ProgressChangeEvent e) {
		if(e.isEngineDone()) {
			try
			{
				ImageIO.write(canvas, "PNG", fileToWriteTo);
			}
			catch(IOException ie) { }
			fe = null;
			start.setEnabled(true);
		} else {
			progressBar.setValue(e.getProgress());
			progressBar.setString(e.getProgress() + "% - " + fe.getThreadState());
		}
	}
	@Override
	public void setThreadState(String threadState) {
		progressBar.setString(progressBar.getValue() + "% - " + threadState);
	}
	@Override
	public FlameModel getFlameModel() {
		return flameToExport.getFlameModel();
	}
}
