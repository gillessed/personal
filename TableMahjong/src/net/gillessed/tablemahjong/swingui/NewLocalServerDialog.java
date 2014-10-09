package net.gillessed.tablemahjong.swingui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.gillessed.tablemahjong.server.ServerType;
import net.gillessed.tablemahjong.server.TableMahjongServer;
import net.gillessed.tablemahjong.server.logging.Logger;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class NewLocalServerDialog {
	
	private final JDialog dialog;
	private final JTextField portField;
	private final JTextField usernameField;
	private final JButton ok;
	private final JButton cancel;
	private final TableMahjongFrame parent;
	
	public final ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	};
	
	public final ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int port = Integer.parseInt(portField.getText());
			TableMahjongServer localServer = new TableMahjongServer(port, ServerType.LOCAL);
			boolean started = false;
			try {
				localServer.start();
				started = true;
			} catch (IOException err) {
				
			}
			if(started) {
				parent.setHostingLocalServer(true);
				parent.loginToLocal("localhost", port, usernameField.getText(), true);
				parent.createLocalServerUpdateUI();
			} else {
				JOptionPane.showMessageDialog(dialog, "Error: that port is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
				Logger.getLogger().error("Port " + port + " is already in use.");
			}
			dialog.dispose();
		}
	};
	
	public NewLocalServerDialog(TableMahjongFrame parent) {
		this.parent = parent;
		dialog = new JDialog(parent.getFrame());
		dialog.setTitle("New Local Server");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		FormLayout layout = new FormLayout("10dlu, pref, 10dlu, pref, 10dlu",
				"10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu");
		CellConstraints cc = new CellConstraints();
		
		Container c = dialog.getContentPane();
		c.setLayout(layout);
		
		c.add(new JLabel("Server Port [Default=2345]: "), cc.xy(2,2));
		portField = new JTextField(20);
		portField.setText("2345");
		c.add(portField, cc.xy(4, 2));
		
		c.add(new JLabel("Username in new server: "), cc.xy(2,4));
		usernameField = new JTextField(20);
		c.add(usernameField, cc.xy(4, 4));
		
		JPanel buttonPane = new JPanel();
		ok = new JButton("Ok");
		ok.addActionListener(okListener);
		cancel = new JButton("Cancel");
		cancel.addActionListener(cancelListener);
		buttonPane.add(ok);
		buttonPane.add(cancel);
		c.add(buttonPane, cc.xy(4, 6));
	}
	
	public void show() {
		dialog.pack();
		dialog.setVisible(true);
	}
}
