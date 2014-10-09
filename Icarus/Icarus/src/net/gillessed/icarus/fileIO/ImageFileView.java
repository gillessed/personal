package net.gillessed.icarus.fileIO;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

public class ImageFileView extends FileView
{
	ImageIcon tixIcon = new ImageIcon("resources/flameicon.png");
	public String getName(File f){return null;}
	public String getDescription(File f){return null;}
	public Boolean isTraversable(File f){return null;}
	public String getTypeDescription(File f)
	{
		String extension = IOUtils.getExtension(f);
		String type = null;
		if (extension != null)
		{
			if (extension.equals(IOUtils.flm))
			{
				type = "Flame Data";
			}
		}
		return type;
	}
	public Icon getIcon(File f)
	{
		String extension = IOUtils.getExtension(f);
		Icon icon = null;
		if (extension != null)
		{
			if(extension.equals(IOUtils.flm))
			{
				icon = tixIcon;
			}
		}
		return icon;
	}
}