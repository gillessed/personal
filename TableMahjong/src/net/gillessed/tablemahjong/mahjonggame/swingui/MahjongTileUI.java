package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MahjongTileUI {

	private static final String PIC_DIR = "images" + File.separator + "tiles-small"  + File.separator;
	private static final String PIC_FILE_EXT = ".png";
	private static Image FLIPPED_TILE;
	
	static {
		try {
			FLIPPED_TILE = ImageIO.read(new File(PIC_DIR+"flipped"+PIC_FILE_EXT));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Image tileImage;
	private int x;
	private int y;
	private int dragX;
	private int dragY;
	private boolean hovered;
	private boolean flipped;
	public MahjongTileUI(String description) {
		String[] descParts = description.split("-");
		String filename = PIC_DIR + descParts[0] + "-" + descParts[1] + PIC_FILE_EXT;
		try {
			tileImage = ImageIO.read(new File(filename));
		} catch (IOException e) {
			throw new RuntimeException("Can't read " + filename, e);
		}
		flipped = false;
		hovered = false;
		x = 0;
		y = 0;
	}

	public void draw(Graphics g, MahjongGameUI parent, MinSizeComponent container) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(x + dragX, y + dragY);
		Image imageToDraw = null;
		if(!flipped) {
			if(tileImage != null) {
				imageToDraw = tileImage;
			}
		} else {
			imageToDraw = FLIPPED_TILE;
		}
		int width = parent.getUIProperty(MahjongGameUI.DEFAULT_IMAGE_WIDTH);
		int height = parent.getUIProperty(MahjongGameUI.DEFAULT_IMAGE_HEIGHT);
		if(hovered) {
			double perc = (double)parent.getUIProperty(MahjongGameUI.HOVERED_TILE_INCREASE) / 100.0;
			width += (int)((double)width * perc);
			height += (int)((double)height * perc);
		}
		if(imageToDraw != null) {
			g2.drawImage(imageToDraw, -width / 2, -height / 2, width / 2, height / 2,
					parent.getUIProperty(MahjongGameUI.SOURCE_IMAGE_X1),
					parent.getUIProperty(MahjongGameUI.SOURCE_IMAGE_Y1),
					parent.getUIProperty(MahjongGameUI.SOURCE_IMAGE_X2),
					parent.getUIProperty(MahjongGameUI.SOURCE_IMAGE_Y2),
					container.getComponent());
		}
		g2.translate(-x - dragX, -y - dragY);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x + dragX;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y + dragY;
	}

	public void setHovered(MahjongGameUI parent, boolean hovered) {
		if(hovered != this.hovered) {
			this.hovered = hovered;
			parent.repaint();
		}
	}

	public boolean isHovered() {
		return hovered;
	}

	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public boolean contains(MahjongGameUI parent, int mx, int my) {
		int width = parent.getUIProperty(MahjongGameUI.DEFAULT_IMAGE_WIDTH);
		int height =parent.getUIProperty(MahjongGameUI.DEFAULT_IMAGE_HEIGHT);
		return (mx > x - width / 2 && mx < x + width / 2 && my > y - height / 2 && my < y + height / 2);
	}

	public void mouseMoved(MahjongGameUI parent, int x, int y) {
		setHovered(parent, contains(parent, x, y));
	}

	public void setDragX(MahjongGameUI parent, int dragX) {
		this.dragX = dragX;
		parent.repaint();
	}

	public void setDragY(MahjongGameUI parent, int dragY) {
		this.dragY = dragY;
		parent.repaint();
	}
}
