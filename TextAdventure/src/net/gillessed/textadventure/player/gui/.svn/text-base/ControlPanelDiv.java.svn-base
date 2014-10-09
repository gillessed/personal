package net.gillessed.textadventure.player.gui;

import com.gillessed.spacepenguin.gui.Justification;
import com.gillessed.spacepenguin.gui.component.Button;
import com.gillessed.spacepenguin.gui.component.Div;
import com.gillessed.spacepenguin.gui.component.Text;

public class ControlPanelDiv extends Div {

	public ControlPanelDiv() {
		super("control_panel");
		addClass("control_panel");
		
		Text titleLine1 = new Text("control_title1", "Text", Justification.CENTER);
		titleLine1.addClass("small_title");
		addChild(titleLine1);
		
		Text titleLine2 = new Text("control_title2", "Adventure", Justification.CENTER);
		titleLine2.addClass("small_title");
		addChild(titleLine2);
		
		Button characterButton = new Button("char_button", "Characters");
		characterButton.addClass("main_button");
		addChild(characterButton);
		
		Button partyButton = new Button("party_button", "Party");
		partyButton.addClass("main_button");
		addChild(partyButton);
		
		Button inventoryButton = new Button("inv_button", "Inventory");
		inventoryButton.addClass("main_button");
		addChild(inventoryButton);
		
		Button logButton = new Button("log_button", "Travel Log");
		logButton.addClass("main_button");
		addChild(logButton);
		
		Button saveButton = new Button("save_button", "Save");
		saveButton.addClass("main_button");
		addChild(saveButton);
		
		Button quitButton = new Button("quit_button", "Quit");
		quitButton.addClass("main_button");
		addChild(quitButton);
	}
}
