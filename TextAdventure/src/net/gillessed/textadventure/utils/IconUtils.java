package net.gillessed.textadventure.utils;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconUtils {
	
	public static final String ICON_PATH = "resources" + File.separator + "icons" + File.separator;
	
	public static final Icon ADD_ICON_16 = new ImageIcon(ICON_PATH + "add16.png");
	
	public static final Icon CHARACTERS_ICON_16 = new ImageIcon(ICON_PATH + "characters16.png");
	public static final Icon CHARACTERS_ICON_128 = new ImageIcon(ICON_PATH + "characters128.png");
	public static Icon CHARACTERS_ICON(int s) {
		if(s == 16) return CHARACTERS_ICON_16;
		else if(s == 128) return CHARACTERS_ICON_128;
		else return null;
	}
	
	public static final Icon DELETE_ICON_16 = new ImageIcon(ICON_PATH + "delete16.png");
	
	public static final Icon ENEMIES_ICON_16 = new ImageIcon(ICON_PATH + "enemies16.png");
	public static final Icon ENEMIES_ICON_128 = new ImageIcon(ICON_PATH + "enemies128.png");
	public static Icon ENEMIES_ICON(int s) {
		if(s == 16) return ENEMIES_ICON_16;
		else if(s == 128) return ENEMIES_ICON_128;
		else return null;
	}
	
	public static final Icon ENEMY_AREA_ICON_16 = new ImageIcon(ICON_PATH + "enemyarea16.png");
	public static final Icon ENEMY_AREA_ICON_32 = new ImageIcon(ICON_PATH + "enemyarea32.png");
	public static final Icon ENEMY_AREA_ICON_128 = new ImageIcon(ICON_PATH + "enemyarea128.png");
	public static Icon ENEMY_AREA_ICON(int s) {
		if(s == 16) return ENEMY_AREA_ICON_16;
		else if(s == 32) return ENEMY_AREA_ICON_32;
		else if(s == 128) return ENEMY_AREA_ICON_128;
		else return null;
	}
	
	public static final Icon EVENTS_ICON_16 = new ImageIcon(ICON_PATH + "events16.png");
	public static final Icon EVENTS_ICON_128 = new ImageIcon(ICON_PATH + "events128.png");
	public static Icon EVENTS_ICON(int s) {
		if(s == 16) return EVENTS_ICON_16;
		else if(s == 128) return EVENTS_ICON_128;
		else return null;
	}
	
	public static final Icon FOLDER_ICON_16 = new ImageIcon(ICON_PATH + "folder16.png");
	
	public static final Icon FRIENDLY_AREA_ICON_16 = new ImageIcon(ICON_PATH + "friendlyareas16.png");
	public static final Icon FRIENDLY_AREA_ICON_32 = new ImageIcon(ICON_PATH + "friendlyareas32.png");
	public static final Icon FRIENDLY_AREAS_ICON_128 = new ImageIcon(ICON_PATH + "friendlyareas128.png");
	public static Icon FRIENDLY_AREAS_ICON(int s) {
		if(s == 16) return FRIENDLY_AREA_ICON_16;
		else if(s == 32) return FRIENDLY_AREA_ICON_32;
		else if(s == 128) return FRIENDLY_AREAS_ICON_128;
		else return null;
	}
	
	public static final Icon INTERACTIONS_ICON_16 = new ImageIcon(ICON_PATH + "interactions16.png");
	public static final Icon INTERACTIONS_ICON_128 = new ImageIcon(ICON_PATH + "interactions128.png");
	public static Icon INTERACTIONS_ICON(int s) {
		if(s == 16) return INTERACTIONS_ICON_16;
		else if(s == 128) return INTERACTIONS_ICON_128;
		else return null;
	}
	
	public static final Icon ITEM_ICON_16 = new ImageIcon(ICON_PATH + "items16.png");
	public static final Icon ITEM_ICON_128 = new ImageIcon(ICON_PATH + "items128.png");
	public static Icon ITEM_ICON(int s) {
		if(s == 16) return ITEM_ICON_16;
		else if(s == 128) return ITEM_ICON_128;
		else return null;
	}
	
	public static final Icon NEW_ICON_16 = new ImageIcon(ICON_PATH + "new16.png");
	public static final Icon OPEN_ICON_16 = new ImageIcon(ICON_PATH + "open16.png");
	public static final Icon QUIT_ICON_16 = new ImageIcon(ICON_PATH + "quit16.png");
	public static final Icon SAVE_ICON_16 = new ImageIcon(ICON_PATH + "save16.png");
	public static final Icon SAVEAS_ICON_16 = new ImageIcon(ICON_PATH + "saveas16.png");
	public static final Icon SAVEDATATYPE_ICON_16 = new ImageIcon(ICON_PATH + "savedatatype16.png");
	
	public static final Icon SHOP_ICON_16 = new ImageIcon(ICON_PATH + "shop16.png");
	public static final Icon SHOP_ICON_128 = new ImageIcon(ICON_PATH + "shop128.png");
	public static Icon SHOP_ICON(int s) {
		if(s == 16) return SHOP_ICON_16;
		else if(s == 128) return SHOP_ICON_128;
		else return null;
	}
	
	public static final Icon SKILLS_ICON_16 = new ImageIcon(ICON_PATH + "skills16.png");
	public static final Icon SKILLS_ICON_128 = new ImageIcon(ICON_PATH + "skills128.png");
	public static Icon SKILLS_ICON(int s) {
		if(s == 16) return SKILLS_ICON_16;
		else if(s == 128) return SKILLS_ICON_128;
		else return null;
	}
	
	public static final Icon TOGGLE_CELL_ICON_32 = new ImageIcon(ICON_PATH + "togglecell32.png");
	
	public static final Icon UNIVERSE_ICON_16 = new ImageIcon(ICON_PATH + "universe16.png");
	public static final Icon UNIVERSE_ICON_128 = new ImageIcon(ICON_PATH + "universe128.png");
	public static Icon UNIVERSE_ICON(int s) {
		if(s == 16) return UNIVERSE_ICON_16;
		else if(s == 128) return UNIVERSE_ICON_128;
		else return null;
	}
	
	public static final Icon VARIABLE_ICON_16 = new ImageIcon(ICON_PATH + "variable16.png");
	public static final Icon VARIABLE_ICON_128 = new ImageIcon(ICON_PATH + "variable128.png");
	public static Icon VARIABLE_ICON(int s) {
		if(s == 16) return VARIABLE_ICON_16;
		else if(s == 128) return VARIABLE_ICON_128;
		else return null;
	}
	
	public static final Icon WORLDS_ICON_16 = new ImageIcon(ICON_PATH + "worlds16.png");
	public static final Icon WORLDS_ICON_128 = new ImageIcon(ICON_PATH + "worlds128.png");
	public static Icon WORLDS_ICON(int s) {
		if(s == 16) return WORLDS_ICON_16;
		else if(s == 128) return WORLDS_ICON_128;
		else return null;
	}
}
