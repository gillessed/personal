package net.gillessed.textadventure.datatype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.gillessed.textadventure.editor.EditorMain;

public class UniverseDAO {
	private int version;
	private String gameName;
	private Universe universe;
	
	public void saveUniverse(File f, Universe universe, String gameName) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
			out.writeInt(EditorMain.version);
			out.writeObject(gameName);
			out.writeObject(universe);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void readUniverse(File f) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			int version = in.readInt();
			if(version != EditorMain.version) {
				System.err.println("Version " + version + " is not compatible " +
						" with this program (Version " + EditorMain.version + ").");
				in.close();
				return;
			}
			gameName = (String)in.readObject();
			universe = (Universe)in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public String readName(File f) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			in.readInt();
			String name = (String)in.readObject();
			in.close();
			return name;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public String getGameName() {
		return gameName;
	}

	public int getVersion() {
		return version;
	}

	public Universe getUniverse() {
		return universe;
	}
}
