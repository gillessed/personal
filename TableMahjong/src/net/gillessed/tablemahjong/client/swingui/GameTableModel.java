package net.gillessed.tablemahjong.client.swingui;

import javax.swing.table.AbstractTableModel;

import net.gillessed.tablemahjong.client.RoomPairMap;
import net.gillessed.tablemahjong.threadspace.CreateGameRoomBean;

@SuppressWarnings("serial")
public class GameTableModel extends AbstractTableModel {
	
	private final RoomPairMap model;

	public GameTableModel(RoomPairMap roomPairMap) {
		model = roomPairMap;
	}
	
	public void put(String uuid, CreateGameRoomBean bean) {
		model.put(uuid, bean);
		fireTableRowsInserted(model.size(), model.size());
		fireTableDataChanged();
	}
	
	public CreateGameRoomBean get(String uuid) {
		return model.get(uuid);
	}
	
	public CreateGameRoomBean remove(String uuid) {
		CreateGameRoomBean bean = model.remove(uuid);
		fireTableRowsDeleted(model.size(), model.size());
		fireTableDataChanged();
		return bean;
	}

	@Override
	public int getColumnCount() {
		return CreateGameRoomBean.PARAMS;
	}
	
	@Override
	public String getColumnName(int column) {
		switch(column) {
		case 0:
			return "Game Creator";
		case 1:
			return "Rule Set";
		case 2:
			return "Starting money";
		case 3:
			return "Description";
		case 4:
			return "Status";
		default:
			return null;
		}
	}

	@Override
	public int getRowCount() {
		return model.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex > getColumnCount() || columnIndex < 0 || rowIndex < 0 || rowIndex > getRowCount()) {
			return null;
		}
		CreateGameRoomBean bean = model.getBeans().get(rowIndex);
		switch(columnIndex) {
		case 0:
			return bean.creator;
		case 1:
			return bean.ruleSet;
		case 2:
			return bean.startingMoney;
		case 3:
			return bean.description;
		case 4:
			return bean.status;
		default:
			return null;
		}
	}
	
	public String getRoomUuid(int rowIndex) {
		if(rowIndex < 0 || rowIndex > getRowCount()) {
			return null;
		}
		return model.getUuids().get(rowIndex);
	}
}
