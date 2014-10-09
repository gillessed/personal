package com.gillessed.scanlater.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.gillessed.scanlater.utils.Pair;

public class ScanlaterFonts {
	public static Font normalBase;
	public static Font boldBase;
	public static Font italicBase;
	public static Font normal;
	public static Font normalVeryLarge;
	public static Font normalLarge;
	public static Font normalSmall;
	public static Font normalVerySmall;
	static {
		try {
			String normalFontFile = "fonts" + File.separatorChar + "font.ttf";
			String boldFontFile = "fonts" + File.separatorChar + "font_b.ttf";
			String italicFontFile = "fonts" + File.separatorChar + "font_i.ttf";
			normalBase = Font.createFont(Font.TRUETYPE_FONT, new File(normalFontFile));
			boldBase = Font.createFont(Font.TRUETYPE_FONT, new File(boldFontFile));
			italicBase = Font.createFont(Font.TRUETYPE_FONT, new File(italicFontFile));
			normal = normalBase.deriveFont(20f);
			normalVeryLarge = normalBase.deriveFont(48f);
			normalLarge = normalBase.deriveFont(24f);
			normalSmall = normalBase.deriveFont(16f);
			normalVerySmall = normalBase.deriveFont(12f);
		} catch (FontFormatException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static Map<Pair<Integer, Integer>, Font> fontMap = new HashMap<>();
	
	public static Font getFont(int fontSize, int fontStyle) {
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>(fontSize, fontStyle);
		Font ret;
		if(!fontMap.containsKey(pair)) {
			Font temp = null;
			switch(fontStyle) {
			case Font.PLAIN: temp = normalBase; break;
			case Font.BOLD: temp = boldBase; break;
			case Font.ITALIC: temp = italicBase; break;
			}
			ret = temp.deriveFont(Font.PLAIN, (float)fontSize);
			fontMap.put(pair, ret);
		} else {
			ret = fontMap.get(pair);
		}
		return ret;
	}
}
