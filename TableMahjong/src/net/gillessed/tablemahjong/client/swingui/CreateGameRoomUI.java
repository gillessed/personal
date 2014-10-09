package net.gillessed.tablemahjong.client.swingui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.gillessed.tablemahjong.client.CreateGameRoom;
import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateEvent.UpdateType;
import net.gillessed.tablemahjong.client.event.UpdateListener;
import net.gillessed.tablemahjong.rules.AbstractMahjongRules;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.swingui.PlayerSelectionComboBoxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class CreateGameRoomUI extends JPanel {
	private final CreateGameRoom model;
	private final MainRoomUI UIParent;
	
	private final JComboBox ruleSet;
	private final SpinnerNumberModel startMoneyModel;
	private final JSpinner startMoney;
	private final JTextField descriptionField;

	private final JTextField player1;
	private final JComboBox player2;
	private final JComboBox player3;
	private final JComboBox player4;
	
	private final JButton okStart;
	private final JButton cancel;
	
	private final CellConstraints cc;
	
	private ActionListener okStartListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(model.getState() == CreateGameRoom.CREATED) {
				model.getBean().startingMoney = startMoneyModel.getNumber().intValue();
				model.getBean().description = descriptionField.getText();
				model.getBean().ruleSet = ruleSet.getSelectedItem().toString();
				model.finalizeToServer();
				getUIParent().getCreateGameButton().setEnabled(false);
				toggleGUI();
			} else {
				JSONObject obj = new JSONObject();
				try {
					obj.put("action", "start");
					obj.put("room", model.getUuid());
					obj.put("bean", model.getBean().getJSON());
					List<String> players = new ArrayList<String>();
					players.add(model.getBean().creator);
					players.add((String)model.getPlayer2().getSelectedItem());
					players.add((String)model.getPlayer3().getSelectedItem());
					players.add((String)model.getPlayer4().getSelectedItem());
					for(int i = 1; i <= 4; i++) {
						obj.put("player" + i, players.get(i-1));
					}
					JSONArray joinerList = new JSONArray();
					for(String s : model.getJoiners()) {
						if(!players.contains(s)) {
							joinerList.put(s);
						}
					}
					obj.put("joiners", joinerList);
				} catch (JSONException exc) {
					throw new RuntimeException(exc);
				}
				Packet finalize = new Packet(PacketType.CREATEROOMACTION, Packet.ATTEMPT, obj);
				getUIParent().getModel().getClientUpdaterThread().sendPacket(finalize);
				
				closeUI();
				getUIParent().getModel().getCreateGameRooms().remove(getModel().getUuid());
			}
		}
	};
	
	private ActionListener closeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
	};
	
	private class ComboListener implements ItemListener {

		private final String name;
		private final Map<String, PlayerSelectionComboBoxModel> playerModels;
		
		public ComboListener(String name, Map<String,PlayerSelectionComboBoxModel> playerModels) {
			this.name = name;
			this.playerModels = playerModels;
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.DESELECTED) {
				for(Map.Entry<String, PlayerSelectionComboBoxModel> entry : playerModels.entrySet()) {
					if(!name.equals(entry.getKey())) {
						if(!(e.getItem() instanceof String)) {
							throw new RuntimeException("Item must be a string");
						}
						String item = (String)e.getItem();
						if(item.length() > 0 && model.isMagic()) {
							entry.getValue().addPlayer(item);
						}
					}
				}
			} else if(e.getStateChange() == ItemEvent.SELECTED) {
				for(Map.Entry<String, PlayerSelectionComboBoxModel> entry : playerModels.entrySet()) {
					if(!name.equals(entry.getKey())) {
						if(!(e.getItem() instanceof String)) {
							throw new RuntimeException("Item must be a string");
						}
						String item = (String)e.getItem();
						if(item.length() > 0 && model.isMagic()) {
							entry.getValue().removePlayer(item);
						}
					}
				}
			}
		}
	}
	
	private final ComboListener player2Listener;
	private final ComboListener player3Listener;
	private final ComboListener player4Listener;

	public CreateGameRoomUI(MainRoomUI parent, CreateGameRoom model) {
		this.UIParent = parent;
		this.model = model;
		
		player2Listener = new ComboListener(CreateGameRoom.PLAYER2, model.getPlayerModels());
		player3Listener = new ComboListener(CreateGameRoom.PLAYER3, model.getPlayerModels());
		player4Listener = new ComboListener(CreateGameRoom.PLAYER4, model.getPlayerModels());
		
		FormLayout layout = new FormLayout("10dlu, pref, 10dlu, pref, 10dlu",
				"10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu");
		cc = new CellConstraints();
		
		setLayout(layout);
		
		ruleSet = new JComboBox(AbstractMahjongRules.getMahjongRules().values().toArray());
		addLabeled(ruleSet, "Rule Set: ", 2);
		
		startMoneyModel = new SpinnerNumberModel(20000, 5000, 100000, 100);
		startMoney = new JSpinner(startMoneyModel);
		addLabeled(startMoney, "Start Money: ", 4);
		
		descriptionField = new JTextField(20);
		addLabeled(descriptionField, "Description: ", 6);
		
		player1 = new JTextField(model.getBean().creator);
		player1.setEnabled(false);
		addLabeled(player1, "Player 1: ", 8);
		
		player2 = new JComboBox(model.getPlayer2());
		player2.addItemListener(player2Listener);
		addLabeled(player2, "Player 2: ", 10);
		
		player3 = new JComboBox(model.getPlayer3());
		player3.addItemListener(player3Listener);
		addLabeled(player3, "Player 3: ", 12);
		
		player4 = new JComboBox(model.getPlayer4());
		player4.addItemListener(player4Listener);
		addLabeled(player4, "Player 4: ", 14);
		
		okStart = new JButton("Ok");
		okStart.addActionListener(okStartListener);
		cancel = new JButton("Cancel");
		cancel.addActionListener(closeListener);
		
		model.addUpdateListener(new UpdateListener() {
			@Override
			public void actionPerformed(UpdateEvent e) {
				if(e.getType() == UpdateType.JOINCREATEGAME || e.getType() == UpdateType.QUITCREATEGAME) {
					if(getModel().getState() == CreateGameRoom.FINALIZED) {
						try {
							okStart.setEnabled(e.getEventData().getBoolean("is-ready"));
						} catch (JSONException ex) {
							throw new RuntimeException(ex);
						}
					} 
				}
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(okStart);
		buttonPanel.add(cancel);
		add(buttonPanel, cc.xy(4, 16));
		
		toggleGUI();
	}
	
	private void toggleGUI() {
		player2.setEnabled(!(model.getState() == CreateGameRoom.CREATED));
		player3.setEnabled(!(model.getState() == CreateGameRoom.CREATED));
		player4.setEnabled(!(model.getState() == CreateGameRoom.CREATED));
		ruleSet.setEnabled(!(model.getState() == CreateGameRoom.FINALIZED));
		startMoney.setEnabled(!(model.getState() == CreateGameRoom.FINALIZED));
		descriptionField.setEditable(!(model.getState() == CreateGameRoom.FINALIZED));
		checkStart();
		repaint();
	}
	
	private void addLabeled(Component c, String label, int row) {
		add(new JLabel(label), cc.xy(2, row));
		add(c, cc.xy(4,row));
	}
	
	public void close() {
		closeUI();
		getUIParent().getCreateGameButton().setEnabled(true);
		sendKillToServer();
		getUIParent().getModel().getCreateGameRooms().remove(getModel().getUuid());
	}
	
	private void sendKillToServer() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("action", "kill");
			obj.put("room", model.getUuid());
			obj.put("bean", model.getBean().getJSON());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		Packet finalize = new Packet(PacketType.CREATEROOMACTION, Packet.ATTEMPT, obj);
		getUIParent().getModel().getClientUpdaterThread().sendPacket(finalize);
	}

	public void closeUI() {
		getParent().remove(this);
	}
	
	public void checkStart() {
		okStart.setText((model.getState() == CreateGameRoom.CREATED) ? "Ok" : "Start");
		okStart.setEnabled((model.getState() == CreateGameRoom.CREATED) || model.isReady());
	}
	
	public CreateGameRoom getModel() {
		return model;
	}

	public MainRoomUI getUIParent() {
		return UIParent;
	}
}
