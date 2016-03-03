package net.gillessed.icarus.swingui.transform;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.gillessed.icarus.swingui.FlameModelContainer;

public class TransformFrame {

	private final JDialog frame;
	private final TransformEditPanel transformEditPanel;
	private final JMenuBar menuBar;
	private final JMenu randomize;
	private final JMenuItem fullyRandomize;
	private final JMenuItem normallyRandomize;
	private final FlameModelContainer flameModelContainer;

	private final ActionListener fullyRandomizeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			transformEditPanel.randomizeTriangles();
			flameModelContainer.flameModified();
			transformEditPanel.repaint();
		}
	};

	private final ActionListener normallyRandomizeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			transformEditPanel.normallyRandomizeTriangles();
            flameModelContainer.flameModified();
			transformEditPanel.repaint();
		}
	};

	public TransformFrame(JFrame parent, FlameModelContainer flameModelContainer) {
		this.flameModelContainer = flameModelContainer;
        frame = new JDialog(parent);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		menuBar = new JMenuBar();
		randomize = new JMenu("Randomize");
		fullyRandomize = new JMenuItem("Fully Randomize");
		fullyRandomize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		fullyRandomize.addActionListener(fullyRandomizeListener);
		randomize.add(fullyRandomize);
		normallyRandomize = new JMenuItem("Normally Randomize");
		normallyRandomize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		normallyRandomize.addActionListener(normallyRandomizeListener);
		randomize.add(normallyRandomize);
		menuBar.add(randomize);
		frame.setJMenuBar(menuBar);

		Container c = frame.getContentPane();
		transformEditPanel = new TransformEditPanel(flameModelContainer);
		c.add(transformEditPanel);
	}

	public void show() {
		if(!frame.isVisible()) {
			frame.setVisible(true);
		}
	}
}
