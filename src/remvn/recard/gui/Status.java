package remvn.recard.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import remvn.recard.Main;
import remvn.recard.card.CardType;
import remvn.recard.config.Config;
import remvn.recard.config.ConfigType;

public class Status {
	
	public static Main main = Main.getIns();
	static String status_url = "https://sv.gamebank.vn/trang-thai-he-thong-2";
	public static CopyOnWriteArrayList<StatusCard> statusCards;
	
	public static void statusTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					statusCards = toStatusCard(getStatusString());
//					System.out.println(getStatusString());
				} catch (IOException e) {
					System.out.println("Loi ket noi toi gamebank: https://sv.gamebank.vn/trang-thai-he-thong-2");
//					e.printStackTrace();
				}
			}
		}.runTaskTimerAsynchronously(main, 0l, 200l);
	}
	
	static Gson gson = new Gson();
	public static CopyOnWriteArrayList<StatusCard> toStatusCard(String s) {
		CopyOnWriteArrayList<StatusCard> statusCards = new CopyOnWriteArrayList<StatusCard>();
     	JsonArray je = new JsonParser().parse(s).getAsJsonArray();
     	JsonElement je1 = je.get(0);
     	JsonParser jp = new JsonParser();
     	String value = je1.toString().toUpperCase();
     	value = value.replace("MOBIPHONE", "MOBIFONE");
     	JsonObject jo = (JsonObject) jp.parse(value);
     	for(CardType cardtype : CardType.values()) {
     		String statusCode = "";
     		try {
     			 statusCode = jo.get(cardtype.name()).getAsString();
//     			 System.out.println(cardtype.name() + " " + statusCode);
     		} catch(NullPointerException exc) {
     			statusCards.add(new StatusCard(cardtype, false));
     			continue;
     		}
     		if(statusCode.equals("1")) statusCards.add(new StatusCard(cardtype, true)); else 
     			statusCards.add(new StatusCard(cardtype, false));
     	}
     	return statusCards;
	}
	
	
	public static void statusMesasgeTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(Boolean.valueOf(Config.getValue(ConfigType.STATUS_ENABLE))) 
				if(!statusCards.isEmpty()) {
					sendMessagesToServer(getStatusMessages());
				}
			}
		}.runTaskTimerAsynchronously(main, 200l, 18000l); // 12000l
	}
	
	public static List<String> getStatusMessages() {
		if(!statusCards.isEmpty()) {
			List<String> message = new ArrayList<String>();
			String prefix = Config.getValue(ConfigType.PREFIX).replaceAll("&", "§");
			message.add(prefix + "§eTình trạng thẻ cào:");
			String sT = "";
			int lengthConst = 0;
			int i = 1;
			for(StatusCard status : statusCards) {
				String s = "§b" + (status.cardType.equals(CardType.VIETNAMMOBILE) ? "VNM" : status.cardType.name());
				if(!status.status) s += " §7Bảo trì§c§l"; else s+= " §7Đang hoạt động§a§l";
				s+= "● ";
				if(i == 1) {
					String stClone = sT + s;
					if(lengthConst == 0) lengthConst = stClone.length() + 2;
					for(int j = 0; j < lengthConst - stClone.length(); j++) {
						s += " "; 
					}
				}
				sT += s;
				i++;
				if(i == 3) {
					message.add(" §b" + sT);
					sT = "";
					i = 1;
				}
			}
			double ratio = Double.valueOf(Config.getValue(ConfigType.RATIO));
			if(ratio > 1) {
				double km = (ratio - 1) * 100;
				message.add(prefix + "§aĐang khuyến mãi §c§l" + km + "% §agiá trị nạp thẻ");
			}
			return message;
		}
		return new ArrayList<String>();
	}
	
	public static void sendMessagesToServer(List<String> message) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					message.forEach(s -> {
						p.sendMessage(s);
					});
				}
			}
		}.runTaskAsynchronously(main);
	}
	
	
	public static void updateStatus() {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			try {
				statusCards = toStatusCard(getStatusString());
			} catch (IOException e) {
				System.out.println("Loi ket noi toi gamebank: https://sv.gamebank.vn/trang-thai-he-thong-2");
			}
		});
	}
	
	public static StatusCard getByCardType(CardType type, CopyOnWriteArrayList<StatusCard> list) {
		if(list == null) updateStatus();
		for(StatusCard sc : list) {
			if(sc.cardType.equals(type)) return sc;
		}
		return null;
	}
	
	public static String getStatusString() throws IOException {
		URL url = null;
		try {
			url = new URL(status_url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		URLConnection urlCon = null;
        Authenticator.setDefault(new Authenticator()
        {
          @Override
          protected PasswordAuthentication getPasswordAuthentication()
          {
            return new PasswordAuthentication(Config.getValue(ConfigType.API_USER), Config.getValue(ConfigType.API_PASSWORD).toCharArray());
          }
        });
		urlCon = url.openConnection();
		HttpURLConnection httpsCon = (HttpURLConnection) urlCon;
        httpsCon.setDoOutput(true);
        httpsCon.setDoInput(true);
        httpsCon.setInstanceFollowRedirects(false);
        httpsCon.setRequestMethod("GET");
//        httpsCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        httpsCon.setRequestProperty("charset", "utf-8");
//        httpsCon.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
//        httpsCon.setReadTimeout(120);
        httpsCon.setReadTimeout(200);
        httpsCon.setUseCaches(false);
        httpsCon.connect();

        
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
		} catch(HttpRetryException e) {
			e.printStackTrace();
		}

		StringBuilder result = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
		    result.append(line);
		}
		httpsCon.disconnect();
		return result.toString();
	}
	
}
