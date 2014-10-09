package net.gillessed.icarus.fileIO;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.gillessed.icarus.AffineTransform;
import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.variation.Variation;

import com.gillessed.gradient.GradientProvider;

public class FlameReader {

	/**
	 * Ths file from which to read the model.
	 */
	private final File file;
	private final GradientProvider provider;
	
	public FlameReader(File file, GradientProvider provider) {
		this.file = file;
		this.provider = provider;
	}
	
	public FlameModel read() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream dis = new ObjectInputStream(fis);
		String gradientName = (String)dis.readObject();
		FlameModel fm = new FlameModel(provider.getGradient(gradientName));
		fm.clearFunctions();
		fm.setQuality(dis.readInt());
		fm.setGamma(dis.readDouble());
		int functionSize = dis.readInt();
		for(int i = 0; i < functionSize; i++) {
			Function f = new Function(fm.getVariationDefinitions());
			f.setProbability(dis.readDouble());
			f.setColor(dis.readInt());
			for(Variation v : f.getVariations()) {
				v.setWeight(dis.readDouble());
			}
			AffineTransform af = new AffineTransform();
			af.setA(dis.readDouble());
			af.setB(dis.readDouble());
			af.setC(dis.readDouble());
			af.setD(dis.readDouble());
			af.setE(dis.readDouble());
			af.setF(dis.readDouble());
			f.setAffineTransform(af);
			fm.addFunction(f);
		}
		fm.setSymmetry((String)dis.readObject());
		dis.close();
		return fm;
	}
}
