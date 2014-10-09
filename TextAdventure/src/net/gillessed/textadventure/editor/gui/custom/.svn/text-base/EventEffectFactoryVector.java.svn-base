package net.gillessed.textadventure.editor.gui.custom;

import java.util.Vector;

import net.gillessed.textadventure.datatype.AreaTransitionEventEffect;
import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyEventEffect;
import net.gillessed.textadventure.datatype.EnvironmentEventEffect;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.EventEffect;
import net.gillessed.textadventure.datatype.ItemTradeEventEffect;
import net.gillessed.textadventure.datatype.TextEventEffect;
import net.gillessed.textadventure.datatype.VariableEventEffect;
import net.gillessed.textadventure.editor.gui.propertypanel.AreaTransitionEventEffectCreatorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.CreatorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.EnemyEventEffectCreatorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.EnvironmentEventEffectCreatorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.EventEffectAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.ItemTradeEventEffectCreatorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.TextEventEffectCreatorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.VariableEventEffectCreatorPanel;

@SuppressWarnings("serial")
public class EventEffectFactoryVector extends Vector<EventEffectFactory>{
	private Vector<Class<?>> classList = new Vector<>();
	public EventEffectFactoryVector() {
		add(new AreaTransitionEventEffectFactory());
		classList.add(AreaTransitionEventEffect.class);
		add(new EnvironmentEventEffectFactory());
		classList.add(EnvironmentEventEffect.class);
		add(new EnemyEventEffectFactory());
		classList.add(EnemyEventEffect.class);
		add(new ItemTradeEventEffectFactory());
		classList.add(ItemTradeEventEffect.class);
		add(new TextEventEffectFactory());
		classList.add(TextEventEffect.class);
		add(new VariableEventEffectFactory());
		classList.add(VariableEventEffect.class);
	}
	
	public EventEffectFactory getFactory(Class<?> clazz) {
		return get(classList.indexOf(clazz));
	}
}

class AreaTransitionEventEffectFactory extends EventEffectFactory {
	@Override
	public EventEffect<? extends DataType> createEventEffect(Event parentEvent) {
		return new AreaTransitionEventEffect(parentEvent);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CreatorPanel<EventEffect<? extends DataType>> createCreatorPanel(
			EventEffect<? extends DataType> model, EventEffectAdderPanel parent) {
		return (CreatorPanel)
				new AreaTransitionEventEffectCreatorPanel((AreaTransitionEventEffect)model, parent);
	}
	
	@Override
	public String toString() {
		return "Area Transition";
	}
}

class EnvironmentEventEffectFactory extends EventEffectFactory {

	@Override
	public EventEffect<? extends DataType> createEventEffect(Event parentEvent) {
		return new EnvironmentEventEffect(parentEvent);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CreatorPanel<EventEffect<? extends DataType>> createCreatorPanel(
			EventEffect<? extends DataType> model, EventEffectAdderPanel parent) {
		return (CreatorPanel)
				new EnvironmentEventEffectCreatorPanel((EnvironmentEventEffect)model, parent);
	}

	@Override
	public String toString() {
		return "Environment Change";
	}
	
}

class EnemyEventEffectFactory extends EventEffectFactory {
	@Override
	public EventEffect<? extends DataType> createEventEffect(Event parentEvent) {
		return new EnemyEventEffect(parentEvent);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CreatorPanel<EventEffect<? extends DataType>> createCreatorPanel(
			EventEffect<? extends DataType> model, EventEffectAdderPanel parent) {
		return (CreatorPanel)
				new EnemyEventEffectCreatorPanel((EnemyEventEffect)model, parent);
	}

	@Override
	public String toString() {
		return "Fight Enemy";
	}
	
}

class ItemTradeEventEffectFactory extends EventEffectFactory {

	@Override
	public EventEffect<? extends DataType> createEventEffect(Event parentEvent) {
		return new ItemTradeEventEffect(parentEvent);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CreatorPanel<EventEffect<? extends DataType>> createCreatorPanel(
			EventEffect<? extends DataType> model, EventEffectAdderPanel parent) {
		return (CreatorPanel)
				new ItemTradeEventEffectCreatorPanel((ItemTradeEventEffect)model, parent);
	}

	@Override
	public String toString() {
		return "Item Trade";
	}
	
}

class TextEventEffectFactory extends EventEffectFactory {

	@Override
	public EventEffect<? extends DataType> createEventEffect(Event parentEvent) {
		return new TextEventEffect(parentEvent);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CreatorPanel<EventEffect<? extends DataType>> createCreatorPanel(
			EventEffect<? extends DataType> model, EventEffectAdderPanel parent) {
		return (CreatorPanel)
				new TextEventEffectCreatorPanel((TextEventEffect)model, parent);
	}

	@Override
	public String toString() {
		return "Text";
	}
}

class VariableEventEffectFactory extends EventEffectFactory {

	@Override
	public EventEffect<? extends DataType> createEventEffect(Event parentEvent) {
		return new VariableEventEffect(parentEvent);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CreatorPanel<EventEffect<? extends DataType>> createCreatorPanel(
			EventEffect<? extends DataType> model, EventEffectAdderPanel parent) {
		return (CreatorPanel)
				new VariableEventEffectCreatorPanel((VariableEventEffect)model, parent);
	}

	@Override
	public String toString() {
		return "Variable";
	}
}