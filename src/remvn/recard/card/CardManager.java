package remvn.recard.card;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import remvn.recard.Main;
import remvn.recard.config.Config;
import remvn.recard.config.ConfigType;

public class CardManager {
	public static Main main = Main.getIns();
	public static ConcurrentHashMap<UUID, Request> requestMap = new ConcurrentHashMap<UUID, Request>();
	public static ConcurrentHashMap<UUID, Long> delayMap = new ConcurrentHashMap<UUID, Long>();
	public static void removeRequest(UUID uuid) {
		if(requestMap.containsKey(uuid)) requestMap.remove(uuid);
	}
	public static void addRequest(Player p, UUID uuid, Request request) {
		removeRequest(uuid);
		if(delayMap.containsKey(uuid)) {
			if(System.currentTimeMillis() - delayMap.get(uuid) > 60*1000l) {
				requestMap.put(uuid, request);
				delayMap.put(uuid, System.currentTimeMillis());
				p.sendMessage("§aHãy nhập §esố seri §atrên khung chat");
			} else {
				long delay = 60*1000l - (System.currentTimeMillis() - delayMap.get(uuid));
				p.sendMessage("§cBạn phải chờ " + formatTime(delay) + " để tiếp tục nạp thẻ");
				return;
			}
		} else {
			delayMap.put(uuid, System.currentTimeMillis());
			requestMap.put(uuid, request);
			p.sendMessage("§aHãy nhập §esố seri §atrên khung chat");
		}
	}
	
	public static void napthe() {
		
	}
	
	
	public static String formatTime(long time) {
		long totalsecond = time / 1000;
		String until = "";			
		long day = (totalsecond/86400);
		long hour = ((totalsecond%86400) / 3600);
		long min = (((totalsecond%86400) % 3600) / 60);
		long second = (((totalsecond%86400) % 3600) % 60);
		
		if(day != 0) until += day + " ngày ";
		if(hour != 0) until += hour + " giờ ";
		if(min != 0) until += min + " phút ";

		until += second + " giây";
		return until;
	}
	
	public static String urlGameBank = "http://sv.gamebank.vn/api/card";
	public static String post(String data) throws IOException {
		URL url = null;
		try {
			url = new URL(urlGameBank);
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
        httpsCon.setRequestMethod("POST");
        httpsCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpsCon.setRequestProperty("charset", "utf-8");
        httpsCon.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
//        httpsCon.setReadTimeout(120);

        httpsCon.setUseCaches(false);
        httpsCon.connect();
        DataOutputStream os = new DataOutputStream(httpsCon.getOutputStream());
		os.write(data.getBytes());
		os.close();
		
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
	
	 public static String getAuthantication(String username, String password) {
	   String auth = new String(Base64Coder.encodeString(username + ":" + password));
	   return auth;
	 }
	
}
