package com.gillessed.spacepenguin.gui;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.gillessed.spacepenguin.panel.Screen;

public class Frame {

	private JFrame frame;
	private Panel panel;
	private WindowListener wl;
	
	public Frame(String title, Screen renderTarget, int width, int height) {
		frame = new JFrame();
		frame.setLocation(new Point(0, 0));
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		panel = new Panel(width, height);
		renderTarget.setPanel(panel);
		panel.targetInit(renderTarget);
		frame.getContentPane().setLayout(new GridLayout(1,1));
		frame.getContentPane().add(panel);
	}
	
	public void setOnCloseAction(WindowListener wl) {
		if(this.wl != null) {
			frame.removeWindowListener(this.wl);
		}
		this.wl = wl;
		frame.addWindowListener(wl);
	}
	
	public void start() {
		frame.pack();
		frame.setVisible(true);
		panel.requestFocus();
	}
}
