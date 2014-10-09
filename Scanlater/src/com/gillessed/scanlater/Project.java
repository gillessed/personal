package com.gillessed.scanlater;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.scanlater.ui.Globals;
import com.gillessed.scanlater.ui.ScanlaterFonts;
import com.gillessed.scanlater.ui.ScanlaterImagePanel;
import com.gillessed.scanlater.utils.Pair;

public class Project implements Serializable {
	private static final long serialVersionUID = -6543659289412469370L;
	
	private List<Page> pages;
	private final String name;
	private final File directory;

	/**
	 * Constructs a new project object taking all the images
	 * from the given directory and making pages for them. 
	 */
	public Project(String directory) throws IOException {
		this(new File(directory));
	}
	
	/**
	 * Constructs a new project object taking all the images
	 * from the given directory and making pages for them. 
	 */
	public Project(File dir) throws IOException {
		this.directory = dir;
		name = dir.getName();
		if(!dir.exists()) {
			throw new IOException("Directory \"" + dir.getAbsolutePath() + "\" does not exist.");
		}
		if(!dir.isDirectory()) {
			throw new IOException("File \"" + dir.getAbsolutePath() + "\" is not a directory.");
		}
		File[] files = dir.listFiles();
		if(files.length == 0) {
			throw new IOException("Directory \"" + dir.getAbsolutePath() + "\" contains no files.");
		}
		List<File> imageFiles = new ArrayList<File>();
		for(int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			String br[] = name.split("\\.");
			String ext = "";
			if(br.length > 1) {
				ext = br[br.length - 1];
			}
			if(ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg")) {
				imageFiles.add(files[i]);
			}
		}
		if(imageFiles.isEmpty()) {
			throw new IOException("Directory \"" + dir.getAbsolutePath() + "\" contains no image files. Only png and jpg files can be used.");
		}
		pages = new ArrayList<>();
		List<Pair<Page, File>> pagesToLoad = new ArrayList<>();
		for(File file : imageFiles) {
			Page page = new Page(file.getName());
			pages.add(page);
			pagesToLoad.add(new Pair<Page, File>(page, file));
		}
		new PageLoader(pagesToLoad).start();
	}
	
	/**
	 * This will go through the directory and load the image files into the projects.
	 * Only use this function for loading images after reading the project object
	 * from a serialized file.
	 */
	public void loadImages() throws IOException {
		List<Pair<Page, File>> pagesToLoad = new ArrayList<>();
		for(Page page : pages) {
			File target = new File(directory.getAbsolutePath() + File.separatorChar + page.getFilename());
			pagesToLoad.add(new Pair<Page, File>(page, target));
		}
		new PageLoader(pagesToLoad).start();
	}
	
	public List<Page> getPages() {
		return pages;
	}
	
	public Page getPage(int index) {
		return pages.get(index);
	}

	public void draw(Graphics2D g, Dimension viewerDimensions) {
		int index = 1;
		for(Page page : pages) {
			page.draw(g, viewerDimensions);
			g.setColor(Color.white);
			g.setFont(ScanlaterFonts.normalVeryLarge);
			String toDraw = Integer.toString(index);
			g.drawString(toDraw, page.getWidth() / 2,  -50);
			
			g.translate(page.getWidth() + Globals.PAGE_MARGIN, 0);
			index++;
		}
	}
	
	public int getCurrentPage(Point2D.Double displacement, double scale) {
		return 0;
	}

	public String getName() {
		return name;
	}

	public File getDirectory() {
		return directory;
	}

	public Bubble completedSelected(List<Point> points, Point displacement, double scale) {
		int dx = 0;
		Page container = null;
		for(Page page : pages) {
			double px = (points.get(0).x - displacement.x) / scale - dx;
			double py = (points.get(0).y - displacement.y) / scale;
			if(page.contains(px, py)) {
				container = page;
				break;
			}
			dx += page.getWidth() + Globals.PAGE_MARGIN;
		}
		if(container == null) {
			return null;
		}
		return container.newBubble(points, displacement, dx, scale);
	}
	
	public int getPageForBubble(Bubble bubble) {
		int index = 0;
		for(Page page : pages) {
			if(page.containsBubble(bubble)) {
				return index; 
			}
			index++;
		}
		return -1;
	}

	public boolean isBubbleVisible(Bubble bubble, ScanlaterImagePanel scanlaterImagePanel) {
		int dx = 0;
		Page container = null;
		for(Page page : pages) {
			if(page.containsBubble(bubble)) {
				container = page;
				break;
			}
			dx += page.getWidth() + Globals.PAGE_MARGIN;
		}
		if(container == null) {
			return false;
		}
		return bubble.isVisible(scanlaterImagePanel, scanlaterImagePanel.getDisplacement(),
				scanlaterImagePanel.getScale(), dx);
	}

	public void focusOnBubble(Bubble bubble, ScanlaterImagePanel scanlaterImagePanel) {
		int dx = 0;
		Page container = null;
		for(Page page : pages) {
			if(page.containsBubble(bubble)) {
				container = page;
				break;
			}
			dx += page.getWidth() + Globals.PAGE_MARGIN;
		}
		if(container != null) {
			double scale = scanlaterImagePanel.getScale();
			Point2D.Double bubbleMidpoint = bubble.getMidPoint();
			bubbleMidpoint.x += dx;
			bubbleMidpoint.x *= scale;
			bubbleMidpoint.y *= scale;
			double x = -(bubbleMidpoint.x - scanlaterImagePanel.getWidth() / 2);
			double y = -(bubbleMidpoint.y - scanlaterImagePanel.getHeight() / 2);
			Point newDisplacement = new Point((int)x, (int)y);
			scanlaterImagePanel.setDisplacement(newDisplacement);
		}
	}
	
	public Pair<Bubble, Integer> bubbleContainsMouse(int mx, int my, Point displacement, double scale) {
		int dx = 0;
		for(Page page : pages) {
			Pair<Bubble, Integer> result = page.bubbleContainsMouse(mx, my, displacement, scale, dx);
			if(result.getSecond() != Bubble.HOVER_NO) {
				return result;
			}
			dx += page.getWidth() + Globals.PAGE_MARGIN;
		}
		return new Pair<Bubble, Integer>(null, Bubble.HOVER_NO);
	}
}
