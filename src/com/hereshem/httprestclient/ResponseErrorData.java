package com.hereshem.httprestclient;

public class ResponseErrorData {
	private String result;
	private int status;
	
	public ResponseErrorData(String result, int status) {
		super();
		this.result = result;
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public int getStatus() {
		return status;
	}
	
}
