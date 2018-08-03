package main.java.remvn.recard.utils;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtils {
	
	public static int getEnchantment(Enchantment type, ItemStack item) {
		if(!checkNullorAir(item)) return 0;
		if(!item.hasItemMeta()) return 0;
		ItemMeta meta = item.getItemMeta();
		if(!meta.hasEnchant(type)) {
			return 0;
		} else {
			return meta.getEnchantLevel(type);
		}
	}
	
	public static void addGlow(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
	}
	
	public static ItemStack createDec(int type) {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		item.setItemMeta(meta);
		item.setDurability((short) type);
		return item;
	}
	
	public static ItemStack createItemStack(Material type, String name) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static void setDisplayName(ItemStack item, String displayName) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
	}
	
	public static void setLore(ItemStack item, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static void addLore(ItemStack item, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore1 = new ArrayList<String>();
		if(meta.hasLore()) lore1.addAll(meta.getLore());
		lore1.addAll(lore);
		meta.setLore(lore1);
		item.setItemMeta(meta);
	}
	
	public static boolean checkNullorAir(ItemStack item) {
		if(item == null || item.getType().equals(Material.AIR)) return false;
		return true;
	}
	
	public static void addItemFlag(ItemStack item, ItemFlag flag) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
	}
	
}
