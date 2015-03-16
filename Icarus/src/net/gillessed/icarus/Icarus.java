package net.gillessed.icarus;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.gillessed.icarus.geometry.CircleInversion;
import net.gillessed.icarus.geometry.CompoundTransformation;
import net.gillessed.icarus.geometry.IdentityTransformation;
import net.gillessed.icarus.geometry.Point;
import net.gillessed.icarus.geometry.Rotation;
import net.gillessed.icarus.geometry.Symmetry;
import net.gillessed.icarus.geometry.Transformation;
import net.gillessed.icarus.geometry.Translation;
import net.gillessed.icarus.swingui.IcarusFrame;
import net.gillessed.icarus.variation.Bent;
import net.gillessed.icarus.variation.Diamond;
import net.gillessed.icarus.variation.Disc;
import net.gillessed.icarus.variation.Ex;
import net.gillessed.icarus.variation.Exponential;
import net.gillessed.icarus.variation.Fisheye;
import net.gillessed.icarus.variation.Greg1;
import net.gillessed.icarus.variation.Greg2;
import net.gillessed.icarus.variation.Greg3;
import net.gillessed.icarus.variation.Handkerchief;
import net.gillessed.icarus.variation.Heart;
import net.gillessed.icarus.variation.Horseshoe;
import net.gillessed.icarus.variation.Hyperbolic;
import net.gillessed.icarus.variation.Julia;
import net.gillessed.icarus.variation.Linear;
import net.gillessed.icarus.variation.Polar;
import net.gillessed.icarus.variation.Sinusoidal;
import net.gillessed.icarus.variation.Spherical;
import net.gillessed.icarus.variation.Spiral;
import net.gillessed.icarus.variation.Swirl;
import net.gillessed.icarus.variation.Variation;

import com.lipstikLF.LipstikLookAndFeel;
/**
 * Driver for the program. It runs the GUI methods and initializes the whole thing.
 */
public class Icarus {
	
	public static Map<String, Symmetry> symmetryMap;
	public static String symmetryDefault = "None";
	private static List<Variation> variationDefinitions;
	
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new LipstikLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		variationDefinitions = new ArrayList<Variation>();
		variationDefinitions.add(new Linear());
		variationDefinitions.add(new Sinusoidal());
		variationDefinitions.add(new Spherical());
		variationDefinitions.add(new Swirl());
		variationDefinitions.add(new Horseshoe());
		variationDefinitions.add(new Polar());
		variationDefinitions.add(new Handkerchief());
		variationDefinitions.add(new Heart());
		variationDefinitions.add(new Disc());
		variationDefinitions.add(new Spiral());
		variationDefinitions.add(new Hyperbolic());
		variationDefinitions.add(new Diamond());
		variationDefinitions.add(new Ex());
		variationDefinitions.add(new Julia());
		variationDefinitions.add(new Bent());
		variationDefinitions.add(new Fisheye());
		variationDefinitions.add(new Exponential());
		variationDefinitions.add(new Greg1());
		variationDefinitions.add(new Greg2());
		variationDefinitions.add(new Greg3());
		
		symmetryMap = new TreeMap<String, Symmetry>();
		createSymmetry("None",
				new IdentityTransformation());
		
		createSymmetry("Translation",
				new IdentityTransformation(),
				new Translation(0.2, 0),
				new Translation(0.4, 0),
				new Translation(0.6, 0),
				new Translation(-0.2, 0),
				new Translation(-0.4, 0),
				new Translation(-0.6, 0));
		
		createSymmetry("Motion",
				new IdentityTransformation(),
				new Rotation(Math.PI/17, false),
				new Rotation(2*Math.PI/17, false),
				new Rotation(3*Math.PI/17, false),
				new Rotation(4*Math.PI/17, false),
				new Rotation(5*Math.PI/17, false));
		
		createSymmetry("Slow Motion",
				new IdentityTransformation(),
				new Rotation(Math.PI/61, false),
				new Rotation(2*Math.PI/61, false),
				new Rotation(3*Math.PI/61, false),
				new Rotation(4*Math.PI/61, false),
				new Rotation(5*Math.PI/61, false),
				new Rotation(6*Math.PI/61, false),
				new Rotation(7*Math.PI/61, false),
				new Rotation(8*Math.PI/61, false),
				new Rotation(9*Math.PI/61, false),
				new Rotation(10*Math.PI/61, false),
				new Rotation(11*Math.PI/61, false),
				new Rotation(12*Math.PI/61, false),
				new Rotation(13*Math.PI/61, false),
				new Rotation(14*Math.PI/61, false),
				new Rotation(15*Math.PI/61, false),
				new Rotation(16*Math.PI/61, false),
				new Rotation(17*Math.PI/61, false),
				new Rotation(18*Math.PI/61, false),
				new Rotation(19*Math.PI/61, false),
				new Rotation(20*Math.PI/61, false));
		
		createSymmetry("Full Wheel",
				new IdentityTransformation(),
				new Rotation(Math.PI/5, false),
				new Rotation(2*Math.PI/5, false),
				new Rotation(3*Math.PI/5, false),
				new Rotation(4*Math.PI/5, false),
				new Rotation(5*Math.PI/5, false),
				new Rotation(6*Math.PI/5, false),
				new Rotation(7*Math.PI/5, false),
				new Rotation(8*Math.PI/5, false),
				new Rotation(9*Math.PI/5, false));

		createSymmetry("Plus",
				new IdentityTransformation(),
				new Rotation(Math.PI, false),
				new Rotation(0, true),
				new Rotation(Math.PI, true));

		createSymmetry("Ex",
				new IdentityTransformation(),
				new Rotation(Math.PI, false),
				new Rotation(1, true),
				new Rotation(Math.PI + 1, true));

		createSymmetry("Trigon",
				new IdentityTransformation(),
				new Rotation(2*Math.PI/3, false),
				new Rotation(4*Math.PI/3, false));

		createSymmetry("Slow Full Wheel",
				new IdentityTransformation(),
				new Rotation(2*Math.PI/5, false),
				new Rotation(4*Math.PI/5, false),
				new Rotation(6*Math.PI/5, false),
				new Rotation(8*Math.PI/5, false));
		
		createSymmetry("Half Wheel",
				new IdentityTransformation(),
				new Rotation(Math.PI/5, false),
				new Rotation(2*Math.PI/5, false),
				new Rotation(3*Math.PI/5, false),
				new Rotation(4*Math.PI/5, false),
				new Rotation(5*Math.PI/5, false));
		
		createSymmetry("Pound",
				new Translation(-0.1, 0),
				new CompoundTransformation(
						new Translation(0.1, 0.0),
						new Rotation(0, true)),
				new CompoundTransformation(
						new Rotation(Math.PI / 2, false),
						new Translation(0, 0.1)),
				new CompoundTransformation(
						new Rotation(Math.PI / 2, true),
						new Translation(0, -0.1)));
		
		createSymmetry("Inversion on a Half-Unit Circle",
				new CircleInversion(new Point(0,0), 0.5));
		
		createSymmetry("Circle Inversions 1",
				new IdentityTransformation(),
				new CircleInversion(new Point(0,0), 0.5));
		
		createSymmetry("Circle Inversions 2",
				new CircleInversion(new Point(0.5,0.5), 0.5),
				new CircleInversion(new Point(0.5,-0.5), 0.5),
				new CircleInversion(new Point(-0.5,-0.5), 0.5),
				new CircleInversion(new Point(-0.5,0.5), 0.5));
		
		createSymmetry("Circle Inversions 3",
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)));
		
		createSymmetry("Circle Inversions 4",
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new Rotation(2*Math.PI/3, false),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)),
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new Rotation(4*Math.PI/3, false),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)),
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)));
		
		createSymmetry("Circle Inversions 5",
				new CircleInversion(new Point(-0.25,0.25), 2),
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new Rotation(2*Math.PI/2.5, false),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)),
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new Rotation(4*Math.PI/3, true),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)),
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new Rotation(4*Math.PI/3, true),
						new CircleInversion(new Point(0.1,-0.4), 0.4),
						new Rotation(Math.PI/3, true),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)),
				new CompoundTransformation(
						new CircleInversion(new Point(1,1), 2),
						new Rotation(4*Math.PI/3, true),
						new CircleInversion(new Point(-1,-1), 0.5)),
				new CompoundTransformation(
						new CircleInversion(new Point(0.25,0.25), 0.5),
						new CircleInversion(new Point(-0.25,-0.25), 0.5)));
		
		IcarusFrame f = new IcarusFrame();
		f.show();
	}
	
	public static List<Variation> variationList() {
		return variationDefinitions;
	}
	
	public static void createSymmetry(String name, Transformation... transformations) {
		Symmetry sym = new Symmetry(name);
		for(Transformation t : transformations) {
			sym.add(t);
		}
		symmetryMap.put(sym.toString(), sym);
	}
}
