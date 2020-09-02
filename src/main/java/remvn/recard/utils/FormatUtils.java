package remvn.recard.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class FormatUtils {

	static DecimalFormat nonDecimalFormat = new DecimalFormat("###,###");
	public static String formatNonDecimalDouble(double value) {
		return nonDecimalFormat.format(value);
	}
	
	static DecimalFormat locationFormat = new DecimalFormat("###.#");
	public static String formatLocationDouble(double value) {
		return locationFormat.format(value);
	}
	
	public static String formatFloat(float value) {
		return String.format("%.1f", value);
	}
	
	public static SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static String getDateAndTime(long time) {
		return dateAndTimeFormat.format(time);
	}
	
	public static String getTimeString(long time) {
		long totalsecond = time / 1000;
		String timeString = "";			
		long day = (totalsecond/86400);
		long hour = ((totalsecond%86400) / 3600);
		long min = (((totalsecond%86400) % 3600) / 60);
		long second = (((totalsecond%86400) % 3600) % 60);
		
		if(day != 0) timeString += day + " ngày ";
		if(hour != 0) timeString += hour + " giờ ";
		if(min != 0) timeString += min + " phút ";
		long milis = (time % 1000) / 100;
		if(totalsecond <= 0) timeString += String.format("%s.%s giây", second, milis);
		else timeString += second + " giây";
		return timeString;
	}
	
}
