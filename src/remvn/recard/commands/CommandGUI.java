package remvn.recard.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import remvn.recard.Main;
import remvn.recard.card.Card;
import remvn.recard.card.CardManager;
import remvn.recard.card.CardPrice;
import remvn.recard.card.CardType;
import remvn.recard.card.Request;
import remvn.recard.card.RequestType;
import remvn.recard.config.Config;
import remvn.recard.gui.GUIManager;
import remvn.recard.gui.Status;

public class CommandGUI implements CommandExecutor {
	static Main main = Main.getIns();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("recard.admin")) {
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				Config.loadFileF();
				sender.sendMessage("§a[ReCard] Da reload config");
				Status.sendMessagesToServer(Status.getStatusMessages());
				return true;
			}
		}
		if(sender.hasPermission("recard.napthe")) 
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				Status.updateStatus();
				GUIManager.openGUISelectCardType(p);
				return true;
			}
			if(args.length == 2) {
				Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
					CardType cardType = CardType.getByName(args[0].toUpperCase());
					CardPrice cardPrice = CardPrice.getByPrice(args[1]);
					if(cardType != null && cardPrice != null) {
						Card card = new Card("", "", cardType, cardPrice);
						Request request = new Request(p, card, RequestType.SERI);
						CardManager.addRequest(p, p.getUniqueId(), request);
					}
				});
				return true;
			}
		}
		sender.sendMessage("§c§lSử dụng: §c/napthe mở menu nạp thẻ");
		sender.sendMessage("§c§lHoặc: §c/napthe <LOẠI THẺ> <MỆNH GIÁ>");
		sender.sendMessage("§cLOẠI THẺ: §b" + CardType.getListCardType());
		sender.sendMessage("§cMỆNH GIÁ: §b" + CardPrice.getListCardPrice());
		return true;
	}

}
