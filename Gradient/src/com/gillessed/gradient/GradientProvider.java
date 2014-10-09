package com.gillessed.gradient;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class is a storage map for gradients that is backed by
 * a file that persists after the program is closed.
 * 
 * @author Gregory Cole
 *
 */
public class GradientProvider {
	
	private static final String DEFAULT_GRADIENT_SOURCE_FILE_NAME = "gradients.dat";
	
	private final List<Gradient> gradientList;
	private final String gradientDataFilename;
	private final boolean autoSave;
	
	public GradientProvider() {
		this(DEFAULT_GRADIENT_SOURCE_FILE_NAME, true);
	}
	
	public GradientProvider(String gradientDataFilename) {
		this(gradientDataFilename, true);
	}
	
	public GradientProvider(boolean autoSave) {
		this(DEFAULT_GRADIENT_SOURCE_FILE_NAME, autoSave);
	}
	
	public GradientProvider(String gradientDataFilename, boolean autoSave) {
		this.gradientDataFilename = gradientDataFilename;
		gradientList = new ArrayList<Gradient>();
		this.autoSave = autoSave;
		load();
	}
	
	/**
	 * Add some default hard-coded gradients to populate the list
	 * @throws IOException 
	 */
	public void addDefaultGradients() {
		SortedMap<Integer, Color> map = new TreeMap<Integer, Color>();
		map.put(200,new Color(0,200,200));
		map.put(300,new Color(204,0,150));
		gradientList.add(new Gradient(new Color(0,12,203),
				new Color(203,12,3),
				map,
				Gradient.DEFAULT_SIZE,
				"basic-1"));

		map = new TreeMap<Integer, Color>();
		map.put(300, new Color(100,200,200));
		gradientList.add(new Gradient(new Color(205,12,203),
				new Color(100,200,30),
				map,
				Gradient.DEFAULT_SIZE,
				"basic-2"));

		map = new TreeMap<Integer, Color>();
		map.put(300, new Color(200,200,50));
		map.put(350, new Color(200,100,0));
		gradientList.add(new Gradient(new Color(205,255,24),
				new Color(200,0,230),
				map,
				Gradient.DEFAULT_SIZE,
				"basic-3"));

		map = new TreeMap<Integer, Color>();
		map.put(200, new Color(45,255,200));
		map.put(350, new Color(255,45,0));
		gradientList.add(new Gradient(new Color(0,45,255),
				map,
				Gradient.DEFAULT_SIZE,
				"basic-4"));

		map = new TreeMap<Integer, Color>();
		map.put(200, new Color(0,255,0));
		map.put(350, new Color(255,0,0));
		gradientList.add(new Gradient(new Color(0,0,255),
				map,
				Gradient.DEFAULT_SIZE,
				"basic-5"));
		if(autoSave) {
			save();
		}
	}
	
	/**
	 * Read in the data from the storage file.
	 * If the file does not exist, an empty file
	 * will be created.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void load() {
		try {
			File src = new File(gradientDataFilename);
			if(!src.exists()) {
				// If the file doesn't exist, create it and add the default gradient
				SortedMap<Integer, Color> map = new TreeMap<Integer, Color>();
				gradientList.add(new Gradient(new Color(255, 255, 255),
						new Color(255, 255, 255),
						map,
						Gradient.DEFAULT_SIZE,
						"blank"));
				save();
			}
			FileInputStream fin = new FileInputStream(src);
			ObjectInputStream din = new ObjectInputStream(fin);
			int count = din.readInt();
			for(int i = 0; i < count; i++) {
				Gradient g = (Gradient)din.readObject();
				gradientList.add(g);
			}
			din.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Save the gradient map to the data file.
	 * 
	 * @throws IOException
	 */
	public void save() {
		try {
			File dest = new File(gradientDataFilename);
			FileOutputStream fout = new FileOutputStream(dest);
			ObjectOutputStream dos = new ObjectOutputStream(fout);
			dos.writeInt(gradientList.size());
			for(Gradient g : gradientList) {
				dos.writeObject(g);
			}
			dos.close();;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Gradient> getGradientList() {
		return Collections.unmodifiableList(gradientList);
	}

	public Gradient getGradient(String gradientName) {
		Gradient ret = null;
		for(Gradient g : gradientList) {
			if(g.getName().equals(gradientName)) {
				ret = g;
				break;
			}
		}
		return ret;
	}
	
	public void addGradient(Gradient g) throws IOException {
		gradientList.add(g);
		if(autoSave) {
			save();
		}
	}
	
	public void removeGradient(Gradient g) throws IOException {
		gradientList.remove(g);
		if(autoSave) {
			save();
		}
	}
}
