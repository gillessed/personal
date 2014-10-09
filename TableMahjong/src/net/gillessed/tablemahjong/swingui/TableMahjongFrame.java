package net.gillessed.tablemahjong.swingui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import net.gillessed.tablemahjong.client.ClientUpdaterThread;
import net.gillessed.tablemahjong.client.MainRoom;
import net.gillessed.tablemahjong.client.RoomPairMap;
import net.gillessed.tablemahjong.client.swingui.GameRoomUI;
import net.gillessed.tablemahjong.client.swingui.MainRoomUI;
import net.gillessed.tablemahjong.mahjonggame.swingui.MahjongGamePane;
import net.gillessed.tablemahjong.server.SocketStream;
import net.gillessed.tablemahjong.server.logging.Logger;
import net.gillessed.tablemahjong.server.logging.OptionPaneAppender;
import net.gillessed.tablemahjong.server.packet.Packet;
import net.gillessed.tablemahjong.server.packet.Packet.PacketType;
import net.gillessed.tablemahjong.server.utils.ServerUtils;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TableMahjongFrame {

	private final TableMahjongFrame selfReference = this;
	
	private final JFrame frame;
	private final JTabbedPane tabbedPanes;
	
	private final JMenuItem quitMenuItem;
	
	private final JMenuItem onlineConnectMenuItem;
	private final JMenuItem localConnectMenuItem;
	private final JMenuItem createLocalServerMenuItem;
	private final JMenuItem killLocalServerMenuItem;
	
	private final JMenuItem debugServerMenuItem;
	private final JMenuItem debugLocalMenuItem;
	private final JMenuItem simulateGameMenuItem;
	private final JMenuItem reloadGraphicsMenuItem;
	
	private boolean isHostingLocalServer;
	private boolean isConnectedToServer;
	
	private MainRoom mainRoom;
	
	private ActionListener quitListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			exitFromQuitOrClose();
		}
	};
	
	private ActionListener createLocalServerListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			NewLocalServerDialog dialog = new NewLocalServerDialog(selfReference);
			dialog.show();
		}
	};
	
	private ActionListener killLocalServerListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(!mainRoom.isLocalServer()) {
				throw new RuntimeException("You shouldn't try to kill a local server which isn't one.");
			}
			mainRoom.sendKillToServer();
			mainRoom.close(true);
			closeLocalServerUpdateUI();
		}
	};
	
	private ActionListener connectLocalServerListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			ConnectLocalServerDialog dialog = new ConnectLocalServerDialog(selfReference);
			dialog.show();
		}
	};
	
	private ActionListener debugServerListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(isConnectedToServer) {
				mainRoom.getClientUpdaterThread().sendDebugPacket("main-thread-space", "create-game-thread-spaces");
			} else {
				Logger.getLogger().info("Tried to debug server, but not connected to any.");
			}
		}
	};
	
	private ActionListener debugLocalListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(isConnectedToServer) {
				Logger.getLogger().info("*** Start Local Debug ***");
				mainRoom.debug();
				Logger.getLogger().info("*** End Local Debug ***");
			} else {
				Logger.getLogger().info("Not connected to any server at the moment.");
			}
		}
	};
	
	private ActionListener simluateGameListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(isConnectedToServer) {
				Logger.getLogger().info("*** SimulateGame ***");
				Object[] possibilities = mainRoom.getUsernames().toArray();
				String s = (String)JOptionPane.showInputDialog(
				                    frame,
				                    "Make which user creator:",
				                    "Customized Dialog",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    possibilities,
				                    possibilities[0]);

				//If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
					mainRoom.getClientUpdaterThread().sendSimulatePacket("game", s);
				}
				Logger.getLogger().info("*** End Local Debug ***");
			} else {
				Logger.getLogger().info("Not connected to any server at the moment.");
			}
		}
	};
	
	private ActionListener reloadGraphicsListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < tabbedPanes.getComponentCount(); i++) {
				if(tabbedPanes.getComponent(i) instanceof GameRoomUI) {
					MahjongGamePane mgp = ((GameRoomUI)tabbedPanes.getComponent(i)).getGamePane();
					mgp.loadUIProperties();
				}
			}
		}
	};
	
	private WindowListener closeFrameListener = new WindowListener() {
		@Override
		public void windowOpened(WindowEvent e) {
		}
		@Override
		public void windowClosing(WindowEvent e) {
			exitFromQuitOrClose();
		}
		@Override
		public void windowClosed(WindowEvent e) {
		}
		@Override
		public void windowIconified(WindowEvent e) {
		}
		@Override
		public void windowDeiconified(WindowEvent e) {
		}
		@Override
		public void windowActivated(WindowEvent e) {
		}
		@Override
		public void windowDeactivated(WindowEvent e) {
		}
	};
	
	public TableMahjongFrame() {
		isHostingLocalServer = false;
		isConnectedToServer = false;
		
		frame = new JFrame();
		frame.setTitle("Table Mahjong");
		frame.setSize(1000,800);
		frame.setLocation(50, 50);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(closeFrameListener);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(quitListener);
		fileMenu.add(quitMenuItem);
		menuBar.add(fileMenu);
		JMenu connectMenu = new JMenu("Server");
		onlineConnectMenuItem = new JMenuItem("Connect Online");
		connectMenu.add(onlineConnectMenuItem);
		
		localConnectMenuItem = new JMenuItem("Connect Local");
		localConnectMenuItem.addActionListener(connectLocalServerListener);
		connectMenu.add(localConnectMenuItem);
		
		createLocalServerMenuItem = new JMenuItem("Local server");
		createLocalServerMenuItem.addActionListener(createLocalServerListener);
		connectMenu.add(createLocalServerMenuItem);
		
		killLocalServerMenuItem = new JMenuItem("Kill Server");
		killLocalServerMenuItem.setEnabled(false);
		killLocalServerMenuItem.addActionListener(killLocalServerListener);
		connectMenu.add(killLocalServerMenuItem);
		
		menuBar.add(connectMenu);
		
		if(Boolean.parseBoolean(System.getProperty("debug", "false"))) {
			JMenu debugMenu = new JMenu("Debug");
			debugServerMenuItem = new JMenuItem("Debug Server");
			debugServerMenuItem.addActionListener(debugServerListener);
			debugLocalMenuItem = new JMenuItem("Debug Local");
			debugLocalMenuItem.addActionListener(debugLocalListener);
			simulateGameMenuItem = new JMenuItem("Simulate Game");
			simulateGameMenuItem.addActionListener(simluateGameListener);
			reloadGraphicsMenuItem = new JMenuItem("Reload gameui.properties");
			reloadGraphicsMenuItem.addActionListener(reloadGraphicsListener);
			reloadGraphicsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
			debugMenu.add(debugServerMenuItem);
			debugMenu.add(debugLocalMenuItem);
			debugMenu.add(simulateGameMenuItem);
			debugMenu.add(reloadGraphicsMenuItem);
			menuBar.add(debugMenu);
		} else {
			debugServerMenuItem = null;
			debugLocalMenuItem = null;
			simulateGameMenuItem = null;
			reloadGraphicsMenuItem = null;
		}
		
		getFrame().setJMenuBar(menuBar);
		
		tabbedPanes = new JTabbedPane();
		
		Container c = frame.getContentPane();
		c.add(tabbedPanes);
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public void loginToLocal(String host, int port, String selfUsername, boolean isLocalServer) {
		try {
			Socket socket = new Socket(host, port);
			SocketStream stream = new SocketStream(socket);
			JSONObject obj = new JSONObject();
			obj.put("username", selfUsername);
			Packet loginPacket = new Packet(PacketType.LOGIN, Packet.ATTEMPT, obj);
			stream.getOut().println(loginPacket.toString());
			Packet response = ServerUtils.waitForResponse(stream);
			Logger.getLogger().pushAppender(new OptionPaneAppender(getFrame()));
			boolean loginSuccess = false;
			if(response == null) {
				Logger.getLogger().debug("Server login attempt timed out. Please try again later");
			} else {
				if(response.getStatusCode() == Packet.SUCCESS) {
					loginSuccess = true;
					Logger.getLogger().debug("Successfully logged in.");
				} else if(response.getStatusCode() == Packet.FAILURE) {
					Logger.getLogger().debug("Login failed: " + response.getMessageString("failure-message"));
				}
			}
			Logger.getLogger().popAppender();
			if(loginSuccess) {
				Packet mainRoomPacket = ServerUtils.waitForResponse(stream);
				JSONObject messageJSON = mainRoomPacket.getMessage();
				String mainRoomUUID = messageJSON.getString("main-room-uuid");
				JSONArray usernamesJSON = messageJSON.getJSONArray("user-array");
				List<String> usernames = new ArrayList<String>();
				for(int i = 0; i < usernamesJSON.length(); i++) {
					JSONObject usernameJSON = usernamesJSON.getJSONObject(i);
					String tempUsername = usernameJSON.getString("username");
					usernames.add(tempUsername);
				}
				JSONArray openChallengesJSON = messageJSON.getJSONArray("open-challenges");
				RoomPairMap openChallenges = new RoomPairMap();
				for(int i = 0; i < openChallengesJSON.length(); i++) {
					JSONObject challengeJSON = openChallengesJSON.getJSONObject(i);
					openChallenges.put(challengeJSON.getString("room"),
							new CreateGameRoomBean(challengeJSON.getJSONObject("bean")));
				}
				final MainRoom mainRoom = new MainRoom(mainRoomUUID, usernames, selfUsername, isLocalServer, openChallenges);
				new ClientUpdaterThread(stream, mainRoom).start();
				
				final MainRoomUI mainRoomUI = new MainRoomUI(mainRoom, this);
				String title;
				if(isHostingLocalServer) {
					title = "Hosting Local Server at " + host + ":" + port;
				} else {
					title = "Connected to Local Server at " + host + ":" + port;
				}
				addPane(mainRoomUI, title, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						mainRoom.close(true);
					}
				});
				this.mainRoom = mainRoom;
				setConnectedToServer(true);
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void createLocalServerUpdateUI() {
		createLocalServerMenuItem.setEnabled(false);
		setHostingLocalServer(true);
		killLocalServerMenuItem.setEnabled(true);
	}

	public void closeLocalServerUpdateUI() {
		createLocalServerMenuItem.setEnabled(true);
		setHostingLocalServer(false);
		killLocalServerMenuItem.setEnabled(false);
	}

	public void setHostingLocalServer(boolean isHostingLocalServer) {
		this.isHostingLocalServer = isHostingLocalServer;
	}

	public boolean isHostingLocalServer() {
		return isHostingLocalServer;
	}

	public JTabbedPane getTabbedPanes() {
		return tabbedPanes;
	}
	
	private void exitFromQuitOrClose() {
		if(mainRoom != null) {
			mainRoom.close(true);
		}
		frame.setVisible(false);
		frame.dispose();
		System.exit(0);
	}

	public MainRoom getMainRoom() {
		return mainRoom;
	}

	public void setConnectedToServer(boolean isConnectedToServer) {
		this.isConnectedToServer = isConnectedToServer;
		localConnectMenuItem.setEnabled(!isConnectedToServer);
		onlineConnectMenuItem.setEnabled(!isConnectedToServer);
	}

	public boolean isConnectedToServer() {
		return isConnectedToServer;
	}

	public void killMainRoom() {
		mainRoom = null;
	}
	
	public void addPane(Component pane, String title, ActionListener closeActionListener) {
		tabbedPanes.addTab("", pane);
		tabbedPanes.setSelectedComponent(pane);
		TabbedPaneHeader tph = new TabbedPaneHeader(title);
		getTabbedPanes().setTabComponentAt(getTabbedPanes().indexOfComponent(pane), tph);
		tph.addCloseActionListener(closeActionListener);
	}
}
