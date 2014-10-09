package com.gillessed.euler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem22 implements Problem<Long> {
	private BufferedReader openFile() {
		try {
			return new BufferedReader(new FileReader("input" + File.separator + "22.in"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public Long evaluate() {
		BufferedReader rd = openFile();
		String line;
		try {
			line = rd.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String nameBlocks[] = line.split(",");
		List<String> names = new ArrayList<String>();
		for(int i = 0; i < nameBlocks.length; i++) {
			names.add(nameBlocks[i].substring(1, nameBlocks[i].length() - 1));
		}
		Collections.sort(names);
		long scoreTotal = 0L;
		for(int i = 0; i < names.size(); i++) {
			int letters = 0;
			String name = names.get(i);
			for(int j = 0; j < name.length(); j++) {
				letters += (int)(name.charAt(j)) - 64;
			}
			long score = (long)((i + 1) * letters);
			scoreTotal += score;
		}
		return scoreTotal;
	}
}
