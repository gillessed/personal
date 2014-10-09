package com.gillessed.scanlater;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.gillessed.scanlater.utils.Pair;

public class PageLoader extends Thread {
	private List<Pair<Page, File>> pagesToLoad;

	public PageLoader(List<Pair<Page, File>> pagesToLoad) {
		this.pagesToLoad = pagesToLoad;
	}
	
	@Override
	public void run() {
		List<Integer> errorIndices = new ArrayList<>();
		int index = 1;
		for(Pair<Page, File> pair : pagesToLoad) {
			File file = pair.getSecond();
			try {
				BufferedImage image = ImageIO.read(file);
				pair.getFirst().loadImage(image);
			} catch (IOException e) {
				errorIndices.add(index);
			}
			index++;
		}
		if(!errorIndices.isEmpty()) {
			String errorMessage = "Error loading images ";
			for(int i = 0; i < errorIndices.size() - 1; i++) {
				errorMessage += errorIndices.get(i) + ", ";
			}
			errorMessage += errorIndices.get(errorIndices.size() - 1);
			JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
