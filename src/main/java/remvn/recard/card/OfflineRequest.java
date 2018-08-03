package remvn.recard.card;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import main.java.remvn.recard.config.ConfigType;
import main.java.remvn.recard.utils.Export;
import main.java.remvn.recard.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.Gson;

import main.java.remvn.recard.Main;
import main.java.remvn.recard.config.Config;
import main.java.remvn.recard.gui.Status;
import main.java.remvn.recard.log.Log;
import remvn.recard.Main;
import remvn.recard.config.Config;
import remvn.recard.config.ConfigType;
import remvn.recard.gui.Status;
import remvn.recard.log.Log;
import remvn.recard.utils.Export;
import remvn.recard.utils.PlayerUtils;

public class OfflineRequest {

	public UUID uuid;
	public Card card;
	public UUID id;
	public String playerName;
	public boolean success;
	public String currentMsg;
	public long time;
	public OfflineRequest(UUID uuid, Card card, String playerName) {
		this.id = UUID.randomUUID();
		this.uuid = uuid;
		this.card = card;
		this.playerName = playerName;
		this.success = false;
		this.currentMsg = "";
		this.time = System.currentTimeMillis();
	}

	static Main main = Main.getIns();
	static Gson gson = new Gson();
	public String toJson() {
		return gson.toJson(this);
	}
	public static OfflineRequest fromJson(String json) {
		return gson.fromJson(json, OfflineRequest.class);
	}
	
	static File file = new File(main.getDataFolder(), "/offlinecard.yml");
	static YamlConfiguration saver = YamlConfiguration.loadConfiguration(file);
	static ConcurrentHashMap<UUID, List<OfflineRequest>> map = new ConcurrentHashMap<>();
	
	public static void saveOfflineRequest(OfflineRequest offlineRequest) {
		saver.set("data." + offlineRequest.uuid.toString() + "." + offlineRequest.id.toString(), offlineRequest.toJson());
	}
	
	public static void init() {
		new File(main.getDataFolder() + "").mkdirs();
		if(!file.exists()) {
			Export.copy(main, "offlinecard.yml");
			loadFileF();
		}
		loadAllOfflineRequest();
		OfflineRequestTimer();
		saveFileTimer();
	}
	
	public static void saveFileTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				saveAllRequest();
				saveFileF();
			}
		}.runTaskTimerAsynchronously(main, 0l, 20*20l); // 600s
	}
	
	static List<Integer> blacklistCode = Arrays.asList(3, 9,10,11,15,13,16,17,18,19,20,21,22,6);
	static List<Integer> allowCode = Arrays.asList(30);
	public static void OfflineRequestTimer() {
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				List<OfflineRequest> list = getListRequest();
				if(list.size() > 0) System.out.println("[ReCard] Dang tu dong nap the, so the chua duoc duyet: " + list.size());
				if(Boolean.valueOf(Config.getValue(ConfigType.AUTO)))
				if(!list.isEmpty()) {
					if(i > list.size()-1) i = 0;
					OfflineRequest request = list.get(i);
					if(!Status.getByCardType(request.card.cardtype, Status.statusCards).status) return;
//					System.out.println("auto napthe 1: " + request.toJson());
					i++;
					OfflinePlayer op = Bukkit.getOfflinePlayer(request.uuid);
					Player p = op.getPlayer();
					boolean messageToPlayer = true;
					if(p == null || !p.isOnline()) messageToPlayer = false;
					
					String resultString = "";
					try {
						resultString = CardManager.post(request.card.createRequestUrl(op.getName()));
					} catch (IOException e1) {
//						if(messageToPlayer) p.sendMessage(ExceptionType.IO.message);
						e1.printStackTrace();
					}
					Result result = Result.formJson(resultString);
					boolean success = false;
					int pointFinal = 0;
					boolean mysql = Boolean.valueOf(Config.getValue(ConfigType.MYSQL_ENABLE));
					if (result.code == 0) {
						int point = result.info_card / 1000;
						point = (int) (point * Double.valueOf(Config.getValue(ConfigType.RATIO)));
						pointFinal = point;
						Main.playerPoints.getAPI().give(request.uuid, point);
						success = true;
						Log.writeLog(request.playerName, request.card, result, pointFinal, true);
						PlayerUtils.sendMessagesToServer(Arrays.asList("§8[§bDonate§8] §aNgười chơi " + request.playerName
								+ " vừa donate §e" + request.card.cardprice.getPrice() + " VNĐ"));
						if(messageToPlayer) {
							p.sendMessage("§a§lNạp thành công thẻ " + request.card.cardtype.name() + " "
									+ request.card.cardprice.getPrice() + " VNĐ");
							p.sendMessage("§aĐã chuyển §9" + point + " point §avào tài khoản");
						}
						if (mysql)
							try {
								main.sql.log(p, request.card.cardtype.name(), request.card.seri, request.card.pin,
										request.card.cardprice.getPrice(), Config.getValue(ConfigType.SERVER), success);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
					} else {
						success = false;
					}
					System.out.println(request.card.pin + " Code: "  + result.code);
					request.currentMsg = result.getMsg();
					request.success = success;
//					System.out.println("auto napthe 2: " + request.toJson());
					if(!allowCode.contains(result.code)) {
						System.out.println("remove " + request.card.pin);
						removeRequest(request);
					}
				}
			}
		}.runTaskTimerAsynchronously(main, 20l, 500l);
	}
	
	public static void removeRequest(OfflineRequest request) {
		if(map.containsKey(request.uuid)) {
			List<OfflineRequest> list = map.get(request.uuid);
			if(list.isEmpty())	return;
			synchronized(list) {
				Iterator<OfflineRequest> ite = list.iterator();
				while(ite.hasNext()) {
					OfflineRequest requestClone = ite.next();
					if(requestClone.id.equals(request.id)) {
						ite.remove();
						break;
					}
				}
			}
		}
	}
	
	public static List<OfflineRequest> getListRequest() {
		List<OfflineRequest> requests = new ArrayList<OfflineRequest>();
		for(Entry<UUID, List<OfflineRequest>> entry : map.entrySet()) {
			for(OfflineRequest request : entry.getValue()) {
				requests.add(request);
			}
		}
		return requests;
	}
	
	public static void loadAllOfflineRequest() {
		if(saver.isConfigurationSection("data")) {
			for(String uuid : saver.getConfigurationSection("data").getKeys(false)) {
				if(saver.isConfigurationSection("data." + uuid))
				for(String id : saver.getConfigurationSection("data." + uuid).getKeys(false)) {
					OfflineRequest request = OfflineRequest.fromJson(saver.getString("data." + uuid + "." + id));
					addRequestToMap(request);
					System.out.println("Load request: " + request.toJson());
				}
			}
		}
	}
	
	public static void saveAllRequest() {
		saver.set("data", "");
		for(Entry<UUID, List<OfflineRequest>> entry : map.entrySet()) {
			for(OfflineRequest request : entry.getValue()) {
				saveOfflineRequest(request);
			}
		}
	}
	
	public static void addRequestToMap(OfflineRequest request) {
		if(!map.containsKey(request.uuid)) {
			List<OfflineRequest> list = new ArrayList<OfflineRequest>();
			list.add(request);
			map.put(request.uuid, Collections.synchronizedList(list));
		}
		else {
			List<OfflineRequest> list = map.get(request.uuid);
			list.add(request);
			map.put(request.uuid, list);
		}
	}
	
	public static void loadFileF() {
		try {
			saver.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveFileF() {
		try {
			saver.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
