package remvn.recard.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import remvn.recard.Main;

public class Export {
	
	static Main main = Main.getIns();
	public static void copy(Main main, String filename) { 
		File file = new File(main.getDataFolder(), filename);
		if (!file.exists()) {
		     InputStream link = (main.getClass().getResourceAsStream("/config/" + filename));
		     try {
		    	if(link == null) {
		    		System.out.println("link null");
		    	}
				Files.copy(link, file.getAbsoluteFile().toPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
