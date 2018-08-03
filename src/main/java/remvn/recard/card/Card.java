package remvn.recard.card;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import main.java.remvn.recard.config.ConfigType;
import main.java.remvn.recard.config.Config;
import remvn.recard.config.Config;
import remvn.recard.config.ConfigType;

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
	
	public String createForm(String user) {
		String json = "";
		JsonObject jo = new JsonObject();
		jo.addProperty("merchant_id", Config.getValue(ConfigType.MERCHANT_ID));
		jo.addProperty("api_user", Config.getValue(ConfigType.API_USER));
		jo.addProperty("api_password", Config.getValue(ConfigType.API_PASSWORD));
		jo.addProperty("pin", this.pin);
		jo.addProperty("seri", this.seri);
		jo.addProperty("card_type", this.cardtype.id);
		jo.addProperty("price_guest", this.cardprice.getPrice());
		jo.addProperty("note", "User:  " + user + " | Author: fb.com/RemrezeroVN");
		json = jo.toString();
		return json;
	}
	
	public Map<String, String> getMap(String user) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchant_id", Config.getValue(ConfigType.MERCHANT_ID));
		map.put("api_user", Config.getValue(ConfigType.API_USER));
		map.put("api_password", Config.getValue(ConfigType.API_PASSWORD));
		map.put("pin", this.pin);
		map.put("seri", this.seri);
		map.put("card_type", String.valueOf(this.cardtype.id));
		map.put("price_guest", String.valueOf(this.cardprice.getPrice()));
		map.put("note", "User:  " + user + " | Author: fb.com/RemrezeroVN");
		return map;
	}
	
    public String createRequestUrl(String user) {
        String url_params = "";
        for (Map.Entry<String, String> entry : getMap(user).entrySet()) {
            url_params = url_params == "" ? String.valueOf(url_params) + entry.getKey() + "=" + entry.getValue() : String.valueOf(url_params) + "&" + entry.getKey() + "=" + entry.getValue();
        }
        return url_params;
    }
}
