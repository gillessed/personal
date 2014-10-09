package net.gillessed.textadventure.deletelistener;

import java.io.Serializable;

import net.gillessed.textadventure.datatype.DataType;

public interface DeleteListener extends Serializable {
	public void deleted(DataType deleted);
}
