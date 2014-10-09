package net.gillessed.textadventure.datatype;

import java.util.ArrayList;
import java.util.List;

import net.gillessed.textadventure.utils.ListenerList;

public class ItemTradeEventEffect extends EventEffect<DataType> {
	private static final long serialVersionUID = 6333913394787568134L;
	
	private long gold;
	private final List<ItemBundle> itemBundles;
	
	public ItemTradeEventEffect(Event parent) {
		super(null, parent);
		gold = 0;
		itemBundles = new ListenerList<>();
	}

	public List<ItemBundle> getItemBundles() {
		return itemBundles;
	}
	
	public long getGold() {
		return gold;
	}
	
	public void setGold(long gold) {
		this.gold = gold;
	}
	
	@Override
	public void deleted() {
		List<ItemBundle> temp = new ArrayList<>(itemBundles);
		for(ItemBundle b : temp) {
			b.deleted();
		}
		super.deleted();
	}
}
