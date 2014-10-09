package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.Response;

@SuppressWarnings("serial")
public class ResponseAdderPanel extends AdderPanel<Response, ResponseCreatorPanel>{

	private final Event parentEvent;
	
	public ResponseAdderPanel(List<Response> model, Event parentEvent, JPanel parent) {
		super(model, parent, "Reponses");
		this.parentEvent = parentEvent;
		createGUI();
	}

	@Override
	protected ResponseCreatorPanel getCreatorPanel(Response s, int index) {
		return new ResponseCreatorPanel(s, this);
	}

	@Override
	protected Response generateNewElement(int index) {
		return new Response(parentEvent);
	}

}
