package net.gillessed.tablemahjong.client.swingui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import net.gillessed.tablemahjong.client.ClientUpdaterThread;
import net.gillessed.tablemahjong.client.GameRoom;
import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateListener;
import net.gillessed.tablemahjong.mahjonggame.swingui.MahjongGamePane;
import net.gillessed.tablemahjong.mahjongupdate.MahjongUpdate;
import net.gillessed.tablemahjong.mahjongupdate.UpdateTarget;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;

import org.json.JSONException;
import org.json.JSONObject;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class GameRoomUI extends JPanel {
	private final GameRoomUI selfReference = this;
	
	private final MainRoomUI UIParent;
	private final GameRoom model;
	private final DefaultListModel<String> playerListModel;
	
	private final JTextArea chatArea;
	private final JTextField chatField;
	private final JButton chatButton;
	private final MahjongGamePane gamePane;

	private ActionListener chatListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			updateChat();
		}
	};
	
	private KeyListener chatKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
		}
		@Override
		public void keyReleased(KeyEvent e) {
		}
		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("chat?");
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_ENTER) {
				updateChat();
			}
		}
	};
	
	private final UpdateListener ul = new UpdateListener() {
		@Override
		public void actionPerformed(UpdateEvent e) {
			switch(e.getType()) {
			case MESSAGE:
				try {
					JOptionPane.showMessageDialog(selfReference, e.getEventData().getString("message"));
					JSONObject obj = new JSONObject();
					obj.put("room", getModel().getUuid());
					obj.put("action", "message");
					ClientUpdaterThread cut = getUIParent().getModel().getClientUpdaterThread();
					Packet packet = new Packet(PacketType.GAMEROOMACTION, Packet.ATTEMPT, obj);
					cut.sendPacket(packet);
				} catch (HeadlessException exp) {
					throw new RuntimeException(exp);
				} catch (JSONException exp) {
					throw new RuntimeException(exp);
				}
				break;
			case STEALMESSAGE:
				//TODO: show UI in JPanel;
				break;
			case FREEZE:
				try {
					gamePane.getMahjongGameUI().setControlsFrozen(e.getEventData().getBoolean("freeze"));
				} catch (JSONException exp) {
					throw new RuntimeException(exp);
				}
				break;
			case NEWROUND:
				if(model.getRound() != null) {
					model.getRound().setUIListener(getGamePane().getMahjongGameUI().getUserInterfaceListener());
					model.getRound().setServerListener(new UpdateTarget() {
						@Override
						public void update(MahjongUpdate mahjongUpdate) {
							JSONObject obj = new JSONObject();
							try {
								obj.put("room",model.getUuid());
								obj.put("action", "event");
								obj.put("event", mahjongUpdate.toJSON());
							} catch (JSONException exc) {
								throw new RuntimeException(exc);
							}
							getUIParent().getModel().getClientUpdaterThread().sendPacket(new Packet(PacketType.MAHJONGACTION, Packet.ATTEMPT, obj));
						}
					});
				}
			default:
				//TODO: not all done here
				break;
			}
		}
	};
	
	public GameRoomUI(MainRoomUI UIParent, final GameRoom model) {
		this.UIParent = UIParent;
		this.model = model;
		model.addUpdateListener(ul);
		
		setLayout(new BorderLayout());
		
		playerListModel = new DefaultListModel<String>();
		for(String s : model.getPlayers()) {
			playerListModel.addElement("(P) " + s);
		}
		for(String s : model.getObservers()) {
			playerListModel.addElement(s);
		}
		
		JList<String> usernameList = new JList<String>(playerListModel);
		usernameList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		usernameList.setMinimumSize(new Dimension(150,500));
		
		FormLayout formLayout = new FormLayout("fill:pref:grow, 220px, 80px",
				"fill:pref:grow, pref:grow, 40px");
		CellConstraints cc = new CellConstraints();
		setLayout(formLayout);
		
		gamePane = new MahjongGamePane(getModel(), model.isObserver(), getUIParent().getModel().getSelfUsername());

		getGamePane().setPreferredSize(new Dimension(640,480));
		getGamePane().setMinimumSize(new Dimension(0,0));
		getGamePane().setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		add(getGamePane(), cc.xywh(1, 1, 1, 3));
		
		add(usernameList, cc.xywh(2,1,2,1));
		
		chatArea = new JTextArea(8, 100);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		chatArea.setWrapStyleWord(true);
		JScrollPane chatAreaScrollPane = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(chatAreaScrollPane, cc.xywh(2,2,2,1));
		
		chatField = new JTextField(20);
		chatField.addKeyListener(chatKeyListener);
		add(chatField, cc.xywh(2, 3, 1, 1));
		
		chatButton = new JButton("Post");
		chatButton.addActionListener(chatListener);
		add(chatButton, cc.xywh(3, 3, 1, 1));
	}
	
	private void updateChat() {
		//TODO: send chat message to server
	}

	public GameRoom getModel() {
		return model;
	}

	public MainRoomUI getUIParent() {
		return UIParent;
	}

	public void close() {
		getParent().remove(this);
	}

	public MahjongGamePane getGamePane() {
		return gamePane;
	}
}
