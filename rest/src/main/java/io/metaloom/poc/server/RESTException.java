package io.metaloom.poc.server;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;

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

	public static <T> MaybeSource<T> createMaybe(int code) {
		return Maybe.error(create(code));
	}
}
