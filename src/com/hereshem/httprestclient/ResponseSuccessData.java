package com.hereshem.httprestclient;

public class ResponseSuccessData {
	private String result;
	private int status;
	
	public ResponseSuccessData(String result, int status) {
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
