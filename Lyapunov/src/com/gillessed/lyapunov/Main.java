package com.gillessed.lyapunov;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String args[]) throws IOException {
		Lyapunov lyapunov = new Lyapunov("ABACBBDDA", new double[]{2, 2}, new double[]{4, 4});
		lyapunov.setAntiAlias(2);
		for(int i = 1; i < 10; i++) {
			lyapunov.renderToImage(new File("lyapunov" + i + ".png"), 250, 250, new double[]{3, 3 + i * 0.01});
			System.out.println("done " + i);
		}
	}
 }
