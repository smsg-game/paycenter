package com.fantingame.pay.exception;

public class EasouPayException extends Exception {

	private static final long serialVersionUID = 4973594990790332390L;
	
	private String code;
	private String msg;

	public EasouPayException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getMessage() {
		return this.msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
