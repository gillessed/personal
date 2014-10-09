package com.gillessed.euler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Problem18 implements Problem<Long> {
	private BufferedReader openFile() {
		try {
			return new BufferedReader(new FileReader("input" + File.separator + "18.in"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public Long evaluate() {
		List<List<Long>> triangle = new ArrayList<List<Long>>();
		BufferedReader rd = openFile();
		String line;
		try {
			while((line = rd.readLine()) != null) {
				List<Long> row = new ArrayList<Long>();
				String[] toParse = line.split(" ");
				for(int i = 0; i < toParse.length; i++) {
					row.add(Long.parseLong(toParse[i]));
				}
				triangle.add(row);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<Long> sums = new ArrayList<Long>();
		List<Long> childSums = new ArrayList<Long>();
		int row = triangle.size() - 2;
		childSums.addAll(triangle.get(row + 1));
		while(row >= 0) {
			for(int i = 0; i < triangle.get(row).size(); i++) {
				long leftSum = childSums.get(i) + triangle.get(row).get(i);
				long rightSum = childSums.get(i + 1) + triangle.get(row).get(i);
				sums.add(Math.max(leftSum, rightSum));
			}
			row--;
			childSums = sums;
			sums = new ArrayList<Long>();
		}
		return childSums.get(0);
	}
}
