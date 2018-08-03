package main.java.remvn.recard;

import java.util.logging.Level;

import main.java.remvn.recard.card.OfflineRequest;
import main.java.remvn.recard.config.ConfigType;
import main.java.remvn.recard.gui.ListenerGUI;
import main.java.remvn.recard.gui.Status;
import main.java.remvn.recard.log.Log;
import main.java.remvn.recard.mysql.MySQL;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.remvn.recard.commands.CommandCard;
import main.java.remvn.recard.commands.CommandGUI;
import main.java.remvn.recard.config.Config;

public class Main extends JavaPlugin {
	
	public static Main main;
	public static PlayerPoints playerPoints;
	public MySQL sql;
	public void onEnable() {
		main = this;
		this.getCommand("rc").setExecutor(new CommandCard());
		this.getCommand("napthe").setExecutor(new CommandGUI());
		this.getServer().getPluginManager().registerEvents(new ListenerPlayer(), this);
		this.getServer().getPluginManager().registerEvents(new ListenerGUI(), this);
		Config.init();
		hookPlayerPoints();
		connect();
		new BukkitRunnable() {
			@Override
			public void run() {
				Status.statusTimer();
				Log.initLog();
				Bukkit.getScheduler().runTaskLaterAsynchronously(main, () ->{
					Status.statusMesasgeTimer();
					OfflineRequest.init();
				}, 50l);
			}
		}.runTaskLater(this, 100l);
	}
	
	public void onDisable() {
	}
	
	public static Main getIns() {
		return main;
	}
	
    public boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        try {
            playerPoints = PlayerPoints.class.cast(plugin);
        } catch(NoClassDefFoundError exc) {
			ConsoleCommandSender console = Bukkit.getConsoleSender();
			console.sendMessage("");
			console.sendMessage("");
			console.sendMessage("�cKhong tim thay plugin PlayerPoints");
			console.sendMessage("�cPlugin ReCard se bi vo hieu hoa");
			console.sendMessage("");
			console.sendMessage("");
			this.setEnabled(false);
        }
        return playerPoints != null; 
    }
	
    // MySQL(Main main, String host, int port, String user, String db, String password)
    public void connect() {
        if (sql != null)
          sql.closeConnection();
        if (Boolean.valueOf(Config.getValue(ConfigType.MYSQL_ENABLE))) {
          try {
        	  String host = Config.getValue(ConfigType.MYSQL_HOST);
        	  int port = Integer.valueOf(Config.getValue(ConfigType.MYSQL_PORT));
        	  String user = Config.getValue(ConfigType.MYSQL_USER);
        	  String db = Config.getValue(ConfigType.MYSQL_DATABASE);
        	  String pass = Config.getValue(ConfigType.MYSQL_PASSWORD);
            sql = new MySQL(getIns(), host, port, db, user, pass);
          } catch (Exception e) {
            getIns().getLogger().log(Level.SEVERE, "Khong the ket noi MySQL", e);
            sql = null;
          }
        } else
          sql = null;
      }
	
}
