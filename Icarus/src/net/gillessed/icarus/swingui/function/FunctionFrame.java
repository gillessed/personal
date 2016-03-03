package net.gillessed.icarus.swingui.function;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.Function;
import net.gillessed.icarus.Icarus;
import net.gillessed.icarus.event.FlameModificationListener;
import net.gillessed.icarus.event.NewFlameListener;
import net.gillessed.icarus.event.FunctionEvent;
import net.gillessed.icarus.event.FunctionListener;
import net.gillessed.icarus.swingui.FlameModelContainer;
import net.gillessed.icarus.variation.Variation;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class FunctionFrame {

	private boolean removing;
	private final FlameModelContainer flameModelContainer;
	private final FunctionEditPanel functionEditPanel;
	private final JDialog frame;
	private final JList<Function> functionList;
	private final DefaultListModel<Function> functionListModel;
	private final JButton addFunction;
	private final JButton removeFunction;
	private final JButton random;
	private final JButton ok;

	private final ActionListener addListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Function variation = new Function(flameModelContainer.getFlameModel().getVariationDefinitions());
			flameModelContainer.getFlameModel().addFunction(variation);
		}
	};
	private final ActionListener removeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Function> variations = flameModelContainer.getFlameModel().getFunctions();
			int selected = functionList.getSelectedIndex();
			flameModelContainer.getFlameModel().removeFunction(variations.get(selected));
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
			if(flameModelContainer.getFlameModel().getFunctions().size() == 1) {
				removeFunction.setEnabled(false);
			}
            flameModelContainer.flameModified();
		}
		@Override
		public void functionAdded(FunctionEvent e) {
			functionListModel.addElement(e.getChild());
			if(flameModelContainer.getFlameModel().getFunctions().size() > 1) {
				removeFunction.setEnabled(true);
			}
            flameModelContainer.flameModified();
		}
	};
	private final ListSelectionListener listListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting() && !removing) {
				functionEditPanel.applyChanges();
				int selected = functionList.getSelectedIndex();
				functionEditPanel.setModel(flameModelContainer.getFlameModel().getFunctions().get(selected >= 0 ? selected : 0));
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
			for(Function f : flameModelContainer.getFlameModel().getFunctions()) {
				for(Variation v : f.getVariations()) {
					if(Math.random() > 0.6) {
						v.setWeight(Math.random());
					} else {
						v.setWeight(0);
					}
				}
			}
			functionEditPanel.updateTable();
			flameModelContainer.flameModified();
		}
	};

	private final NewFlameListener flameChangeListener = new NewFlameListener() {
		@Override
		public void newFlame(FlameModel flameModel) {
			updateFunctions(flameModel, true);
		}
	};

    private final FlameModificationListener flameModificationListener = new FlameModificationListener() {

        @Override
        public void flameModified() {
            updateFunctions(flameModelContainer.getFlameModel(), false);
        }
    };

    private class VariationIncrementedListener implements ActionListener {
        private final Variation variation;
        public VariationIncrementedListener(Variation variation) {
            this.variation = variation;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for(Function function : flameModelContainer.getFlameModel().getFunctions()) {
                Variation var = function.getVariation(variation);
                var.setWeight(var.getWeight() + 1);
            }
            flameModelContainer.flameModified();
        }
    }

    private class VariationErasedListener implements ActionListener {
        private final Variation variation;
        public VariationErasedListener(Variation variation) {
            this.variation = variation;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            for(Function function : flameModelContainer.getFlameModel().getFunctions()) {
                Variation var = function.getVariation(variation);
                var.setWeight(0);
            }
            flameModelContainer.flameModified();
        }
    }

	public FunctionFrame(JFrame parent, FlameModelContainer flameModelContainer) {
		this.flameModelContainer = flameModelContainer;
		this.flameModelContainer.addNewFlameListener(flameChangeListener);
		this.flameModelContainer.addFlameModificationListener(flameModificationListener);
		removing = false;

		frame = new JDialog(parent);
		frame.setTitle("Functions");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu functionMenu = new JMenu("Function");

		JMenu incrementSubMenu = new JMenu("Increment");
		for(Variation variation : Icarus.variationList()) {
		    JMenuItem variationMenuItem = new JMenuItem(variation.getName());
		    variationMenuItem.addActionListener(new VariationIncrementedListener(variation));
		    incrementSubMenu.add(variationMenuItem);
		}
		functionMenu.add(incrementSubMenu);

        JMenu removeSubMenu = new JMenu("Remove");
        for(Variation variation : Icarus.variationList()) {
            JMenuItem variationMenuItem = new JMenuItem(variation.getName());
            variationMenuItem.addActionListener(new VariationErasedListener(variation));
            incrementSubMenu.add(variationMenuItem);
        }
        functionMenu.add(removeSubMenu);

		menuBar.add(functionMenu);
		frame.setJMenuBar(menuBar);

		Container c = frame.getContentPane();
		functionListModel = new DefaultListModel<Function>();
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

		functionEditPanel = new FunctionEditPanel(flameModelContainer, null);
		functionEditPanel.setPreferredSize(new Dimension(350,300));
		panel.add(functionEditPanel, cc.xy(5, 4));

		JPanel plusMinusPanel = new JPanel();
		addFunction = new JButton("+");
		addFunction.addActionListener(addListener);
		plusMinusPanel.add(addFunction);

		removeFunction = new JButton("-");
		removeFunction.addActionListener(removeListener);
		plusMinusPanel.add(removeFunction);

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

	private void updateFunctions(FlameModel flameModel, boolean attachNewListener) {
	    int selectedIndex = functionList.getSelectedIndex();
		removing = true;
		functionListModel.clear();
		for(Function v : flameModel.getFunctions()) {
			functionListModel.addElement(v);
		}
		if(selectedIndex < functionListModel.size() && selectedIndex >= 0) {
            functionList.setSelectedIndex(selectedIndex);
		} else {
			functionList.setSelectedIndex(0);
		}
		functionEditPanel.setModel(functionList.getSelectedValue());
		removing = false;

		if(attachNewListener) {
		    flameModel.addFunctionListener(functionListener);
		}
		if(flameModel.getFunctions().size() <= 1) {
			removeFunction.setEnabled(false);
		}
	}

	public void show() {
		if(!frame.isVisible()) {
			frame.setVisible(true);
		}
	}
}
