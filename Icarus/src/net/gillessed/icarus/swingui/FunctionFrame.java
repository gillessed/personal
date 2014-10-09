package net.gillessed.icarus.swingui;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.event.FunctionEvent;
import net.gillessed.icarus.event.FunctionListener;
import net.gillessed.icarus.variation.Variation;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class FunctionFrame implements EditFrame {
	private FunctionEditPanel functionEditPanel;
	private boolean removing;
	private final JDialog frame;
	private final JList<Function> functionList;
	private final DefaultListModel<Function> functionListModel;
	private final FlameModel flameModel;
	private final JButton addFunction;
	private final JButton removeFunction;
	private final JButton random;
	private final JButton ok;
	private final ActionListener addListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Function variation = new Function(flameModel.getVariationDefinitions());
			flameModel.addFunction(variation);
		}
	};
	private final ActionListener removeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Function> variations = flameModel.getFunctions();
			int selected = functionList.getSelectedIndex();
			flameModel.removeVariation(variations.get(selected));
			if(selected >= functionListModel.size()) {
				selected = functionListModel.size() - 1;
			}
			functionList.setSelectedIndex(selected);
		}
	};
	private final FunctionListener functionListener = new FunctionListener() {
		@Override
		public void functionRemoved(FunctionEvent e) {
			removing = true;
			functionListModel.removeElement(e.getChild());
			removing = false;
			if(flameModel.getFunctions().size() == 1) {
				removeFunction.setEnabled(false);
			}
		}
		@Override
		public void functionAdded(FunctionEvent e) {
			functionListModel.addElement(e.getChild());
			if(flameModel.getFunctions().size() > 1) {
				removeFunction.setEnabled(true);
			}
		}
	};
	private final ListSelectionListener listListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting() && !removing) {
				functionEditPanel.applyChanges();
				int selected = functionList.getSelectedIndex();
				functionEditPanel.setModel(flameModel.getFunctions().get(selected >= 0 ? selected : 0));
			}
		}
	};
	private final ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			functionEditPanel.applyChanges();
			frame.dispose();
		}
	};
	private final ActionListener randomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(Function f : flameModel.getFunctions()) {
				for(Variation v : f.getVariations()) {
					if(Math.random() > 0.6) {
						v.setWeight(Math.random());
					} else {
						v.setWeight(0);
					}
				}
			}
			functionEditPanel.updateTable();
		}
	};
	private final WindowListener windowListener = new WindowListener() {
		@Override
		public void windowOpened(WindowEvent arg0) {}
		@Override
		public void windowIconified(WindowEvent arg0) {}
		@Override
		public void windowDeiconified(WindowEvent arg0) {}
		@Override
		public void windowDeactivated(WindowEvent arg0) {}
		@Override
		public void windowClosing(WindowEvent arg0) {}
		
		@Override
		public void windowClosed(WindowEvent arg0) {
			functionEditPanel.applyChanges();
			flameModel.removeVariationListener(functionListener);
		}
		
		@Override
		public void windowActivated(WindowEvent arg0) {}
	};
	/**
	 * @param parent
	 * @param flameModel
	 */
	public FunctionFrame(JFrame parent, FlameModel flameModel) {
		this.flameModel = flameModel;
		this.flameModel.addVariationListener(functionListener);
		removing = false;
		
		frame = new JDialog(parent);
		frame.setTitle("Functions");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(windowListener);
		
		Container c = frame.getContentPane();
		functionListModel = new DefaultListModel<Function>();
		for(Function v : this.flameModel.getFunctions()) {
			functionListModel.addElement(v);
		}
		functionList = new JList<Function>(functionListModel);
		functionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		FunctionListRenderer flr = new FunctionListRenderer();
		functionList.setCellRenderer(flr);
		functionList.setBorder(BorderFactory.createBevelBorder(1));
		functionList.setSelectedIndex(0);
		functionList.addListSelectionListener(listListener);
		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout("10dlu, pref, pref, 10dlu, pref:grow, 10dlu, pref, 10dlu",
											"10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu");
		JPanel panel = new JPanel(layout);

		panel.add(new JLabel("Variations"), cc.xy(2, 2));
		panel.add(new JLabel("Details"), cc.xy(5, 2));
		
		JScrollPane scrollPane = new JScrollPane(functionList);
		scrollPane.setPreferredSize(new Dimension(150,300));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, cc.xyw(2, 4, 2));
		
		functionEditPanel = new FunctionEditPanel(this.flameModel.getFunctions().get(
				functionList.getSelectedIndex()), flameModel);
		functionEditPanel.setPreferredSize(new Dimension(350,300));
		panel.add(functionEditPanel, cc.xy(5, 4));
		
		JPanel plusMinusPanel = new JPanel();
		addFunction = new JButton("+");
		addFunction.addActionListener(addListener);
		plusMinusPanel.add(addFunction);
		
		removeFunction = new JButton("-");
		removeFunction.addActionListener(removeListener);
		plusMinusPanel.add(removeFunction);
		if(this.flameModel.getFunctions().size() == 0) {
			throw new RuntimeException("You cannot have 0 variations for a flame!");
		} else if(this.flameModel.getFunctions().size() == 1) {
			removeFunction.setEnabled(false);
		}
		
		panel.add(plusMinusPanel, cc.xy(3,6));
		
		random = new JButton("Random");
		random.addActionListener(randomListener);
		panel.add(random, cc.xy(5, 6));
		
		ok = new JButton("Ok");
		ok.addActionListener(okListener);
		panel.add(ok, cc.xy(7, 6));
		
		c.add(panel);
		
		frame.setSize(new Dimension(600,450));
	}
	public void show() {
		frame.setVisible(true);
	}
	public JDialog getFrame() {
		return frame;
	}
}
