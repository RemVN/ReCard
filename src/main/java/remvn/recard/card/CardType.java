package main.java.remvn.recard.card;

import java.util.Arrays;

import main.java.remvn.recard.config.ConfigType;
import main.java.remvn.recard.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import main.java.remvn.recard.config.Config;
import main.java.remvn.recard.gui.StatusCard;

public enum CardType {
	
	VIETTEL(1),
	MOBIFONE(2),
	VINAPHONE(3),
	GATE(4),
	VIETNAMMOBILE(5),
	ZING(6),
	MEGACARD(8),
	ONCASH(9);
	
	public int id;
	private CardType(int id) {
		this.id = id;
	}
	
	public ItemStack getDisplayItem(StatusCard statuscard) {
		ItemStack item = new ItemStack(Material.BOOK);
		if(statuscard.status) {
			ItemStackUtils.addGlow(item);
			ItemStackUtils.setLore(item, Arrays.asList("§aĐang hoạt động"));
		} else {
			ItemStackUtils.setLore(item, Arrays.asList("§cĐang bảo trì"));
			CardType cardType = statuscard.cardType;
			if(Boolean.valueOf(Config.getValue(ConfigType.AUTO)))
			if(cardType.equals(CardType.VIETTEL) || cardType.equals(CardType.VINAPHONE) || cardType.equals(CardType.MOBIFONE)) {
				ItemStackUtils.addLore(item, Arrays.asList("§aShift + Click §7để tiếp tục nạp", "§7nếu nạp khi đang bảo trì thì thẻ", "§7của bạn sẽ chuyển vào hệ thống, hết", "§7bảo trì thì thẻ sẽ tự động nạp", "§c§nNhớ giữ lại thẻ!!"));
			}
		}
		ItemStackUtils.setDisplayName(item, "§b" + this.name());
		return item;
	}
	
	public static CardType getByName(String name) {
		for(CardType cardType : CardType.values()) {
			if(cardType.name().equalsIgnoreCase(name)) return cardType;
		}
		return null;
	}
	
	public static String getListCardType() {
		String list = "";
		for(CardType cardtype : CardType.values()) {
			list += cardtype.name() + " ";
		}
		return list;
	}
	
}
