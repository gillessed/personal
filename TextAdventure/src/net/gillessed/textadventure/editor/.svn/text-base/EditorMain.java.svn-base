package net.gillessed.textadventure.editor;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.gillessed.textadventure.editor.gui.EditorFrame;

import com.lipstikLF.LipstikLookAndFeel;

public class EditorMain {
	
	public static int version = 1;
	
	public static void main(String args[]) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		UIManager.setLookAndFeel(new LipstikLookAndFeel());
		
		EditorFrame e = new EditorFrame();
		e.setTitle("TextAdventure Editor");
		e.setExtendedState(JFrame.MAXIMIZED_BOTH);
		e.setSize(200, 200);
		e.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		e.validate();
		e.setVisible(true);
	}
}
