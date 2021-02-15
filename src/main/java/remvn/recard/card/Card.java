package remvn.recard.card;

import remvn.recard.config.Config;
import remvn.recard.config.ConfigType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Card {
	
	public String seri;
	public String pin;
	public CardType cardtype;
	public CardPrice cardprice;

	public Card(String seri, String pin, CardType cardtype, CardPrice cardprice) {
		this.seri  = seri;
		this.pin = pin;
		this.cardtype = cardtype;
		this.cardprice = cardprice;
	}
	
	public Map<String, String> getMap(String user) {
		Map<String, String> map = new HashMap<>();
		map.put("merchant_id", Config.getValue(ConfigType.MERCHANT_ID));
		map.put("api_user", Config.getValue(ConfigType.API_USER));
		map.put("api_password", Config.getValue(ConfigType.API_PASSWORD));
		map.put("pin", this.pin);
		map.put("seri", this.seri);
		map.put("card_type", String.valueOf(this.cardtype.id));
		map.put("price_guest", String.valueOf(this.cardprice.getPrice()));
		map.put("note", getNote(user));
		return map;
	}
	
    public String createRequestUrl(String user) {
		return getMap(user).entrySet().stream().map(x -> x.getKey() + "=" + x.getValue())
				.collect(Collectors.joining("&"));
    }

	public String getNote(String user) {
		return user + " from " + Config.getValue(ConfigType.SERVER);
	}
}
