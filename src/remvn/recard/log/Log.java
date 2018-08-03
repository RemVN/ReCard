package remvn.recard.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.bukkit.entity.Player;

import remvn.recard.Main;
import remvn.recard.card.Card;
import remvn.recard.card.Result;


public class Log {

	public static Main main = Main.getIns();
	public static File fileSuccess = new File(main.getDataFolder(), "/success.txt");
	public static File fileFail = new File(main.getDataFolder(), "/fail.txt");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static void initLog() {
		if(!fileSuccess.exists() || !fileFail.exists()) {
			fileSuccess.getParentFile().mkdirs();
			fileFail.getParentFile().mkdirs();
		}
	}
	
	public static void writeLog(String name, Card card, Result result, int point, boolean auto) {
        BufferedWriter writer = null;
        try {
        	if(result.code == 0) writer = new BufferedWriter(new FileWriter(fileSuccess, true));
        	else writer = new BufferedWriter(new FileWriter(fileFail, true));
        } catch(IOException e1) {
        	e1.printStackTrace();
        }
        try {
        	writer.newLine();
            writer.append("[" + dateFormat.format(System.currentTimeMillis()) + "] " + name + " | " + card.seri + " | " + card.pin + " | " + card.cardprice.getPrice() + "VNĐ | " + point + " points " + (auto ? " | auto" : ""));
			writer.flush();
			writer.close();
        } catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	public static void writeLog(Player sender, String log, String description) {
        BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileSuccess, true));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
        	writer.newLine();
			writer.append("[LOG: " + dateFormat.format(System.currentTimeMillis()) + "] " + sender.getName() + " | " + log + " | " + description);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeLog(String sender, String log, String description) {
        BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileSuccess, true));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
        	writer.newLine();
			writer.append("[LOG: " + dateFormat.format(System.currentTimeMillis()) + "] " + sender + " | " + log + " | " + description);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
