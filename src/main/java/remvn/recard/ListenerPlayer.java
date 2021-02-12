package remvn.recard;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.InventoryView;
import remvn.recard.card.CardManager;
import remvn.recard.card.CardType;
import remvn.recard.card.OfflineRequest;
import remvn.recard.card.Request;
import remvn.recard.card.RequestType;
import remvn.recard.card.Result;
import remvn.recard.config.Config;
import remvn.recard.config.ConfigType;
import remvn.recard.event.PlayerCardChargingEvent;
import remvn.recard.gui.Status;
import remvn.recard.log.Log;
import remvn.recard.utils.ExceptionType;
import remvn.recard.utils.PlayerUtils;

public class ListenerPlayer implements Listener {

	static Main main = Main.getIns();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		OfflinePlayer op = p.getPlayer();
		if (CardManager.requestMap.containsKey(p.getUniqueId())) {
//			System.out.println("nap the");
			UUID uuid = p.getUniqueId();
			Request request = CardManager.requestMap.get(uuid);
			if (request.requestType.equals(RequestType.NONE)) {
				return;
			}
			e.setCancelled(true);
			String value = e.getMessage();
			if (request.requestType.equals(RequestType.SERI)) {
				request.card.seri = value;
				request.requestType = RequestType.PIN;
				p.sendMessage("§aHãy tiếp tục nhập §emã thẻ");
				return;
			}
			if (request.requestType.equals(RequestType.PIN)) {
				request.card.pin = value;
				request.requestType = RequestType.NONE;
				
				CardType cardType = request.card.cardtype;
				if(cardType.equals(CardType.VIETTEL) || cardType.equals(CardType.VINAPHONE) || cardType.equals(CardType.MOBIFONE))
				if(!Status.getByCardType(request.card.cardtype, Status.statusCards).status) {
					if(Boolean.valueOf(Config.getValue(ConfigType.AUTO))) {
						OfflineRequest offlineRequest = new OfflineRequest(op.getUniqueId(), request.card, op.getName());
						OfflineRequest.addRequestToMap(offlineRequest);
						p.sendMessage("§bThẻ của bạn đã chuyển vào hệ thống, khi nào hết bảo trì thẻ sẽ tự động nạp");
						return;
					}
				}
				p.sendMessage("§aĐang tiến hành nạp thẻ, xin chờ...");
				String resultString = "";
				try {
					resultString = CardManager.post(request.card.createRequestUrl(p.getName()));
				} catch (IOException e1) {
					p.sendMessage(ExceptionType.IO.message);
					e1.printStackTrace();
				}
				Result result = Result.formJson(resultString);
				boolean mysql = Boolean.valueOf(Config.getValue(ConfigType.MYSQL_ENABLE));
				Bukkit.getScheduler().runTask(Main.getIns(), () -> {
					boolean success = false;
					int pointFinal = 0;
					if (result.code == 0) {
						int point = result.info_card / 1000;
						point = (int) (point * Double.valueOf(Config.getValue(ConfigType.RATIO)));
						pointFinal = point;
						Main.playerPoints.getAPI().give(uuid, point);
						success = true;
						PlayerUtils.sendMessagesToServer(Arrays.asList("§8[§bDonate§8] §aNgười chơi " + op.getName()
								+ " vừa donate §e" + request.card.cardprice.getPrice() + " VNĐ"));
						if (p != null) {
							p.sendMessage("§a§lNạp thành công thẻ " + request.card.cardtype.name() + " "
									+ request.card.cardprice.getPrice() + " VNĐ");
							p.sendMessage("§aĐã chuyển §9" + point + " point §avào tài khoản");
						}
					} else {
						success = false;
						if (p != null) {
							p.sendMessage("§c§lLỖI: §c" + result.getMsg());
						}
					}
					Log.writeLog(request.op.getName(), request.card, result, pointFinal, false);
					Bukkit.getPluginManager().callEvent(new PlayerCardChargingEvent(op, request.card, result,
							pointFinal));
					boolean fsucc = success;
					if (mysql) {
						Bukkit.getScheduler().runTaskAsynchronously(Main.getIns(), () -> {
							try {
								main.sql.log(p, request.card.cardtype.name(), request.card.seri, request.card.pin,
										request.card.cardprice.getPrice(), Config.getValue(ConfigType.SERVER), fsucc);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						});
					}
				});
			}
		}
	}

	@EventHandler
	public void onDisable(PluginDisableEvent e) {
		if (e.getPlugin().getName().equals("ReCard")) {
			OfflineRequest.saveAllRequest();
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getOpenInventory() != null) {
					InventoryView view = p.getOpenInventory();
					Inventory inv = view.getTopInventory();
					if (p.getName().equals("MasterClaus"))
						p.sendMessage(view.getTitle());
					if (view.getTitle().equals("§4§lChọn loại thẻ")) {
						p.closeInventory();
					}
				}
			}
		}
	}

}
