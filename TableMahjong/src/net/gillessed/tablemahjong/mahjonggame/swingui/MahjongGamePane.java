package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.JPanel;

import net.gillessed.tablemahjong.client.GameRoom;
import net.gillessed.tablemahjong.mahjonggame.swingui.MahjongGameUI.RepaintListener;

@SuppressWarnings("serial")
public class MahjongGamePane extends JPanel implements MouseListener, MouseMotionListener {
	
	private static final File MAHJONG_UI_PROPERTIES_FILE = 
		new File("resources" + File.separator + "gameui.properties");
	
	private static final File MAHJONG_FONT_PROPERTIES_FILE = 
			new File("resources" + File.separator + "gamefonts.properties");
	
	private final MahjongGameUI mahjongGameUI;
	private final RepaintQueue repaintQueue;
	
	public MahjongGamePane(GameRoom gameRoom, boolean isObserver, String selfUsername) {
		MinSizeComponent comp = new MinSizeComponent(this);
		mahjongGameUI = new MahjongGameUI(comp, gameRoom, isObserver, selfUsername, MAHJONG_UI_PROPERTIES_FILE, MAHJONG_FONT_PROPERTIES_FILE);
		comp.setMahjongGameUI(mahjongGameUI);
		repaintQueue = new RepaintQueue(this);
		mahjongGameUI.setRepaintListener(new RepaintListener() {
			@Override
			public void repaint() {
				repaintQueue.addRepaintRequest();
			}
		});
		if(!mahjongGameUI.isObserver()) {
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		repaintQueue.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(25,150,25));
		g.fillRect(0,0,getWidth(),getHeight());
		getMahjongGameUI().draw(g);
	}

	public MahjongGameUI getMahjongGameUI() {
		return mahjongGameUI;
	}
	
	public void loadUIProperties() {
		try {
			mahjongGameUI.loadUIProperties();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mahjongGameUI.mouseClicked(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mahjongGameUI.mouseReleased(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mahjongGameUI.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mahjongGameUI.mouseMoved(e);
	}
}
