package net.gillessed.icarus.engine.fractal.task;

import net.gillessed.icarus.AffineTransform;
import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.Global;
import net.gillessed.icarus.Icarus;
import net.gillessed.icarus.engine.api.AbstractTask;
import net.gillessed.icarus.engine.fractal.IterationHistogram;
import net.gillessed.icarus.geometry.ColorPoint;
import net.gillessed.icarus.geometry.Point;
import net.gillessed.icarus.geometry.Symmetry;
import net.gillessed.icarus.geometry.Transformation;
import net.gillessed.icarus.geometry.ViewRectangle;
import net.gillessed.icarus.variation.Variation;

/**
 * This task computes the Iterated Function System portion
 * of the fractal flame algorithm. Realistically, this is the
 * heaviest step of the process.
 *
 * @author gcole
 */
public final class IterationTask extends AbstractTask<IterationHistogram> {

    private static final int BEGINNING_SKIP_COUNT =
        Integer.parseInt(Global.getProperty(Global.BEGINNING_SKIP_COUNT));

    private static final int REPORT_COUNT = 100;

    private final FlameModel flameModel;
    private final Symmetry symmetry;
    private final ViewRectangle viewRectangle;
    private final int superSampleWidth;
    private final int superSampleHeight;
    private final long amount;

    private IterationHistogram histogram;
    private int colorObject;

    public IterationTask(String name,
            FlameModel flameModel,
            ViewRectangle viewRectangle,
            int superSampleWidth,
            int superSampleHeight,
            long amount) {
        super(name);
        this.flameModel = flameModel;
        this.viewRectangle = viewRectangle;
        this.symmetry = Icarus.symmetryMap.get(flameModel.getSymmetry());
        this.superSampleWidth = superSampleWidth;
        this.superSampleHeight = superSampleHeight;
        this.amount = amount;
        histogram = new IterationHistogram(superSampleWidth, superSampleHeight);
        colorObject = flameModel.getColorProvider().getRandomColorObject();
    }

    @Override
    public IterationHistogram doWork() throws Exception {
        setMaxProgress(amount + BEGINNING_SKIP_COUNT);
        fractalAlgorithm();
        return histogram;
    }
    public void fractalAlgorithm() {
        double x;
        double y;
        double ptotal;
        double rand;

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
            Point p = transform(flameModel, rand, x, y);
            x = p.getX();
            y = p.getY();
        }
        incrementProgress(BEGINNING_SKIP_COUNT);
        for(long i = 0; i < amount; i++) {
            for(long j = 0; i < amount && j < REPORT_COUNT; i++, j++) {
                rand = Math.random();
                ColorPoint p = transform(flameModel, rand, x, y);
                x = p.getX();
                y = p.getY();
                int cx, cy;
                for(Transformation r : symmetry) {
                    Point newPoint = r.transform(p);
                    cx = viewRectangle.changeX(newPoint.getX(), superSampleWidth);
                    cy = viewRectangle.changeY(newPoint.getY(), superSampleHeight);
                    if(cx >= 0 && cx < superSampleWidth && cy >= 0 && cy < superSampleHeight) {
                        histogram.update(cx, cy, p.getColor());
                    }
                }
            }
            incrementProgress(REPORT_COUNT);
        }
    }

    /**
     * Applies the affine transform and the variation transforms
     * to the point {@code (x, y)}.
     */
    public ColorPoint transform(FlameModel flameModel, double n, double x, double y) {
        double tempx = 0;
        double tempy = 0;
        int choice = 0;
        Function function = null;
        double lowerSum = 0;
        double higherSum = 0;
        for(int i = 0 ; i < flameModel.getFunctions().size(); i++) {
            higherSum = lowerSum + flameModel.getFunctions().get(i).getNormalizedProbability();
            if(n >= lowerSum && n < higherSum) {
                choice = i;
                function = flameModel.getFunctions().get(choice);
                break;
            }
            lowerSum = higherSum;
        }
        AffineTransform affineTransform = function.getAffineTransform();
        tempx = affineTransform.getA() * x + affineTransform.getB() * y + affineTransform.getC();
        tempy = affineTransform.getD() * x + affineTransform.getE() * y + affineTransform.getF();
        Point p = variationTransforms(flameModel, choice, tempx, tempy);

        int tempColor = function.getColor();

        colorObject = (colorObject + tempColor) / 2;

        ColorPoint cp = new ColorPoint(p, colorObject);

        return cp;
    }

    /**
     * Applies the variation transforms to the point {@code (x, y)}.
     */
    public Point variationTransforms(FlameModel flameModel, int n, double tempx, double tempy) {
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
