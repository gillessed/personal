package net.gillessed.tablemahjong.client.swingui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.gillessed.tablemahjong.client.JoinGameRoom;
import net.gillessed.tablemahjong.client.event.UpdateEvent;
import net.gillessed.tablemahjong.client.event.UpdateListener;

import org.json.JSONException;

@SuppressWarnings("serial")
public class JoinGameRoomUI extends JPanel {
	
	private final JoinGameRoom model;

	private final JLabel message;

	private final MainRoomUI UIParent;
	
	public JoinGameRoomUI(MainRoomUI parent, JoinGameRoom model) {
		this.UIParent = parent;
		this.model = model;
		message = new JLabel(model.getMessage());
		add(message);

		model.addUpdateListener(new UpdateListener() {
			@Override
			public void actionPerformed(UpdateEvent e) {
				switch(e.getType()) {
				case SIZEUPATE:
					String messageString;
					try {
						messageString = e.getEventData().getString("message");
					} catch (JSONException exc) {
						throw new RuntimeException(exc);
					}
					message.setText(messageString);
					repaint();
					break;
				case KILL :
					closeUI();
					break;
				default:
					break;	
				}
			}
		});
	}

	public MainRoomUI getUIParent () {
		return UIParent;
	}

	public JoinGameRoom getModel() {
		return model;
	}

	public void close() {
		model.leave();
		closeUI();
	}

	public void closeUI() {
		getParent().remove(this);
	}
}
