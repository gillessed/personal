package net.gillessed.textadventure.datatype;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;

public class ItemBundle extends DataType {
	private static final long serialVersionUID = 7389691372363509090L;
	
	private double itemAmount;
	private ItemDescription item;
	private final DeleteListener itemListener;
	public ItemBundle() {
		super(null);
		itemAmount = 0;
		itemListener = new DeleteListener() {
			private static final long serialVersionUID = -4198887943036790648L;

			@Override
			public void deleted(DataType deleted) {
				setItem(null);
			}
		};
	}

	public ItemDescription getItem() {
		return item;
	}

	public void setItem(ItemDescription item) {
		if(this.item != null) {
			this.item.removeDeleteListener(itemListener);
		}
		this.item = item;
		if(item != null) {
			item.addDeleteListener(itemListener);
		}
	}

	public double getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(double itemAmount) {
		this.itemAmount = itemAmount;
	}

	@Override
	public Icon getIcon(int size) {
		return null;
	}
}
