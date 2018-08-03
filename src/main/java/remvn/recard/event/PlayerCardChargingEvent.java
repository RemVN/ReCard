package main.java.remvn.recard.event;

import main.java.remvn.recard.card.Result;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.remvn.recard.card.Card;

public class PlayerCardChargingEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
    public OfflinePlayer op;
    public Card card;
    public Result result;
    public int point;
//    private boolean cancelled;
    
    public PlayerCardChargingEvent(OfflinePlayer op, Card card, Result result, int point) {
    	super();
    	this.op = op;
    	this.card = card;
    	this.result = result;
    	this.point = point;
	}
	
	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void setCancelled(boolean arg0) {
	}

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
