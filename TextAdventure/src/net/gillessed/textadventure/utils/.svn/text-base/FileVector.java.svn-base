package net.gillessed.textadventure.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

public class FileVector extends Vector<File> {
	private static final long serialVersionUID = -1330857487544924199L;
	public FileVector(String directory, String fileFilter) throws FileNotFoundException {
		File f = new File(directory);
		if(f.exists() && f.isDirectory()) {
			File[] dirContents = f.listFiles();
			for(int i = 0; i < dirContents.length; i++) {
				if(dirContents[i].getName().endsWith(fileFilter)) {
					add(dirContents[i]);
				}
			}
		} else {
			throw new FileNotFoundException("Directory " + f.getAbsolutePath() + " not found or is not a directory.");
		}
	}
}
