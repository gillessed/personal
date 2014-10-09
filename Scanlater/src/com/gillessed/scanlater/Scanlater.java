package com.gillessed.scanlater;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.gillessed.scanlater.ui.ScanlaterFrame;
import com.lipstikLF.LipstikLookAndFeel;

public class Scanlater {
	public static void main(String args[]) {
		try {
            UIManager.setLookAndFeel(new LipstikLookAndFeel());
	    } catch (UnsupportedLookAndFeelException e) {
	            throw new RuntimeException(e);
	    }
		
		ScanlaterFrame frame;
		File previous = new File("previous");
		if(previous.exists()) {
			try{
				BufferedReader rd = new BufferedReader(new FileReader(previous));
				String filename = rd.readLine();
				rd.close();
				
				File saveFile = new File(filename + File.separatorChar + "translation.trf");
				ObjectInputStream os = new ObjectInputStream(new FileInputStream(saveFile));
				Project project = (Project)os.readObject();
				Point displacement = (Point)os.readObject();
				double scale = os.readDouble();
				os.close();
				
				project.loadImages();
				
				frame = new ScanlaterFrame(project, displacement, scale);
				frame.start();
			} catch (IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error loading file", JOptionPane.ERROR_MESSAGE);
			}
		} else { 
			frame = new ScanlaterFrame(null, new Point(100, 100), 1);
			frame.start();
		}
	}
}
