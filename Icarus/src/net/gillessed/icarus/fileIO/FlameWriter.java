package net.gillessed.icarus.fileIO;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.gillessed.icarus.AffineTransform;
import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.variation.Variation;

public class FlameWriter {
	/**
	 * The model to write.
	 */
	private final FlameModel model;

	public FlameWriter(FlameModel model) {
		this.model = model;
	}
	
	/**
	 * This function outputs a flame model in a certain format to a file, so it can be read in and
	 * edited again later.
	 * @param dest The file we wish to write the model to.
	 * @throws IOException
	 */
	public void write(File dest) throws IOException {
		FileOutputStream fout = new FileOutputStream(dest);
		ObjectOutputStream dos = new ObjectOutputStream(fout);
		dos.writeObject(model.getColorProvider());
		dos.writeInt(model.getQuality());
		dos.writeDouble(model.getGamma());
		dos.writeInt(model.getFunctions().size());
		for(Function f : model.getFunctions()) {
			dos.writeDouble(f.getProbability());
			int temp = f.getColor();
			dos.writeInt(temp);
			for(Variation v : f.getVariations()) {
				dos.writeDouble(v.getWeight());
			}
			AffineTransform af = f.getAffineTransform();
			dos.writeDouble(af.getA());
			dos.writeDouble(af.getB());
			dos.writeDouble(af.getC());
			dos.writeDouble(af.getD());
			dos.writeDouble(af.getE());
			dos.writeDouble(af.getF());
		}
		dos.writeObject(model.getSymmetry());
		dos.flush();
		dos.close();
	}
}
