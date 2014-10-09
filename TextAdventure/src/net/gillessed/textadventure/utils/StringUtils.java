package net.gillessed.textadventure.utils;


public class StringUtils {
	public static String toCamelCase(String str) {
		String name = str;
		String[] words = name.split("_");
		name = "";
		for(int j = 0; j < words.length; j++) {
			words[j] = words[j].substring(0, 1).toUpperCase() + words[j].substring(1).toLowerCase();
			name += words[j];
			if(j < words.length - 1) {
				name += " ";
			}
		}
		return name;
	}
}
