package remvn.recard.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import remvn.recard.Main;
import remvn.recard.utils.Export;

public class Config {
	
	public static Main main = Main.getIns();
	public static File file = new File(main.getDataFolder(), "config.yml");
	public static YamlConfiguration saver = YamlConfiguration.loadConfiguration(file);
	
	public static String getValue(ConfigType type) {
		if(type.path != null && !type.path.isEmpty()) {
			return saver.getString(type.path);
		} else
		return saver.getString(type.name().toLowerCase());
	}
	
	public static void init() {
		new File(main.getDataFolder() + "").mkdirs();
		if(!file.exists()) {
			Export.copy(main, "config.yml");
			loadFileF();
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
