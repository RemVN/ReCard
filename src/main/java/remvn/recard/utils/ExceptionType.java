package remvn.recard.utils;

public enum ExceptionType {
	
	PIN_INPUT("§cMã pin phải là một con số"),
	SERI_INPUT("§cSeri phải là một con số"),
	IO("§cLỗi kết nối");
	;
	public String message;
	private ExceptionType(String message) {
		this.message = message;
	}
	
}
