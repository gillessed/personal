package net.gillessed.icarus.engine;

import java.awt.Dimension;

import net.gillessed.icarus.AffineTransform;
import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.Global;
import net.gillessed.icarus.geometry.ColorPoint;
import net.gillessed.icarus.geometry.Point;
import net.gillessed.icarus.geometry.Symmetry;
import net.gillessed.icarus.geometry.Transformation;
import net.gillessed.icarus.geometry.ViewRectangle;
import net.gillessed.icarus.swingui.color.ColorProvider;
import net.gillessed.icarus.variation.Variation;
import net.gillessed.threadpool.HookedThread;
import net.gillessed.threadpool.ThreadPool;

public class FractalEngineHookedThread extends HookedThread<Void> {

	private static final int BEGINNING_SKIP_COUNT = 
		Integer.parseInt(Global.getProperty(Global.BEGINNING_SKIP_COUNT));
	private final FlameModel flameModel;
	private final FractalEngineThread fractalEngineThread;
	private final Symmetry symmetry;
	private final ColorProvider colorProvider;
	private final ViewRectangle viewRectangle;
	private int colorObject;
	private final int superSampleWidth;
	private final int superSampleHeight;
	private final long amount;

	public FractalEngineHookedThread(ThreadPool<Void> threadPool, String uuid, FractalEngineThread fractalEngineThread, long amount) {
		super(threadPool, uuid);
		this.fractalEngineThread = fractalEngineThread;
		this.amount = amount;
		flameModel = fractalEngineThread.getFlameModel();
		colorProvider = flameModel.getColorProvider();
		symmetry = 	fractalEngineThread.getSymmetry();
		viewRectangle = fractalEngineThread.getViewRectangle();
		Dimension dim = fractalEngineThread.getSuperSampleDimensions();
		superSampleWidth = dim.width;
		superSampleHeight = dim.height;
	}

	@Override
	public void compute() {
		fractalAlgorithm();
	}
	
	public void fractalAlgorithm() {
		double x;
		double y;
		double ptotal;
		double rand;
		
		colorObject = colorProvider.getRandomColorObject();
		ptotal = 0;
		x = Math.random() * 2 - 1;
		y = Math.random() * 2 - 1;
		for(Function v : flameModel.getFunctions()) {
			ptotal += v.getProbability();
		}
		for(Function v : flameModel.getFunctions()) {
			v.setNormalizedProbability(v.getProbability() / ptotal);
		}
		for(int i = 0; i < BEGINNING_SKIP_COUNT; i++) {
			rand = Math.random();
			Point p = f(flameModel, rand, x, y);
			x = p.getX();
			y = p.getY();
			fractalEngineThread.calculated();
		}
		for(int i = 0; i < amount; i++) {
			rand = Math.random();
			ColorPoint p = f(flameModel, rand, x, y);
			x = p.getX();
			y = p.getY();
			int cx, cy;
			for(Transformation r : symmetry) {
				Point newPoint = r.transform(p);
				cx = viewRectangle.changeX(newPoint.getX(), superSampleWidth);
				cy = viewRectangle.changeY(newPoint.getY(), superSampleHeight);
				if(cx >= 0 && cx < superSampleWidth && cy >= 0 && cy < superSampleHeight) {
					fractalEngineThread.updateModel(cx, cy, (short)p.getColor());
				}
			}
			fractalEngineThread.calculated();
		}
	}
	public ColorPoint f(FlameModel flameModel, double n, double x, double y)
	{
		double tempx = 0;
		double tempy = 0;
		int choice = 0;
		Function function = null;
		double lowerSum = 0;
		double higherSum = 0;
		for(int i = 0 ; i < flameModel.getFunctions().size(); i++) {
			higherSum = lowerSum + flameModel.getFunctions().get(i).getNormalizedProbability();
			if(n >= lowerSum && n < higherSum)
			{
				choice = i;
				function = flameModel.getFunctions().get(choice);
				break;
			}
			lowerSum = higherSum;
		}
		AffineTransform affineTransform = function.getAffineTransform();
		tempx = affineTransform.getA() * x + affineTransform.getB() * y + affineTransform.getC();
		tempy = affineTransform.getD() * x + affineTransform.getE() * y + affineTransform.getF();
		Point p = v(flameModel, choice, tempx, tempy);
		
		int tempColor = function.getColor();
		
		colorObject = (colorObject + tempColor) / 2;
		
		ColorPoint cp = new ColorPoint(p, colorObject);
		
		return cp;
	}
	public Point v(FlameModel flameModel, int n, double tempx, double tempy)
	{
		double sumx = 0.0, sumy = 0.0;
		double wtotal = 0;
		
		for(Variation v : flameModel.getFunctions().get(n).getVariations()) {
			if(v.getWeight() > 0) {
				wtotal += v.getWeight();
			}
		}
		
		for(Variation v : flameModel.getFunctions().get(n).getVariations()) {
			if(v.getWeight() > 0) {
				Point p = v.calculate(tempx, tempy);
				sumx += v.getWeight() / wtotal * p.getX();
				sumy += v.getWeight() / wtotal * p.getY();
			}
		}
		
		return new Point(sumx, sumy);
	}

}
