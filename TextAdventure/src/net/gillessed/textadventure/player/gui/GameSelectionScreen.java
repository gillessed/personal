package net.gillessed.textadventure.player.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

import net.gillessed.textadventure.datatype.UniverseDAO;
import net.gillessed.textadventure.player.gameobjects.Game;
import net.gillessed.textadventure.utils.FileVector;

import com.gillessed.spacepenguin.gui.Justification;
import com.gillessed.spacepenguin.gui.component.Button;
import com.gillessed.spacepenguin.gui.component.Table;
import com.gillessed.spacepenguin.gui.component.Text;
import com.gillessed.spacepenguin.panel.Screen;

public class GameSelectionScreen extends Screen {

	private static final String UNIVERSE_DIRECTORY = "universes";
	
	public GameSelectionScreen() {
		super("game_selection_screen",
				"render" + File.separator + "gameselection.prop",
				"render" + File.separator + "common.prop");
		
		Text title = new Text("game_selection_title", "Select A Universe", Justification.CENTER);
		title.addClass("title");
		addChild(title);
		
		try {
			final FileVector universes = new FileVector(UNIVERSE_DIRECTORY, ".tag");
			int rows = 0;
			if(universes != null) {
				rows = universes.size();
			}
			Table list = new Table("game_table", 2, rows);
			list.addClass("game_list");
			final UniverseDAO dao = new UniverseDAO();
			for(int i = 0; i < rows; i++) {
				String gameName = dao.readName(universes.get(i));
				Text text = new Text("game_label_" + (i + 1), gameName, Justification.LEFT);
				text.addClass("universe_load_label");
				list.addElement(text, 0, i);
				
				Button startButton = new Button("game_start_button_" + (i+1), "Start Game");
				startButton.addClass("start_button");
				final int index = i;
				startButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						File f = universes.get(index);
						dao.readUniverse(f);
						Game g = new Game(dao.getUniverse(), dao.getGameName());
						GameScreen gameScreen = new GameScreen(g);
						switchTarget(gameScreen);
					}
				});
				list.addElement(startButton, 1, i);
			}
			addChild(list);
		} catch (FileNotFoundException e) {
			System.err.println("Universe directory not found.");
		}
		
		Button backButton = new Button("back_button", "Back to Main Menu");
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				switchTarget(new MainMenuScreen());
			}
		});
		addChild(backButton);
	}
}
