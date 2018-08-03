package main.java.remvn.recard.card;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CardPrice {
	
	CP_10000(),
	CP_20000(),
	CP_30000(),
	CP_50000(),
	CP_100000(),
	CP_200000(),
	CP_300000(),
	CP_500000(),
	CP_1000000(),
	;

	
	public int getPrice() {
		String price = this.name().split("_")[1];
		return Integer.valueOf(price);
	}
	
	static DecimalFormat df = new DecimalFormat("###,###");
	public ItemStack getDisplayItem(CardType cardType) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = Arrays.asList("§cChọn sai mệnh giá có thể mất thẻ!!");
		meta.setDisplayName("§b" + df.format(getPrice()) + " " + cardType.name());
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static CardPrice getByPrice(String price) {
		try {
			price = df.parse(price).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for(CardPrice cardPrice : CardPrice.values()) {
			if(String.valueOf(cardPrice.getPrice()).equals(price)) {
				return cardPrice;
			}
		}
		return null;
	}
	
	public static String getListCardPrice() {
		String list = "";
		for(CardPrice cardPrice : CardPrice.values()) {
			list += cardPrice.getPrice() + " ";
		}
		return list;
	}
	
}
