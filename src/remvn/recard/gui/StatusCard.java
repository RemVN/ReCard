package remvn.recard.gui;

import remvn.recard.card.CardType;

public class StatusCard {


	
	public CardType cardType;
	public boolean status;
	
	public StatusCard(CardType cardType, boolean status) {
		this.cardType = cardType;
		this.status = status;
	}
	
	
}
