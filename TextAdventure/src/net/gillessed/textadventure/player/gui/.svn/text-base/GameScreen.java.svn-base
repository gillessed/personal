package net.gillessed.textadventure.player .gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.File;

import net.gillessed.textadventure.player.gameobjects.Game;

import com.gillessed.spacepenguin.gui.component.Div;
import com.gillessed.spacepenguin.gui.component.TextArea;
import com.gillessed.spacepenguin.gui.renderproperties.SPCardinal;
import com.gillessed.spacepenguin.panel.Screen;

public class GameScreen extends Screen {
	
	private final Game game;
	
	private final ControlPanelDiv controlPanel;
	private TextArea textPanel;
	private Div currentActionPanel;
	
	public GameScreen(Game game) {
		super("game_panel",
				"render" + File.separator + "game.prop",
				"render" + File.separator + "common.prop");
		this.game = game;
		
		controlPanel = new ControlPanelDiv();
		addChild(controlPanel);
		
		currentActionPanel = new Div("action_panel");
//		addChild(currentActionPanel);
		
		textPanel = new TextArea("text_area") {
			@Override
			protected Dimension getInnerDimension(Graphics2D g) {
				SPCardinal padding = getRenderProperties().getPadding(getState());
				int paddingx = padding.left + padding.right;
				int paddingy = padding.top + padding.bottom;
				int width = getScreen().getInner().width - controlPanel.getInner().width - paddingx;
				int height = getScreen().getInner().height - getCurrentActionPanel().getMargin().height - paddingy;
				return new Dimension(width, height);
			}
		};
		
		addChild(textPanel);
	}

	public Div getCurrentActionPanel() {
		return currentActionPanel;
	}

	public void setCurrentActionPanel(Div currentActionPanel) {
		this.currentActionPanel = currentActionPanel;
	}

	public void newText(String str) {
		// TODO fill this
	}
}
