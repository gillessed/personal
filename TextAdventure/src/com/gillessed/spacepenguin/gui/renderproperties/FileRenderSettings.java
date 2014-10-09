package com.gillessed.spacepenguin.gui.renderproperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.spacepenguin.gui.RenderTarget.RenderState;

public class FileRenderSettings extends RenderSettings {
	private List<String> typeNames;
	private List<String> classNames;
	private List<String> idNames;
	private RenderState state = null;
	private BufferedReader br;
	public FileRenderSettings(String filename) throws IOException {
		br = new BufferedReader(new FileReader(filename));
		String input = br.readLine();
		typeNames = new ArrayList<String>();
		classNames = new ArrayList<String>();
		idNames = new ArrayList<String>();
		while(input != null) {
			if(input.trim().equals("")) {
			} else if(input.startsWith("\t")) {
				input = input.trim();
				String[] sp = input.split(" ");
				String propName = sp[0].trim();
				RenderProperties.Props property = null;
				for(RenderProperties.Props p : RenderProperties.Props.values()) {
					if(p.toString().equalsIgnoreCase(propName)) {
						property = p;
						break;
					}
				}
				if(property == null) {
					throw new RuntimeException("Property name \"" + propName + "\" not found.");
				}
				String[] propdesc = new String[sp.length - 1];
				for(int i = 1; i < sp.length; i++) {
					propdesc[i-1] = sp[i];
				}
				RenderRule rule = new RenderRule(property, property.parseValue(propdesc));
				rule.typeNames.addAll(typeNames);
				rule.classNames.addAll(classNames);
				rule.idNames.addAll(idNames);
				rule.state = state;
				if(rule.state == null) {
					rule.state = RenderState.NONE;
				}
				addRenderRule(rule);
			} else if (!input.startsWith("~")){
				state = null;
				typeNames.clear();
				classNames.clear();
				idNames.clear();
				String temp = "";
				int flag = -1;
				for(int i = 0; i < input.length(); i++) {
					char ch = input.charAt(i);
					if(ch == '@' || ch == '#' || ch == '$' || ch == '%') {
						if(flag != -1 && !temp.equals("")) {
							addToList(temp, flag);
							temp = "";
						}
						if(ch == '@') flag = 0;
						else if(ch == '#') flag = 1;
						else if(ch == '$') flag = 2;
						else if(ch == '%') flag = 3;
					} else{
						temp += ch;
					}
				}
				if(flag != -1 && !temp.equals("")) {
					addToList(temp, flag);
					temp = "";
				}
			}
			input = br.readLine();
		}
		br.close();
	}
	
	private void addToList(String str, int i) {
		switch(i) {
		case 0:
			typeNames.add(str);
			break;
		case 1:
			classNames.add(str);
			break;
		case 2:
			idNames.add(str);
			break;
		case 3:
			if(state == null) {
				for(RenderState s : RenderState.values()) {
					if(s.toString().equalsIgnoreCase(str)) {
						state = s;
						break;
					}
				}
			} else {
				System.err.println("You cannot have more than one state for a set of rules.");
			}
		}
	}
}
