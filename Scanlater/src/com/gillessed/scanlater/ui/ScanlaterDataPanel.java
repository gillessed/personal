package com.gillessed.scanlater.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.undo.BackgroundColourAction;
import com.gillessed.scanlater.ui.undo.FontSizeAction;
import com.gillessed.scanlater.ui.undo.FontStyleAction;
import com.gillessed.scanlater.ui.undo.TextAction;
import com.gillessed.scanlater.ui.undo.TextColourAction;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ScanlaterDataPanel extends JPanel {
	private static final long serialVersionUID = 4851233853164092087L;

	private static final String PLAIN = "Plain";
	private static final String BOLD = "Bold";
	private static final String ITALIC = "Italic";
	
	private final ActionListener newBubbleListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(newBubbleButton.isSelected()) {
				Globals.instance().newSelector();
				Globals.instance().setMode(Globals.Mode.NEW_BUBBLE, ScanlaterDataPanel.this);
			} else {
				Globals.instance().setMode(Globals.Mode.STANDBY, ScanlaterDataPanel.this);
			}
		}	
	};
	
	private final ActionListener fontStyleListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(activateListeners) {
				if(Globals.instance().hasSelectedBubble()) {
					int oldStyle = Globals.instance().getSelectedBubble().getFontStyle();
					String str = (String)fontStyleComboBox.getSelectedItem();
					switch(str) {
					case PLAIN: Globals.instance().selectedFontStyle = Font.PLAIN; break;
					case BOLD: Globals.instance().selectedFontStyle = Font.BOLD; break;
					case ITALIC: Globals.instance().selectedFontStyle = Font.ITALIC; break;
					}
					Globals.instance().getSelectedBubble().setFontStyle(Globals.instance().selectedFontStyle);
					FontStyleAction action = new FontStyleAction(Globals.instance().getSelectedBubble(), oldStyle, Globals.instance().selectedFontStyle);
					Globals.instance().getUndoStack().addAction(action);
				}
			}
		}
	};
	
	private final ActionListener fontSizeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(activateListeners) {
				if(Globals.instance().hasSelectedBubble()) {
					int oldSize = Globals.instance().getSelectedBubble().getFontSize();
					Integer size = (Integer)fontSizeComboBox.getSelectedItem();
					Globals.instance().selectedFontSize = size;
					Globals.instance().getSelectedBubble().setFontSize(size);
					FontSizeAction action = new FontSizeAction(Globals.instance().getSelectedBubble(), oldSize, size);
					Globals.instance().getUndoStack().addAction(action);
				}
			}
		}
	};
	
	private final BubbleUpdateListener bubbleUpdateListener = new BubbleUpdateListener() {
		@Override
		public void bubbleUpdated(Bubble bubble) {
			if(bubble != null) {
				updateBubbleForm(bubble);
			}
		}
	};
	
	private final BubbleListener bubbleSelectedListener = new BubbleListener() {
		@Override
		public void bubbleSelected(Bubble bubble) {
			activateListeners = false;
			newBubbleButton.setSelected(false);
			if(bubble == null) {
				translatedTextArea.setText("");
				translatedTextArea.setEnabled(false);
				fontStyleComboBox.setEnabled(false);
				fontSizeComboBox.setEnabled(false);
				textColourButton.setEnabled(false);
				textColourIcon.setColor(Color.black);
				backgroundColourButton.setEnabled(false);
				backgroundColourIcon.setColor(Color.black);
				hideButton.setEnabled(false);
				deleteButton.setEnabled(false);
			} else {
				translatedTextArea.setEnabled(true);
				fontStyleComboBox.setEnabled(true);
				fontSizeComboBox.setEnabled(true);
				textColourButton.setEnabled(true);
				backgroundColourButton.setEnabled(true);
				hideButton.setEnabled(true);
				deleteButton.setEnabled(true);
				translatedTextArea.setText(bubble.getTextString());
				updateBubbleForm(bubble);
			}
			activateListeners = true;
		}
	};
	
	private final ActionListener textColourListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(activateListeners && Globals.instance().hasSelectedBubble()) {
				Color oldColour = Globals.instance().getSelectedBubble().getTextColour();
				Color chosenColour = JColorChooser.showDialog(ScanlaterDataPanel.this,
						"Choose a Colour", oldColour);
				if(chosenColour == null) {
					chosenColour = oldColour;
				}
				Globals.instance().getSelectedBubble().setTextColour(chosenColour);
				TextColourAction action = new TextColourAction(Globals.instance().getSelectedBubble(), oldColour, chosenColour);
				Globals.instance().getUndoStack().addAction(action);
			}
		}
	};
	
	private final ActionListener backgroundColourListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(activateListeners && Globals.instance().hasSelectedBubble()) {
				Color oldColour = Globals.instance().getSelectedBubble().getBackgroundColour();
				Color chosenColour = JColorChooser.showDialog(ScanlaterDataPanel.this,
						"Choose a Colour", oldColour);
				if(chosenColour == null) {
					chosenColour = oldColour;
				}
				Globals.instance().getSelectedBubble().setBackgroundColour(chosenColour);
				BackgroundColourAction action = new BackgroundColourAction(Globals.instance().getSelectedBubble(), oldColour, chosenColour);
				Globals.instance().getUndoStack().addAction(action);
			}
		}
	};
	
	private final DocumentListener documentListener = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			updateBubble(translatedTextArea.getDocument());
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			updateBubble(translatedTextArea.getDocument());
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			updateBubble(translatedTextArea.getDocument());
		}
		
		private void updateBubble(Document document) {
			if(activateListeners && Globals.instance().hasSelectedBubble()) {
				textEditFlag = true;
				String oldText = Globals.instance().getSelectedBubble().getTextString();
				String newText = translatedTextArea.getText();
				Globals.instance().getSelectedBubble().setTextString(newText);
				TextAction action = new TextAction(Globals.instance().getSelectedBubble(), oldText, newText);
				Globals.instance().getUndoStack().addAction(action);
				textEditFlag = true;
			}
		}
	};
	
	private JToggleButton newBubbleButton;
	private JTextArea translatedTextArea;
	private JComboBox<String> fontStyleComboBox;
	private JComboBox<Integer> fontSizeComboBox;
	private JButton textColourButton;
	private ColourIcon textColourIcon;
	private JButton backgroundColourButton;
	private ColourIcon backgroundColourIcon;
	private JButton hideButton;
	private JButton deleteButton;
	
	private Project project;
	
	private boolean activateListeners;
	private boolean textEditFlag;
	
	public ScanlaterDataPanel(Project project) {
		this.project = project;
		
		activateListeners = true;
		textEditFlag = false;
		Globals.instance().setBubbleUpdateListener(bubbleUpdateListener);
		
		CellConstraints cc = new CellConstraints();
		setLayout(new FormLayout("10dlu, pref, 10dlu, fill:pref:grow, 10dlu",
				"20dlu, pref, 50dlu, pref, 5dlu, pref, 10dlu, pref, 10dlu, pref"
				+ ", 10dlu, pref, 10dlu, pref, 50dlu, pref, 10dlu, pref"));

		newBubbleButton = new JToggleButton("NEW BUBBLE");
		newBubbleButton.setMargin(new Insets(10, 10, 10, 10));
		newBubbleButton.setFont(ScanlaterFonts.normalLarge);
		newBubbleButton.addActionListener(newBubbleListener);
		add(newBubbleButton, cc.xyw(2, 2, 3));
		
		JLabel translatedLabel = new JLabel("Translated text");
		translatedLabel.setFont(ScanlaterFonts.normalSmall);
		add(translatedLabel, cc.xyw(2, 4, 3));
		translatedTextArea = new JTextArea(7, 1);
		translatedTextArea.setFont(ScanlaterFonts.normalVerySmall);
		translatedTextArea.getDocument().addDocumentListener(documentListener);
		add(translatedTextArea, cc.xyw(2, 6, 3));
		
		JLabel fontTypeLabel = new JLabel("Font Type:");
		fontTypeLabel.setFont(ScanlaterFonts.normalSmall);
		add(fontTypeLabel, cc.xy(2, 8));
		fontStyleComboBox = new JComboBox<String>(new String[] {PLAIN, BOLD, ITALIC});
		fontStyleComboBox.setFont(ScanlaterFonts.normalSmall);
		fontStyleComboBox.addActionListener(fontStyleListener);
		add(fontStyleComboBox, cc.xy(4, 8));
		
		JLabel fontSizeLabel = new JLabel("Font Size:");
		fontSizeLabel.setFont(ScanlaterFonts.normalSmall);
		add(fontSizeLabel, cc.xy(2, 10));
		fontSizeComboBox = new JComboBox<Integer>(new Integer[] {4,6,8,10,12,14,16,18,20,24,28,32,36,40,44,48,56,64,72,88,104,120,140,160,180,200});
		fontSizeComboBox.setSelectedIndex(4);
		fontSizeComboBox.setFont(ScanlaterFonts.normalVerySmall);
		fontSizeComboBox.addActionListener(fontSizeListener);
		add(fontSizeComboBox, cc.xy(4, 10));
		
		JLabel fontColourLabel = new JLabel("Font Colour:");
		fontColourLabel.setFont(ScanlaterFonts.normalSmall);
		add(fontColourLabel, cc.xy(2, 12));
		textColourIcon = new ColourIcon(Color.black);
		textColourButton = new JButton(textColourIcon);
		textColourButton.addActionListener(textColourListener);
		add(textColourButton, cc.xy(4, 12));
		
		JLabel backColourLabel = new JLabel("Background Colour:");
		backColourLabel.setFont(ScanlaterFonts.normalSmall);
		add(backColourLabel, cc.xy(2, 14));
		backgroundColourIcon = new ColourIcon(Color.black);
		backgroundColourButton = new JButton(backgroundColourIcon);
		backgroundColourButton.addActionListener(backgroundColourListener);
		add(backgroundColourButton, cc.xy(4, 14));
		
		hideButton = new JButton("HIDE BUBBLE");
		hideButton.setFont(ScanlaterFonts.normalLarge);
		add(hideButton, cc.xyw(2, 16, 3));
		
		deleteButton = new JButton("DELETE BUBBLE");
		deleteButton.setFont(ScanlaterFonts.normalLarge);
		add(deleteButton, cc.xyw(2, 18, 3));

		translatedTextArea.setEnabled(false);
		fontStyleComboBox.setEnabled(false);
		fontSizeComboBox.setEnabled(false);
		textColourButton.setEnabled(false);
		backgroundColourButton.setEnabled(false);
		hideButton.setEnabled(false);
		deleteButton.setEnabled(false);
		if(this.project == null) {
			newBubbleButton.setEnabled(false);
		}
		
		Globals.instance().addBubbleListener(bubbleSelectedListener);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		if(this.project != null) {
			newBubbleButton.setEnabled(true);
		}
	}
	
	private void updateBubbleForm(Bubble bubble) {
		activateListeners = false;
		if(!textEditFlag) {
			translatedTextArea.setText(bubble.getTextString());
		}
		int fontStyle = bubble.getFontStyle();
		String styleString = PLAIN;
		switch(fontStyle) {
		case Font.PLAIN: styleString = PLAIN; break;
		case Font.BOLD: styleString = BOLD; break;
		case Font.ITALIC: styleString = ITALIC; break;
		}
		for(int i = 0; i < fontStyleComboBox.getItemCount(); i++) {
			String obj = fontStyleComboBox.getItemAt(i);
			if(styleString.equals(obj)) {
				fontStyleComboBox.setSelectedIndex(i);
				break;
			}
		}
		fontSizeComboBox.setEnabled(true);
		for(int i = 0; i < fontSizeComboBox.getItemCount(); i++) {
			Integer item = fontSizeComboBox.getItemAt(i);
			if(item.equals(bubble.getFontSize())) {
				fontSizeComboBox.setSelectedIndex(i);
				break;
			}
		}
		textColourIcon.setColor(bubble.getTextColour());
		textColourButton.repaint();
		backgroundColourIcon.setColor(bubble.getBackgroundColour());
		backgroundColourButton.repaint();
		activateListeners = true;
	}
}
