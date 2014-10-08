package com.gillessed.magic.state;

import com.gillessed.magic.permanent.Permanent;
import com.gillessed.player.Player;

public class Event {
	public enum EventType {
		PHASES_OUT,
		PHASES_IN,
		ENTERS_THE_BATTLEFIELD,
		LEAVE_THE_BATTEFIELD,
		DIES,
		EXILED,
		DRAW_A_CARD,
		DISCARDS_A_CARD,
		UNTAPS,
		TAPS,
		SHUFFLE_LIBRARY,
		CAST_SPELL,
		BEGINNING_OF_UPKEEP
	}
	
	public Card triggeringCard;
	public Permanent triggeringPermanent;
	public Player triggeringPlayer;
	
	public Event(EventType eventType) {
		
	}
}
