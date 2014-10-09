package net.gillessed.textadventure.datatype;

import javax.swing.Icon;

import net.gillessed.textadventure.deletelistener.DeleteListener;

public abstract class EventEffect<T extends DataType> extends DataType {
	private static final long serialVersionUID = 5846701393104778443L;

	private DeleteListener targetListener;
	protected T target;
	
	public EventEffect(T target, Event parent) {
		super(parent);
		this.target = target;
		targetListener = new DeleteListener() {
			private static final long serialVersionUID = -6586443999243105021L;

			@Override
			public void deleted(DataType deleted) {
				setTarget(null);
			}
		};
	}

	public T getTarget() {
		return target;
	}

	public void setTarget(T target) {
		if(this.target != null) {
			this.target.removeDeleteListener(targetListener);
		}
		this.target = target;
		if(target != null) {
			target.addDeleteListener(targetListener);
		}
	}

	@Override
	public Icon getIcon(int size) {
		return null;
	}
}
