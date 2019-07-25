package com.cafe24.ypshop.backend.dto;

public class JSONResult {
	
	private String result;  //성공 or 실패
	private String message; //실패 메시지
	private Object data;    //성공 데이터

	public static JSONResult success(Object data) {
		return new JSONResult("success", null, data);
	}

	//실패
	public static JSONResult fail(String message) {
		return new JSONResult("fail", message, null);
	}
	
	//실패 + data
	public static JSONResult fail(String message, Object data) {
		return new JSONResult("fail", message, data);
	}
	
	private JSONResult(String result, String message, Object data) {
		this.result = result;
		this.message = message;
		this.data = data;
	}
	
	public String getResult() {
		return result;
	}
	public String getMessage() {
		return message;
	}
	public Object getData() {
		return data;
	}
}