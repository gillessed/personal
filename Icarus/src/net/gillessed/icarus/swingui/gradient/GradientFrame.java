package net.gillessed.icarus.swingui.gradient;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.swingui.components.EditFrame;

import com.gillessed.gradient.Gradient;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class GradientFrame implements EditFrame {
	
	private FlameModel flameModel;
	
	private final JDialog frame;
	private final PredefinedGradientPanel predefinedGradientPanel;
	private final GradientEditPanel gradientEditPanel;
	private final JButton ok;

	private final ItemListener predefinedListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				@SuppressWarnings("unchecked")
				JComboBox<Gradient> cb = (JComboBox<Gradient>)e.getSource();
				gradientEditPanel.setGradient((Gradient)cb.getSelectedItem());
				gradientEditPanel.repaint();
		    } 
		}
	};
	private final ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent paramActionEvent) {
			gradientEditPanel.applyChanges();
			frame.dispose();
		}
	};
	public GradientFrame(JFrame parent, FlameModel flameModel, List<Gradient> list) {
		this.setFlameModel(flameModel);
		
		frame = new JDialog(parent);
		frame.setSize(800,300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Gradient");
		
		FormLayout formLayout = new FormLayout("10dlu, pref, 10dlu, pref, 10dlu",
				"10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu");
		CellConstraints cc = new CellConstraints();
		
		Container c = frame.getContentPane();
		c.setLayout(formLayout);

		c.add(new JLabel("Choose Gradient: "), cc.xy(2, 2));
		c.add(new JLabel("Edit Function Values: "), cc.xy(2, 4));
		
		predefinedGradientPanel = new PredefinedGradientPanel(flameModel, list);
		predefinedGradientPanel.getComboBox().addItemListener(predefinedListener);
		c.add(predefinedGradientPanel, cc.xy(4, 2));
		
		gradientEditPanel = new GradientEditPanel(flameModel.getColorProvider().getGradient(), flameModel);
		gradientEditPanel.setPreferredSize(new Dimension(Gradient.DEFAULT_SIZE, 100));
		c.add(gradientEditPanel, cc.xy(4, 4));
		
		JPanel buttonPane = new JPanel();
		ok = new JButton("Ok");
		ok.addActionListener(okListener);
		buttonPane.add(ok);
		c.add(buttonPane, cc.xy(2, 6));
		
	}
	
	public void show() {
		frame.setVisible(true);
	}

	public void setFlameModel(FlameModel flameModel) {
		this.flameModel = flameModel;
	}

	public FlameModel getFlameModel() {
		return flameModel;
	}

	@Override
	public JDialog getFrame() {
		return frame;
	}
	
}
