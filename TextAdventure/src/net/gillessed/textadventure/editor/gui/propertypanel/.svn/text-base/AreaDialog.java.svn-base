package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.World;
import net.gillessed.textadventure.editor.gui.propertypanel.GridCellCanvas.Mode;

@SuppressWarnings("serial")
public class AreaDialog extends JFrame {
	private static DataType returnDataType = null;
	private static Lock lock = new ReentrantLock();
	private static Condition chosen = lock.newCondition();
	
	public static DataType showAreaChooser(final Mode mode, final World parentWorld, final DataType currentArea) {
		new AreaDialog(mode, parentWorld, currentArea);
		DataType dataType = null;
		try {
			lock.lock();
			chosen.await();
			dataType = returnDataType;
			returnDataType = null;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
		return dataType;
	}
	
	private final JComboBox<?> areaBox;
	private final JButton okButton;
	private final JButton cancelButton;
	
	public AreaDialog(Mode mode, World parentWorld, DataType currentArea) {
		setTitle("Area Chooser");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					lock.lock();
					chosen.signal();
				} finally {
					lock.unlock();
				}
			}
		});
		Container panel = getContentPane();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		List<? extends DataType> areas = null;
		if(mode == Mode.FRIENDLY) {
			areas = DataType.getAllFriendlyAreas(parentWorld, (FriendlyArea)currentArea);
		} else if(mode == Mode.ENEMY) {
			areas = DataType.getAllEnemyAreas(parentWorld, (EnemyArea)currentArea);
		}
		areaBox = new JComboBox<>(areas.toArray(new DataType[0]));
		panel.add(new JLabel("Area: "));
		panel.add(areaBox);
		panel.add(Box.createHorizontalStrut(5));
		
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				returnDataType = (DataType)areaBox.getSelectedItem();
				try {
					lock.lock();
					chosen.signal();
				} finally {
					lock.unlock();
				}
				dispose();
			}
		});
		panel.add(okButton);
		panel.add(Box.createHorizontalStrut(5));
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lock.lock();
					chosen.signal();
				} finally {
					lock.unlock();
				}
				dispose();
			}
		});
		panel.add(cancelButton);
		setLocation(10,10);
		pack();
		setVisible(true);
	}
}
