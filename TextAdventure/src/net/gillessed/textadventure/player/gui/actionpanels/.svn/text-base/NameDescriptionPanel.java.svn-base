package net.gillessed.textadventure.player.gui.actionpanels;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class NameDescriptionPanel extends JPanel {
	public NameDescriptionPanel(String name, Icon icon, String description) {
		FormLayout layout = new FormLayout("fill:pref:grow", "130px, 10px, fill:pref:grow");
		setLayout(layout);
		
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel(name);
		Font current = label.getFont();
		label.setFont(new Font(current.getName(), current.getStyle(), 48));
		label.setIcon(icon);
		labelPanel.add(label);
		add(labelPanel, CC.xy(1, 1));

		JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JTextArea textArea = new JTextArea(13, 60);
		textArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textPanel.add(textArea);
		add(textPanel, CC.xy(1, 3));
	}
}
