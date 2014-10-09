package net.gillessed.tablemahjong;

import javax.swing.UIManager;

import net.gillessed.tablemahjong.server.logging.ConsoleAppender;
import net.gillessed.tablemahjong.server.logging.Level;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.swingui.TableMahjongFrame;

import com.lipstikLF.LipstikLookAndFeel;

public class TableMahjongMain {
	public static void main(String args[]) throws Exception {
//		UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
		UIManager.setLookAndFeel(new LipstikLookAndFeel());
		TableMahjongFrame mtf = new TableMahjongFrame();
		mtf.show();
		Logger.getLogger().setLevel(Level.DEV);
		Logger.getLogger().pushAppender(new ConsoleAppender());
	}
}
