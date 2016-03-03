package net.gillessed.icarus.swingui.explorer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.gillessed.icarus.AffineTransform;
import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.geometry.Point;
import net.gillessed.icarus.geometry.Triangle;
import net.gillessed.icarus.swingui.FlameModelContainer;

public class ExplorerFrameRenderer {

    private final static double SCALE = 0.4;
    private final static double SCALE_MIN = 0.1;

    private final static float[][] PERTURBANCES = new float[][] {
            new float[] {-1,-1},
            new float[] {0,-1},
            new float[] {1,-1},
            new float[] {1,0},
            new float[] {1,1},
            new float[] {0,1},
            new float[] {-1,1},
            new float[] {-1,0},
    };

    private class Perturb implements Callable<Void> {

        private int panelNumber;

        public Perturb(int panelNumber) {
            this.panelNumber = panelNumber;
        }

        @Override
        public Void call() throws Exception {
            FlameModel model = flameModelContainer.getFlameModel().cloneFlame();
            for(Function function : model.getFunctions()) {
                AffineTransform affineTransform = function.getAffineTransform();
                Triangle triangle = new Triangle(affineTransform, null);
                for(int n = 1; n <= 3; n++) {
                    Point p = triangle.getVertex(n);
                    double newX = p.getX() + PERTURBANCES[panelNumber][0] * ((Math.random() * SCALE) + SCALE_MIN);
                    double newY = p.getY() + PERTURBANCES[panelNumber][1] * ((Math.random() * SCALE) + SCALE_MIN);
                    triangle.setVertex(new Point(newX, newY), n);
                }
            }
            flamePanels[panelNumber].setFlameModel(model);
            jobFinished();
            return null;
        }
    }

    private final FlameModelContainer flameModelContainer;
    private final ExplorerFlamePanel centerFlamePanel;
    private final ExplorerFlamePanel[] flamePanels;
    private final ExecutorService explorerExecutor;

    private int runningJobCount;

    public ExplorerFrameRenderer(FlameModelContainer flameModelContainer, ExplorerFlamePanel centerFlamePanel, ExplorerFlamePanel[] flamePanels) {
        this.flameModelContainer = flameModelContainer;
        this.centerFlamePanel = centerFlamePanel;
        this.flamePanels = flamePanels;
        explorerExecutor = Executors.newFixedThreadPool(1);
        runningJobCount = 0;
    }

    public void render() {
        if(getRunningJobCount() == 0) {
            centerFlamePanel.removeImage();
            for(int i = 0; i < 8; i++) {
                flamePanels[i].removeImage();
            }
            resetRunningJobCount(9);
            explorerExecutor.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    centerFlamePanel.setFlameModel(flameModelContainer.getFlameModel());
                    jobFinished();
                    return null;
                }
            });
            for(int i = 0; i < 8; i++) {
                explorerExecutor.submit(new Perturb(i));
            }
        }
    }

    private synchronized int getRunningJobCount() {
        return runningJobCount;
    }

    private synchronized void resetRunningJobCount(int total) {
        runningJobCount = total;
    }

    private synchronized void jobFinished() {
        runningJobCount--;
    }

    public boolean isJobRunning() {
        return getRunningJobCount() > 0;
    }
}
