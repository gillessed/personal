package com.gillessed.advancedmath.utils;

public class CharUtils {
	public static int topLevelIndexOf(String s, char ch) {
		char c;
		int level = 0;
		for(int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if(c == '(') {
				level++;
			} else if(c == ')') {
				level--;
			} else if(c == ch && level == 0) {
				return i;
			}
		}
		return -1;
	}
}
