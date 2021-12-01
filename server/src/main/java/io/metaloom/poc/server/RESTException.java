package io.metaloom.poc.server;

public class RESTException extends Exception {

	private static final long serialVersionUID = 2851476111189913616L;

	private int code;

	private String msg;

	public RESTException(int code) {
		this.code = code;
	}

	public RESTException setMessage(String msg) {
		this.msg = msg;
		return this;
	}

	public int code() {
		return code;
	}

	public String message() {
		return msg;
	}

	public static RESTException create(int code) {
		return new RESTException(code);
	}
}
