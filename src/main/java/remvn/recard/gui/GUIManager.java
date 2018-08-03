package remvn.recard.gui;

import java.util.Arrays;

import main.java.remvn.recard.card.CardPrice;
import main.java.remvn.recard.card.CardType;
import main.java.remvn.recard.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import main.java.remvn.recard.Main;
import remvn.recard.Main;
import remvn.recard.card.CardType;
import remvn.recard.utils.ItemStackUtils;

public class GUIManager {
	static Main main = Main.getIns();
	public static void openGUISelectCardType(Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			Inventory inv = Bukkit.createInventory(new SelectCardTypeHolder(),9, "§4§lChọn loại thẻ");
			int i = 1;
			for(CardType cardType : CardType.values()) {
				inv.setItem(i, cardType.getDisplayItem(Status.getByCardType(cardType, Status.statusCards)));
				i++;
			}
			ItemStack info = ItemStackUtils.createItemStack(Material.SIGN, "§aHãy chọn loại thẻ bạn muốn nạp");
			ItemStackUtils.setLore(info, Arrays.asList("§aSau đó chọn tiếp mệnh giá" , "", "§cDo nạp thẻ không ổn định nên sẽ xảy ra" , "§cnạp thẻ chậm hoặc không nạp được nên nhớ", "§cgiữ lại thẻ dù đã nạp được hay không"));
			inv.setItem(0, info);
			Bukkit.getScheduler().runTask(main, () -> {
				p.openInventory(inv);
			});	
		});
	}
	
	public static void openGUISelectCardPrice(Player p, CardType cardType) {
		Inventory inv = Bukkit.createInventory(new SelectCardPriceHolder(),9, "§4§lChọn mệnh giá thẻ " + cardType.name());
		int i = 0;
		for(CardPrice cardPrice : CardPrice.values()) {
			inv.setItem(i, cardPrice.getDisplayItem(cardType));
			i++;
		}
		p.openInventory(inv);
	}
}
