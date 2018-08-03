package main.java.remvn.recard.card;

import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;

public class Result {
	
	public int code;
	public String msg;
	public int info_card;
	
	public Result(int code, String msg, int info_card) {
		this.code = code;
		this.msg = msg;
		this.info_card = info_card;
	}
	
	static Gson gson = new Gson();
	public static Result formJson(String json) {
		return gson.fromJson(json, Result.class);
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
	
	public String getMsg() {
		try {
			return new String(this.msg.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}
