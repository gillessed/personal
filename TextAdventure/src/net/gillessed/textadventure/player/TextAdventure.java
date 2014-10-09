package net.gillessed.textadventure.player;

import javax.swing.UnsupportedLookAndFeelException;

import net.gillessed.textadventure.player.gui.MainMenuScreen;

import com.gillessed.spacepenguin.gui.Frame;

public class TextAdventure {
	public static void main(String args[]) throws UnsupportedLookAndFeelException {
		MainMenuScreen target = new MainMenuScreen();
		target.setup();
		Frame f = new Frame("Text Adventure", target, 1024, 768);
		f.start();
	}
 }
