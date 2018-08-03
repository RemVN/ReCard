package remvn.recard.card;

import org.bukkit.OfflinePlayer;

public class Request {
	
	public OfflinePlayer op;
	public Card card;
	public RequestType requestType;
	
	public Request(OfflinePlayer op, Card card, RequestType requestType) {
		this.op = op;
		this.card = card;
		this.requestType = requestType;
	}
	
}
