package net.gillessed.icarus.swingui.explorer;

import java.awt.Container;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.gillessed.icarus.FlameModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ExplorerFrame {
	private final JDialog dialog;
	private final ExplorerFlamePanel[] flamePanels;
	
	private final FlameModel flameModel;
	private final JFrame parent;
	
	public ExplorerFrame(JFrame parent, FlameModel flameModel) {
		this.parent = parent;
		this.flameModel = flameModel;
		
		dialog = new JDialog(parent);
		dialog.setSize(800, 600);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setTitle("Transform Explorer");
		
		Container c = dialog.getContentPane();
		
		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout("10dlu, pref:grow, 10dlu, pref:grow, 10dlu, pref:grow, 10dlu",
											"10dlu, pref:grow, 10dlu, pref:grow, 10dlu, pref:grow, 10dlu");
		c.setLayout(layout);
		flamePanels = new ExplorerFlamePanel[9];
		
		dialog.pack();
	}
	
	public void show() {
		dialog.setVisible(true);
	}
	
	public void hide() {
		dialog.setVisible(false);
	}
	
	private void perturbTransform() {
		
	}
}
