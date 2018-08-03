package remvn.recard.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import remvn.recard.card.Card;
import remvn.recard.card.CardManager;
import remvn.recard.card.CardPrice;
import remvn.recard.card.CardType;
import remvn.recard.card.Request;
import remvn.recard.card.RequestType;

public class ListenerGUI implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		InventoryHolder holder = e.getInventory().getHolder();
		if(holder instanceof GUIHolder) {
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if(!checkNullorAir(item)) {
				return;
			}
			Player p = (Player) e.getWhoClicked();
			if(holder instanceof SelectCardTypeHolder) {
				CardType cardtype = CardType.getByName(ChatColor.stripColor(getDisplayName(item)));
				if(cardtype != null) {
					StatusCard statusCard = Status.getByCardType(cardtype, Status.statusCards);
					if(statusCard.status)
					GUIManager.openGUISelectCardPrice(p, cardtype); else {
						if(cardtype.equals(CardType.VIETTEL) || cardtype.equals(CardType.VINAPHONE) || cardtype.equals(CardType.MOBIFONE))
						if(e.isShiftClick()) {
							GUIManager.openGUISelectCardPrice(p, cardtype);
						}
					}
				}
			}
			if(holder instanceof SelectCardPriceHolder) {
				String str = ChatColor.stripColor(getDisplayName(item));
				String[] array = str.split(" ");
				CardPrice cardPrice = CardPrice.getByPrice(array[0]);
//				System.out.println(cardPrice.name());
				if(cardPrice != null) {
					CardType cardType = CardType.getByName(array[1]);
					if(cardType != null) {
						Card card = new Card("", "", cardType, cardPrice);
						Request request = new Request(p, card, RequestType.SERI);
						CardManager.addRequest(p, p.getUniqueId(), request);
						p.closeInventory();
					}
				}
			}
		}
	}
	
	public static boolean checkNullorAir(ItemStack item) {
		if(item == null || item.getType().equals(Material.AIR)) return false;
		return true;
	}
	
	public static String getDisplayName(ItemStack item) { 
		if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return "";
		ItemMeta meta = item.getItemMeta();
		return meta.getDisplayName();
	}
}
