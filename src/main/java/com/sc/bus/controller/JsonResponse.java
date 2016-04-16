package com.sc.bus.controller;

public class JsonResponse {

	private String code;
	private String errormsg;

	public JsonResponse() {}
	public JsonResponse(String code, String errormsg) {
		this.code = code;
		this.errormsg = errormsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	@Override
	public String toString() {
		return "JsonResponse [code=" + code + ", errormsg=" + errormsg + "]";
	}
	
	
}