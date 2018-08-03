package remvn.recard.config;

public enum ConfigType {
	
	MERCHANT_ID(),
	API_PASSWORD(),
	API_USER(),
	RATIO(),
	SERVER(),
	STATUS_ENABLE("status.enable"),
	PREFIX("prefix"), 
	AUTO("auto"), 
	
	MYSQL_ENABLE("mysql.enable"),
	MYSQL_HOST("mysql.host"),
	MYSQL_PORT("mysql.port"),
	MYSQL_USER("mysql.user"),
	MYSQL_PASSWORD("mysql.password"),
	MYSQL_DATABASE("mysql.database"),
	
	;
	public String path;
	ConfigType() {
		
	}
	ConfigType(String path) {
		this.path = path;
	}
}
