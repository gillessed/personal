package net.gillessed.textadventure.player.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import com.gillessed.spacepenguin.gui.Justification;
import com.gillessed.spacepenguin.gui.component.Button;
import com.gillessed.spacepenguin.gui.component.Text;
import com.gillessed.spacepenguin.panel.Screen;

public class MainMenuScreen extends Screen {
	
	private final Button newGameButton;
	private final Button loadSavedGameButton;
	private final Button quitButton;
	private final Text title;

	public MainMenuScreen() {
		super("Main menu", "render" + File.separator + "common.prop");

		title = new Text("title", "Text RPG", Justification.CENTER);
		title.addClass("title");
		addChild(title);
		
		newGameButton = new Button("new_game_button", "New Game");
		newGameButton.addClass("main_button");
		newGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				GameSelectionScreen selectionScreen = new GameSelectionScreen();
				switchTarget(selectionScreen);
			}
		});
		addChild(newGameButton);
		
		loadSavedGameButton = new Button("load_saved_button", "Load Saved Game");
		loadSavedGameButton.addClass("main_button");
		loadSavedGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			}
		});
		addChild(loadSavedGameButton);
		
		quitButton = new Button("quit_button", "Quit");
		quitButton.addClass("main_button");
		quitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		addChild(quitButton);
	}
	
	@Override
	public Rectangle draw(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		return super.draw(g);
	}
}
