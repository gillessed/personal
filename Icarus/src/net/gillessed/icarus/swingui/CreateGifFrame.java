package net.gillessed.icarus.swingui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.engine.Callback;
import net.gillessed.icarus.engine.GIFRenderer;
import net.gillessed.icarus.engine.ProgressBarUpdater;
import net.gillessed.icarus.engine.ProgressUpdater;
import net.gillessed.icarus.engine.gif.GIFEngine;
import net.gillessed.icarus.fileIO.IOUtils;
import net.gillessed.icarus.geometry.Point;
import net.gillessed.icarus.geometry.TransformPath;
import net.gillessed.icarus.geometry.Triangle;
import net.gillessed.icarus.geometry.ViewRectangle;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class CreateGifFrame {
	
	private final JTextField widthField;
	private final JTextField heightField;
	private final JSpinner ticksSpinner;
	private final JSpinner frameRateSpinner;
	private final JButton selectFile;
	private final JTextField fileField;
	private final JButton start;
	private final JProgressBar progressBar;
	
	private boolean selectedPath;
	
	private FlameModel flame;
	private final List<TransformPath> transformPaths;

	private final JDialog dialog;
	private final PathEditPanel pathEditPanel;
	
	public CreateGifFrame(JFrame parent, FlameModel flameModel) {
		flame = flameModel;
		selectedPath = false;
		transformPaths = new ArrayList<TransformPath>();
		
		dialog = new JDialog(parent);
		dialog.setLocation(0, 0);
		
		Container c = dialog.getContentPane();
		c.setLayout(new FormLayout("fill:pref:grow",
				"fill:pref:grow, pref, pref"));
		
		pathEditPanel = new PathEditPanel(flame, transformPaths);
		pathEditPanel.setPreferredSize(new Dimension(512, 512));
		c.add(pathEditPanel, CC.xy(1, 1));
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FormLayout("10px, pref, 5px, pref, 20px, pref, 5px, pref, 10px",
				"5px, pref, 5px, pref, 5px, pref, 5px, pref, 5px"));
		
		widthField = new JTextField(10);
		widthField.setText("320");
		controlPanel.add(new JLabel("Width: "), CC.xy(2, 2));
		controlPanel.add(widthField, CC.xy(4, 2));
		
		heightField = new JTextField(10);
		heightField.setText("240");
		controlPanel.add(new JLabel("Height: "), CC.xy(6, 2));
		controlPanel.add(heightField, CC.xy(8, 2));
		
		ticksSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
		controlPanel.add(new JLabel("Ticks: "), CC.xy(2, 4));
		controlPanel.add(ticksSpinner, CC.xy(4, 4));
		
		frameRateSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 60, 1));
		controlPanel.add(new JLabel("Frame Rate: "), CC.xy(6, 4));
		controlPanel.add(frameRateSpinner, CC.xy(8, 4));

		JPanel filePanel = new JPanel(new FormLayout("pref, 5dlu, fill:pref:grow", "pref"));
		selectFile = new JButton("Select File: ");
		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(IOUtils.getDirectory()));
				int result = chooser.showOpenDialog(dialog);
				if(result == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getPath();
					if(!path.endsWith(".gif")) {
						path += ".gif";
					}
					fileField.setText(path);
					selectedPath = true;
				}
			}
		});
		filePanel.add(selectFile, CC.xy(1, 1));
		fileField = new JTextField("Select a file...");
		filePanel.add(fileField, CC.xy(3, 1));
		controlPanel.add(filePanel, CC.xyw(2, 6, 7));
		
		start = new JButton("Render");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedPath) {
					render();
				}
			}
		});
		controlPanel.add(start, CC.xy(2, 8));
		
		c.add(controlPanel, CC.xy(1, 2));

		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		c.add(progressBar, CC.xy(1, 3));
		
		dialog.pack();
	}

	public void show() {
		dialog.setVisible(true);
	}

	public void setFlame(FlameModel flame) {
		this.flame = flame;
	}

	private void render() {
		pathEditPanel.clearTriangleBuffer();
		int ticks = ((SpinnerNumberModel)ticksSpinner.getModel()).getNumber().intValue();
		int frameRate = ((SpinnerNumberModel)frameRateSpinner.getModel()).getNumber().intValue();
		int width = Integer.parseInt(widthField.getText());
		int height = Integer.parseInt(heightField.getText());
		List<FlameModel> flameModels = new ArrayList<>();
		flameModels.add(flame);
		for(int i = 1; i < ticks; i++) {
			FlameModel copy = flame.cloneFlame();
			List<Triangle> triangles = new ArrayList<Triangle>();
			int co = 0;
			for(Function f : copy.getFunctions()) {
				Triangle t = new Triangle(f.getAffineTransform(), pathEditPanel, TransformShowPanel.colors[co]);
				triangles.add(t);
				co++;
			}
			for(TransformPath tp : transformPaths) {
				Point displace = tp.getVectorForPercentage((double)i / (double) ticks);
				Point p = triangles.get(tp.getTriangleIndex()).getVertex(tp.getVertex());
				Point newP = new Point(p.getX() + displace.getX(), p.getY() + displace.getY());
				triangles.get(tp.getTriangleIndex()).setVertex(newP, tp.getVertex());
			}
			flameModels.add(copy);
		}
		ViewRectangle viewRectangle = new ViewRectangle(ViewRectangle.DEFAULT_VALUE, (double) height / (double) width, null);

        ProgressUpdater progressUpdater = new ProgressBarUpdater(progressBar);
		try {
			GIFRenderer.get().renderGIF(
					flameModels,
					width,
					height,
					viewRectangle,
					new File(fileField.getText()),
					frameRate,
					progressUpdater,
					null);
		} catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Error rendering image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
