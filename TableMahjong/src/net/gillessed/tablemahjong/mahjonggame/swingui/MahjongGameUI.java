package net.gillessed.tablemahjong.mahjonggame.swingui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.gillessed.tablemahjong.client.GameRoom;
import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateEvent.UpdateType;
import net.gillessed.tablemahjong.client.event.UpdateListener;
import net.gillessed.tablemahjong.mahjonggame.MahjongGame;
import net.gillessed.tablemahjong.mahjonggame.MahjongGame.Wind;
import net.gillessed.tablemahjong.mahjonggame.MahjongHand;
import net.gillessed.tablemahjong.mahjonggame.MahjongPlayer;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.UpdateTarget;
import net.gillessed.tablemahjong.server.logging.Logger;

public class MahjongGameUI extends AbstractMahjongUIComponent<GameRoom> {
	
	//List of constants used to access UI properties
	public static final String HAND_OFFSET = "hand-offset";
	public static final String PICKUP_OVERLAY = "pickup-overlay";
	public static final String DEFAULT_IMAGE_WIDTH = "default-image-width";
	public static final String DEFAULT_IMAGE_HEIGHT = "default-image-height";
	public static final String SOURCE_IMAGE_X1 = "source-image-x1";
	public static final String SOURCE_IMAGE_Y1 = "source-image-y1";
	public static final String SOURCE_IMAGE_X2 = "source-image-x2";
	public static final String SOURCE_IMAGE_Y2 = "source-image-y2";
	public static final String MIN_SIZE = "min-size";
	public static final String HOVERED_TILE_INCREASE = "hovered-tile-increase";
	public static final String CENTER_SQUARE_SIZE = "center-square-size";
	public static final String CENTER_SQUARE_INSET = "center-square-inset";
	public static final String TILE_ZONE_INSET = "tile-zone-inset"; 
	public static final String TILE_ZONE_THICKNESS = "tile-zone-thickness";
	public static final String TILE_ZONE_PADDING = "tile-zone-padding";
	
	public static final Map<String, Font> fontMap = new HashMap<String, Font>();
	public static final String PLAYER_DETAILS_FONT = "player-details-font";
	public static final String ROUND_UI_FONT = "round-ui-font";
	
	public interface RepaintListener {
		public void repaint();
	}
	
	private RepaintListener repaintListener; 
	
	private final UpdateTarget userInterfaceListener = new UpdateTarget() {
		@Override
		public void update(MahjongUpdate mahjongUpdate) {
			repaint();
		}
	};
	
	private final Map<Wind, MahjongTileUI> windIndicators;
	private final Map<Wind, MahjongHandUI> mahjongHandUIs;
	
	private final boolean isObserver;
	private final String selfUsername;
	private final Properties properties;
	private final File gameuiProperties;
	private final File gamefontProperties;
	
	private boolean controlsFrozen = false;
	
	public MahjongGameUI(MinSizeComponent container, GameRoom gameRoom, boolean isObserver, String selfUsername, File gameuiProperties, File gamefontProperties) {
		super(container, null, gameRoom);
		gameRoom.addUpdateListener(new UpdateListener() {
			@Override
			public void actionPerformed(UpdateEvent e) {
				if(e.getType() == UpdateType.NEWROUND) {
					updateUIObjects();
					repaint();
				}
			}
		});
		this.mahjongHandUIs = new HashMap<MahjongGame.Wind, MahjongHandUI>();
		this.gameuiProperties = gameuiProperties;
		this.gamefontProperties = gamefontProperties;
		properties = new Properties();
		try {
			loadUIProperties();
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
		this.isObserver = isObserver;
		this.selfUsername = selfUsername;
		windIndicators = new HashMap<Wind, MahjongTileUI>();
		windIndicators.put(Wind.East, UIResources.getMahjongTileUI("wind-east-1"));
		windIndicators.put(Wind.North, UIResources.getMahjongTileUI("wind-north-1"));
		windIndicators.put(Wind.West, UIResources.getMahjongTileUI("wind-west-1"));
		windIndicators.put(Wind.South, UIResources.getMahjongTileUI("wind-south-1"));
	}
	
	private synchronized void updateUIObjects() {
		mahjongHandUIs.clear();
		for(MahjongHand hand : getModel().getRound().getHands().values()) {
			mahjongHandUIs.put(hand.getWind(), new MahjongHandUI(getContainer(), this, hand));
		}
	}
	
	public synchronized void draw(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int sqSize = getParent().getUIProperty(MahjongGameUI.CENTER_SQUARE_SIZE);
		int sqInset = getParent().getUIProperty(MahjongGameUI.CENTER_SQUARE_INSET);
		
		int width = getContainer().getWidth();
		int height = getContainer().getHeight();
		
		g.setColor(new Color(50, 50, 50));
		g.fillRect((width - sqSize) / 2 + sqInset, (height - sqSize) / 2 + sqInset, sqSize - 2 * sqInset, sqSize - 2 * sqInset);

		if(getModel().getRound() != null) {
			g.setColor(Color.white);
			g.setFont(getParent().getUIFont(MahjongGameUI.ROUND_UI_FONT));
			g.drawString("Current Player:", width / 2 - 70, height / 2 - 50);
			g.drawString("Prevailing Wind:", width / 2 - 70, height / 2 + 50);

			Wind windPerspective = getModel().getRound().getCurrentWind();
			if(windPerspective != null) {
				MahjongTileUI windUI = getParent().getWindIndicators().get(windPerspective);
				windUI.setX(width / 2 + 50);
				windUI.setY(height / 2 - 50);
				windUI.draw(g, getParent(), getContainer());
			}
			Wind prevailingWind = getModel().getRound().getPrevailingWind();
			if(prevailingWind != null) {
				MahjongTileUI windUI = getParent().getWindIndicators().get(prevailingWind);
				windUI.setX(width / 2 + 50);
				windUI.setY(height / 2 + 50);
				windUI.draw(g, getParent(), getContainer());
			}
			
			for(MahjongPlayer mp : getModel().getRound().getPlayers()) {
				drawPlayer(mp, g);
			}
			
			for(MahjongHandUI mhui : mahjongHandUIs.values()) {
				mhui.draw(g);
			}
		}
	}
	
	private void drawPlayer(MahjongPlayer mp, Graphics g) {

		Graphics2D g2 = (Graphics2D)g;
		g2.setFont(getParent().getUIFont(MahjongGameUI.PLAYER_DETAILS_FONT));
		
		int orientation;
		Wind wind = mp.getAssociatedHand();
		if(getParent().isObserver()) {
			// South should be at the bottom of the screen if observer
			orientation = Wind.South.getDistanceTo(wind);
		} else {
			// If not observer, player's own hand should be at the bottom of the screen
			orientation = getParent().getSelfWind().getDistanceTo(wind);
		}
		
		int width = getContainer().getWidth();
		int height = getContainer().getHeight();
		int size = getParent().getUIProperty(MahjongGameUI.CENTER_SQUARE_SIZE);
		int inset = getParent().getUIProperty(MahjongGameUI.CENTER_SQUARE_INSET);
		int tileWidth = getParent().getUIProperty(MahjongGameUI.DEFAULT_IMAGE_WIDTH);
		
		int x = 0, y = 0;
		
		switch(orientation) {
		case 0:
			x = size / 2;
			y = size - inset;
			break;
		case 1:
			x = inset;
			y = size / 2;
			break;
		case 2:
			x = size / 2;
			y = inset;
			break;
		case 3:
			x = size - inset;
			y = size / 2;
			break;
		}
		String stringToDraw = mp.getName() + ": " + mp.getMoney();
		FontMetrics fm = g2.getFontMetrics(getParent().getUIFont(MahjongGameUI.PLAYER_DETAILS_FONT));
		int fontHeight = fm.getAscent() + fm.getDescent();
		int rectPadding = inset - (fontHeight) / 2;
		int stringLength = fm.stringWidth(stringToDraw);

		double rotationAngle = (orientation) * Math.PI * 0.5;
		int transX = (width - size) / 2 + x;
		int transY =  (height - size) / 2 + y;
		g2.translate(transX, transY);
		g2.rotate(rotationAngle);
		
		MahjongTileUI windIndicator = getParent().getParent().getWindIndicators().get(mp.getAssociatedHand());
		windIndicator.setX(-stringLength / 2 - rectPadding - tileWidth / 2 - 5);
		windIndicator.setY(0);
		windIndicator.draw(g, getParent(), getContainer());
		
		g2.setColor(new Color(200,30,30));
		g2.fillRoundRect(-stringLength / 2 - rectPadding, -inset, stringLength + 2 * rectPadding, 2 * inset, rectPadding, rectPadding);
		
		g2.setColor(new Color(0,0,0));
		g2.drawString(stringToDraw, -stringLength / 2, fontHeight / 2);
		
		g2.rotate(-rotationAngle);
		g2.translate(-transX, -transY);
	}
	
	public Map<Wind, MahjongTileUI> getWindIndicators() {
		return Collections.unmodifiableMap(windIndicators);
	}

	public boolean isObserver() {
		return isObserver;
	}
	
	public UpdateTarget getUserInterfaceListener() {
		return userInterfaceListener;
	}

	public String getSelfUsername() {
		return selfUsername;
	}
	
	public Wind getSelfWind() {
		if(isObserver()){
			return null;
		}
		if(getModel().getRound() != null) {
			Set<MahjongPlayer> players = getModel().getRound().getPlayers();
			for(MahjongPlayer player : players) {
				if(player.getName().equals(selfUsername)){
					return player.getAssociatedHand();
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	public void repaint() {
		repaintListener.repaint();
	}

	public void setRepaintListener(RepaintListener repaintListener) {
		this.repaintListener = repaintListener;
	}

	public RepaintListener getRepaintListener() {
		return repaintListener;
	}
	
	public void loadUIProperties() throws Exception{
		synchronized(properties) {
			Logger.getLogger().dev("CLIENT - Loading " + gameuiProperties.getCanonicalPath());
			properties.load(new FileInputStream(gameuiProperties));
		}
		synchronized(fontMap) {
			Logger.getLogger().dev("CLIENT - Loading " + gamefontProperties.getCanonicalPath());
			BufferedReader reader = new BufferedReader(new FileReader(gamefontProperties));
			fontMap.clear();
			String inLine = reader.readLine();
			while(!(inLine == null)) {
				Logger.getLogger().dev("CLIENT - " + inLine);
				String[] splitLine = inLine.split(" ");
				int type;
				if("plain".equals(splitLine[2])) {
					type = Font.PLAIN;
				} else if("bold".equals(splitLine[2])) {
					type = Font.BOLD;
				} else {
					type = Font.PLAIN;
				}
				Font newFont = new Font(splitLine[1], type, Integer.parseInt(splitLine[3]));
				fontMap.put(splitLine[0], newFont);
				inLine = reader.readLine();
			}
			reader.close();
		}
		if(repaintListener != null) {
			repaintListener.repaint();
		}
	}
	
	public int getUIProperty(String key) {
		synchronized(properties) {
			return Integer.parseInt(properties.getProperty(key, "0"));
		}
	}
	
	public Font getUIFont(String key) {
		synchronized(fontMap) {
			return fontMap.get(key);
		}
	}
	
	@Override
	public MahjongGameUI getParent() {
		return this;
	}

	public boolean isControlsFrozen() {
		return controlsFrozen;
	}

	public void setControlsFrozen(boolean controlsFrozen) {
		this.controlsFrozen = controlsFrozen;
		if(!controlsFrozen && getSelfWind() != null) {
			mahjongHandUIs.get(getSelfWind()).mouseReleased();
		}
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		if(!isControlsFrozen() && getSelfWind() != null) {
			mahjongHandUIs.get(getSelfWind()).mouseClicked(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(!isControlsFrozen() && getSelfWind() != null) {
			mahjongHandUIs.get(getSelfWind()).mouseDragged(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(!isControlsFrozen() && getSelfWind() != null) {
			mahjongHandUIs.get(getSelfWind()).mouseReleased(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		if(!isControlsFrozen() && getSelfWind() != null) {
			mahjongHandUIs.get(getSelfWind()).mouseMoved(e);
		}
	}
}
