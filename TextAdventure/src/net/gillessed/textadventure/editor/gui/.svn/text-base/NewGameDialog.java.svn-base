package net.gillessed.textadventure.editor.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class NewGameDialog {
	
	private static final String DEFAULT_GAME_NAME = "My Game";
	
	private final JDialog dialog;
	private final JTextField gameNameTextField;
	private final JButton okButton;
	private final JButton cancelButton;
	private final EditorFrame editorFrame;
	
	public NewGameDialog(EditorFrame editorFrame) {
		this.editorFrame = editorFrame;
		dialog = new JDialog(editorFrame);
		dialog.setResizable(false);
		dialog.setTitle("New Game");
		FormLayout layout = new FormLayout("10dlu, pref, 10dlu, pref, 10dlu", "10dlu, pref, 10dlu, pref, 10dlu ");
		CellConstraints cc = new CellConstraints();
		dialog.setLayout(layout);
		Container c = dialog.getContentPane();
		c.add(new JLabel("Enter a name for your game: "), cc.xy(2, 2));
		gameNameTextField = new JTextField(15);
		gameNameTextField.setText(DEFAULT_GAME_NAME);
		gameNameTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					ok();
				} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cancel();
				}
			}
		});
		c.add(gameNameTextField, cc.xy(4, 2));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		buttonPanel.add(okButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		buttonPanel.add(cancelButton);
		c.add(buttonPanel, cc.xyw(2, 4, 3));
		dialog.pack();
		dialog.setVisible(true);
	}
	
	private void ok() {
		editorFrame.createNewModel(gameNameTextField.getText());
		dialog.setVisible(false);
	}
	
	private void cancel() {
		dialog.setVisible(false);
	}
}
