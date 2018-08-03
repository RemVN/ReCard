package main.java.remvn.recard.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import main.java.remvn.recard.Main;

public class MySQL
{
  private lib.PatPeter.SQLibrary.MySQL sql;
  private Main main = Main.getIns();
  private boolean enabled = false;
  
  public MySQL(Main main, String host, int port, String db, String user, String password) throws SQLException {
    this.main = main;
//    sql = new lib.PatPeter.SQLibrary.MySQL(Logger.getLogger("Minecraft"), "[ReCard] ", host, port, user, db + "?autoReconnect=true", 
//      password);
    sql = new lib.PatPeter.SQLibrary.MySQL(Logger.getLogger("Minecraft"), "ReCard", host, port, db + "?autoReconnect=true", user, password);
    sql.open();
    sql.query(
      "CREATE TABLE if not exists `napthe_log` ( `id` MEDIUMINT(11) NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `uuid` VARCHAR(100) NOT NULL , `seri` VARCHAR(255) NOT NULL , `pin` VARCHAR(255) NOT NULL , `loai` VARCHAR(255) NOT NULL , `time` INT(11) NOT NULL , `menhgia` VARCHAR(10) NOT NULL , `success` INT(2) NOT NULL , `server` VARCHAR(15) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM");
    enabled = true;
  }
  
  public MySQL() {}
  
  protected void cleanup(ResultSet result, PreparedStatement statement)
  {
    if (result != null) {
      try {
        result.close();
      } catch (SQLException e) {
        main.getLogger().log(Level.SEVERE, "SQLException on cleanup", e);
      }
    }
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        main.getLogger().log(Level.SEVERE, "SQLException on cleanup", e);
      }
    }
  }
  
  public void closeConnection() {
    sql.close();
  }
  
  public void restart() {
    main.connect();
  }
  
  public void log(Player player, String cardtype, String seri, String code, int menhgia, String server, boolean success) throws SQLException
  {
    if (!enabled)
      return;
    if (sql.getConnection().isClosed())
      restart();
    sql.query("INSERT INTO napthe_log(name,uuid,loai,time,menhgia,pin,seri,server,success) VALUE (\"" + 
      player.getName() + "\", \"" + player.getUniqueId().toString() + "\", \"" + cardtype + "\", " + 
      System.currentTimeMillis() / 1000L + ", \"" + menhgia + "\", \"" + code + "\", \"" + seri + "\", \"" + 
      server + "\", " + (success ? 1 : 0) + ")");
  }
  
  public boolean isEnabled() {
    return enabled;
  }
}