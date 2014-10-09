package net.gillessed.icarus.fileIO;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FlameFileFilter extends FileFilter {
	private final String okFileExtension;
	private final String description;
	public FlameFileFilter(String extension, String description)
	{
		okFileExtension = extension;
		this.description = description;
	}
	public boolean accept(File file)
	{
		if(file.getName().toLowerCase().endsWith(okFileExtension))
		{
			return true;
		}
		if(file.isDirectory())
		{
			return true;
		}
		return false;
	}
	public String getDescription()
	{
		return description;
	}
}
