package net.gillessed.icarus.swingui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.gillessed.gradient.GradientProvider;
import com.gillessed.gradient.editor.GradientEditorPanel;

public class GradientEditorFrame {
	private JDialog dialog;

	private WindowListener closeListener = new WindowAdapter() {
	@Override
		public void windowClosed(WindowEvent e) {
			gradientProvider.save();
		}
	
		public void windowClosing(WindowEvent e) {
			gradientProvider.save();
		}
	};

	private GradientProvider gradientProvider;
	
	public GradientEditorFrame(JFrame parent, GradientProvider gradientProvider) {
		this.gradientProvider = gradientProvider;
		dialog = new JDialog(parent);
		dialog.addWindowListener(closeListener);
		Container c = dialog.getContentPane();
		c.setLayout(new GridLayout(1,1));
		GradientEditorPanel panel = new GradientEditorPanel(parent, gradientProvider);
		c.add(panel);
		dialog.pack();
	}
	public void show() {
		dialog.setVisible(true);
	}
}
