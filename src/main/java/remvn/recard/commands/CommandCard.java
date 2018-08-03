package main.java.remvn.recard.commands;

import java.io.IOException;
import java.sql.SQLException;

import main.java.remvn.recard.Main;
import main.java.remvn.recard.card.CardType;
import main.java.remvn.recard.card.Result;
import main.java.remvn.recard.config.ConfigType;
import main.java.remvn.recard.gui.Status;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.remvn.recard.config.Config;

public class CommandCard implements CommandExecutor {
	
	public static Main main = Main.getIns();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("ReCard.admin")) {
//			if(args[0].equals("test")) {
//				Card card = new Card("123123", "123231", CardType.VIETTEL, CardPrice.CP_300000);
//				String resultString = "";
//				try {
//					String data = card.createRequestUrl();
//					resultString = CardManager.post(data);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				
//				Result result = Result.formJson(resultString);
//				System.out.println("Code: " + result.code);
//				System.out.println("Card price: " + result.info_card);
//				System.out.println(result.getMsg());
//				
//				return true;
//			}
			
			if(args[0].equals("status")) {
				if(!Status.statusCards.isEmpty()) {
					Status.sendMessagesToServer(Status.getStatusMessages());
				}
				return true;
			}
			
			if(args[0].equals("test2")) {
				String statusString = "";
				try {
					statusString = Status.getStatusString();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sender.sendMessage(statusString);
				Status.toStatusCard(statusString);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("test3")) {
				Player p = (Player) sender;
				try {
//					System.out.println(Config.getValue(ConfigType.MYSQL_ENABLE));
					main.sql.log(p, CardType.VIETTEL.name(), "12312", "3123", 34234, Config.getValue(ConfigType.SERVER), true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(args[0].equalsIgnoreCase("test4")) {
				Result result = new Result(0, "asd", 100000);
				int point = result.info_card/1000;
				point = (int) (point * Double.valueOf(Config.getValue(ConfigType.RATIO)));
				sender.sendMessage("debug point card 100k: " + point);
			}
			
			
			if(args[0].equalsIgnoreCase("reload")) {
				Config.loadFileF();
				sender.sendMessage("§aĐã reload ReCard");
				return true;
			}	
		}
		return true;
	}
	
	
}
