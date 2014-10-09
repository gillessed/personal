package net.gillessed.tablemahjong.client.swingui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import net.gillessed.tablemahjong.client.MainRoom;
import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateEvent.UpdateType;
import net.gillessed.tablemahjong.client.event.UpdateListener;
import net.gillessed.tablemahjong.swingui.TableMahjongFrame;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

import org.json.JSONException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class MainRoomUI extends JPanel {
	
	private final MainRoomUI selfReference = this;
	private final MainRoom model;
	
	private final JTextArea chatArea;
	private final JTextField chatField;
	private final JButton chatButton;
	private final JButton createGameButton;
	private final JTable gameTable;
	private final DefaultListModel<String> usernameListModel;
	private final TableMahjongFrame UIParent;
	
	private final UpdateListener ul = new UpdateListener() {
		@Override
		public void actionPerformed(UpdateEvent e) {
			switch(e.getType()) {
			case USERJOIN:
				try {
					String addElement = e.getEventData().getString(UpdateType.USERJOIN.getDataKeys().get(0));
					if(getModel().getSelfUsername().equals(addElement)) {
						addElement += " (me)";
					}
					usernameListModel.addElement(addElement);
				} catch (JSONException err) {
					throw new RuntimeException(err);
				}
				break;
			case USERQUIT:
				try {
					String removeElement = e.getEventData().getString(UpdateType.USERJOIN.getDataKeys().get(0));
					if(getModel().getSelfUsername().equals(removeElement)) {
						removeElement += " (me)";
					}
					usernameListModel.removeElement(removeElement);
				} catch (JSONException err) {
					throw new RuntimeException(err);
				}
				break;
			case KILL:
				for(Component c : getUIParent().getTabbedPanes().getComponents()) {
					if(c instanceof CreateGameRoomUI) {
						CreateGameRoomUI cgr = (CreateGameRoomUI)c;
						model.getCreateGameRooms().remove(cgr.getModel().getUuid());
						cgr.close();
					}
					if(c instanceof JoinGameRoomUI) {
						JoinGameRoomUI jgr = (JoinGameRoomUI)c;
						model.getJoinGameRooms().remove(jgr.getModel().getUuid());
						jgr.close();
					}
					if(c instanceof GameRoomUI) {
						GameRoomUI gr = (GameRoomUI)c;
						model.getGameRooms().remove(gr.getModel().getUuid());
						gr.close();
					}
				}
				getUIParent().getTabbedPanes().remove(selfReference);
				getUIParent().killMainRoom();
				getUIParent().closeLocalServerUpdateUI();
				getUIParent().setConnectedToServer(false);
				try {
					if(!e.getEventData().getBoolean("expected")) {
						JOptionPane.showMessageDialog(getUIParent().getFrame(), "The server has been shut down unexpectedly and so the connection on this side has also been closed.", "Notice", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (JSONException err) {
					throw new RuntimeException(err);
				}
				break;
			case CHAT:
				try {
					chatArea.append(e.getEventData().getString("message"));
				} catch (JSONException err) {
					throw new RuntimeException(err);
				}
				break;
			case CREATEGAME:
				try {
					final CreateGameRoomUI createGameRoomUI = new CreateGameRoomUI(selfReference, getModel().getCreateGameRooms().get(e.getEventData().getString("room")));
					getUIParent().addPane(createGameRoomUI, "Create Game", new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							createGameRoomUI.close();
						}
					});
				} catch (JSONException err) {
					throw new RuntimeException(err);
				}
				break;
			case JOINCREATEGAME:
				try {
					final JoinGameRoomUI cgrroomUI = new JoinGameRoomUI(selfReference, getModel().getJoinGameRooms().get(e.getEventData().getString("room")));
					getUIParent().addPane(cgrroomUI, "Open Challenge", new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cgrroomUI.close();
						}
					});
				} catch (JSONException err) {
					throw new RuntimeException(err);
				}
				break;
			case STARTGAME:
				try {
					String status = e.getEventData().getString("status");
					final GameRoomUI cgroomUI = new GameRoomUI(selfReference, getModel().getGameRooms().get(e.getEventData().getString("room")));
					getUIParent().addPane(cgroomUI, "Game In Progress (" + status + ")", new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							
						}
					});
				} catch (JSONException err) {
					throw new RuntimeException(err);
				}
				break;
			case FINALIZEGAME:
				break;
			case FREEZE:
				break;
			case GAMEACTION:
				break;
			case MESSAGE:
				break;
			case NEWROUND:
				break;
			case QUITCREATEGAME:
				break;
			case QUITGAME:
				break;
			case SIZEUPATE:
				break;
			case STEALMESSAGE:
				break;
			default:
				break;
			}
		}
	};
	
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
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_ENTER) {
				updateChat();
			}
		}
	};
	
	private ActionListener createGameListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			model.addCreateGameRoomToServer();
		}
	};
	
	private MouseListener joinCreateGameListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			int row = gameTable.rowAtPoint(p);
			String roomUuid = model.getGameTableModel().getRoomUuid(row);
			CreateGameRoomBean bean = model.getGameTableModel().get(roomUuid);
			boolean okToJoin = true;
			if(roomUuid != null) {
				if(model.getCreateGameRooms().get(roomUuid) != null) {
						okToJoin = false;
				} else if(model.getJoinGameRooms().get(roomUuid) != null) {
						okToJoin = false;
				} else {
					okToJoin = true;
				}
			} else {
				okToJoin = false;
			}
			if(okToJoin) {
				model.joinCreateGameRoomToServer(roomUuid, bean);
			}
		}
		@Override
		public void mousePressed(MouseEvent paramMouseEvent) {
		}
		@Override
		public void mouseReleased(MouseEvent paramMouseEvent) {
		}
		@Override
		public void mouseEntered(MouseEvent paramMouseEvent) {
		}
		@Override
		public void mouseExited(MouseEvent paramMouseEvent) {
		}
		
	};

	public MainRoomUI(MainRoom model, TableMahjongFrame parent) {
		this.model = model;
		this.UIParent = parent;
		model.addUpdateListener(ul);
		
		setLayout(new BorderLayout());
		
		usernameListModel = new DefaultListModel<String>();
		for(String s : model.getUsernames()) {
			if(getModel().getSelfUsername().equals(s)) {
				s += " (me)";
			}
			usernameListModel.addElement(s);
		}
		
		JList<String> usernameList = new JList<String>(usernameListModel);
		usernameList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		usernameList.setMinimumSize(new Dimension(150,500));
		
		FormLayout formLayout = new FormLayout("pref:grow, 40dlu",
				"pref, pref:grow, pref, pref");
		CellConstraints cc = new CellConstraints();
		JPanel gamesListAndButtonsAndchatPanel = new JPanel(formLayout);
		
		JPanel gameButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		createGameButton = new JButton("Create Game");
		getCreateGameButton().addActionListener(createGameListener);
		gameButtonPanel.add(getCreateGameButton());
		gameButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		gamesListAndButtonsAndchatPanel.add(gameButtonPanel, cc.xyw(1, 1, 2));
		
		gameTable = new JTable(model.getGameTableModel());
		gameTable.setMinimumSize(new Dimension(400,250));
		gameTable.addMouseListener(joinCreateGameListener);
		gamesListAndButtonsAndchatPanel.add(gameTable, cc.xyw(1, 2, 2));
		
		chatArea = new JTextArea(16, 100);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		chatArea.setWrapStyleWord(true);
		JScrollPane chatAreaScrollPane = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		gamesListAndButtonsAndchatPanel.add(chatAreaScrollPane, cc.xyw(1,3,2));
		
		chatField = new JTextField(20);
		chatField.addKeyListener(chatKeyListener);
		gamesListAndButtonsAndchatPanel.add(chatField, cc.xy(1, 4));
		
		chatButton = new JButton("Post");
		chatButton.addActionListener(chatListener);
		gamesListAndButtonsAndchatPanel.add(chatButton, cc.xy(2, 4));
		
		JScrollPane usernameListScrollPane = new JScrollPane(usernameList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, usernameListScrollPane, gamesListAndButtonsAndchatPanel);
		splitPane.setDividerLocation(150);
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(0.0);
		add(splitPane, BorderLayout.CENTER);
	}

	public MainRoom getModel() {
		return model;
	}
	
	private void updateChat() {
		if(chatField.getText().length() > 0) {
			model.updateChatToServer(chatField.getText());
			chatField.setText("");
		}
	}

	public JButton getCreateGameButton() {
		return createGameButton;
	}

	public TableMahjongFrame getUIParent() {
		return UIParent;
	}
}
