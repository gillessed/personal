package net.gillessed.icarus.swingui.explorer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.event.FlameModificationListener;
import net.gillessed.icarus.event.NewFlameListener;
import net.gillessed.icarus.swingui.FlameModelContainer;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ExplorerFrame {
    private final static int GAP = 5;

	private final JDialog dialog;
	private final ExplorerFlamePanel centerFlamePanel;
	private final ExplorerFlamePanel[] flamePanels;
	private final FlameModelContainer flameModelContainer;
	private final ExplorerFrameRenderer explorerFrameRenderer;

	private final NewFlameListener newFlameListener = new NewFlameListener() {
        @Override
        public void newFlame(FlameModel flameModel) {
            perturbTransform();
        }
    };

    private final FlameModificationListener flameModificationListener = new FlameModificationListener() {
        @Override
        public void flameModified() {
            perturbTransform();
        }
    };

    private final MouseListener centerPanelMouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            perturbTransform();
        }
    };

    private final MouseListener explorerPanelMouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if(!explorerFrameRenderer.isJobRunning()) {
                flameModelContainer.setFlameModel(((ExplorerFlamePanel)e.getSource()).getFlameModel());
            }
        }
    };

	public ExplorerFrame(JFrame parent, FlameModelContainer flameModelContainer) {
		this.flameModelContainer = flameModelContainer;
		this.flameModelContainer.addNewFlameListener(newFlameListener);
		this.flameModelContainer.addFlameModificationListener(flameModificationListener);

		dialog = new JDialog(parent);
		dialog.setPreferredSize(new Dimension(800, 600));
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("Transform Explorer");

		Container c = dialog.getContentPane();

		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout(GAP + "dlu, fill:pref:grow, " + GAP + "dlu, fill:pref:grow, " + GAP + "dlu, fill:pref:grow, " + GAP + "dlu",
											"" + GAP + "dlu, fill:pref:grow, " + GAP + "dlu, fill:pref:grow, " + GAP + "dlu, fill:pref:grow, " + GAP + "dlu");
		c.setLayout(layout);
		flamePanels = new ExplorerFlamePanel[8];
		for(int i = 0; i < 8; i++) {
		    flamePanels[i] = new ExplorerFlamePanel();
		    flamePanels[i].addMouseListener(explorerPanelMouseListener);
		}
		centerFlamePanel = new ExplorerFlamePanel();
		centerFlamePanel.addMouseListener(centerPanelMouseListener);

		c.add(flamePanels[0], cc.xy(2, 2));
		c.add(flamePanels[7], cc.xy(2, 4));
        c.add(flamePanels[6], cc.xy(2, 6));
        c.add(flamePanels[1], cc.xy(4, 2));
        c.add(centerFlamePanel, cc.xy(4, 4));
        c.add(flamePanels[5], cc.xy(4, 6));
        c.add(flamePanels[2], cc.xy(6, 2));
        c.add(flamePanels[3], cc.xy(6, 4));
        c.add(flamePanels[4], cc.xy(6, 6));

		dialog.pack();

		explorerFrameRenderer = new ExplorerFrameRenderer(flameModelContainer, centerFlamePanel, flamePanels);
	}

	public void show() {
	    if(!dialog.isVisible()) {
	        dialog.setVisible(true);
            explorerFrameRenderer.render();
	    }
	}

	private void perturbTransform() {
	    if(dialog.isVisible()) {
	        explorerFrameRenderer.render();
	    }
	}
}
