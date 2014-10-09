package net.gillessed.icarus.fileIO;


import java.io.File;

import net.gillessed.icarus.Icarus;

public class IOUtils
{
	public final static String flm = "flm";
	public static String getExtension(File f)
	{
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 &&  i < s.length() - 1)
		{
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}
	//This function retrieves the directory of the file for the filechoosers
	public static String getDirectory()
	{
		String n = Icarus.class.getProtectionDomain().getCodeSource().getLocation().toString();
		n = n.substring(n.indexOf(':') + 1,n.length());
		return n;
	}
}
