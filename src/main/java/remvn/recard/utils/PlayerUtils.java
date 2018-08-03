package main.java.remvn.recard.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.remvn.recard.Main;


public class PlayerUtils {
	
	public static Main main = Main.getIns();
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
	
    public static boolean isOutsideOfBorder(Player p) {
        Location loc = p.getLocation();
        WorldBorder border = p.getWorld().getWorldBorder();
        double x = loc.getX();
        double z = loc.getZ();
        double size = border.getSize() / 2;
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }
	
}
