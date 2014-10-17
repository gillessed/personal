package gui;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

import brain.SimpleBrain;
import football.PhilFootBall;

public class Frame {
	private JFrame frame;
	private Panel panel;
	public Frame(PhilFootBall game) {
		frame = new JFrame();
		frame.setTitle("Philosopher's Football");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Panel(game, null, new SimpleBrain(5));
		Container c = frame.getContentPane();
		c.setLayout(new GridLayout(1, 1));
		c.add(panel);
		new RepaintThread(panel).start();
		frame.setVisible(true);
		frame.pack();
		panel.start();
	}
}
