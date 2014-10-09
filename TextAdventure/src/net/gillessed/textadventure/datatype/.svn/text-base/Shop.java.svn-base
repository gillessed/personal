package net.gillessed.textadventure.datatype;

import java.util.List;

import javax.swing.Icon;

import net.gillessed.textadventure.utils.IconUtils;
import net.gillessed.textadventure.utils.ListenerList;

public class Shop extends DataType {
	private static final long serialVersionUID = 2005796300786589394L;
	
	private double buyFactor;
	private double sellFactor;
	private final List<ItemBundle> inventory;
	
	public Shop(DataType parent) {
		super(parent);
		inventory = new ListenerList<>();
	}

	public double getBuyFactor() {
		return buyFactor;
	}

	public void setBuyFactor(double buyFactor) {
		this.buyFactor = buyFactor;
	}

	public double getSellFactor() {
		return sellFactor;
	}

	public void setSellFactor(double sellFactor) {
		this.sellFactor = sellFactor;
	}

	public List<ItemBundle> getInventory() {
		return inventory;
	}

	@Override
	public Icon getIcon(int size) {
		return IconUtils.SHOP_ICON(size);
	}
}
