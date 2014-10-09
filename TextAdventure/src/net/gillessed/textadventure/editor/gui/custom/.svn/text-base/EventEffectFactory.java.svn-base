package net.gillessed.textadventure.editor.gui.custom;


import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.EventEffect;
import net.gillessed.textadventure.editor.gui.propertypanel.CreatorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.EventEffectAdderPanel;

public abstract class EventEffectFactory {
	public abstract EventEffect<? extends DataType> createEventEffect(Event parentEvent);
	public abstract CreatorPanel<EventEffect<? extends DataType>> 
			createCreatorPanel(EventEffect<? extends DataType> model, EventEffectAdderPanel parent);
	public abstract String toString();
}
