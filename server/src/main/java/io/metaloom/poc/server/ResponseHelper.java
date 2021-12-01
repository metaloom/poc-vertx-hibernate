package io.metaloom.poc.server;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.http.HttpHeaders;
import io.vertx.rxjava3.core.http.HttpServerResponse;
import io.vertx.rxjava3.ext.web.RoutingContext;

public final class ResponseHelper {

	private ResponseHelper() {
	}

	public static void sendMessage(RoutingContext rc, int code, String msg) {
		JsonObject json = new JsonObject();
		json.put("message", msg);
		sendJson(rc, code, json);
	}

	public static void sendFail(RoutingContext rc, int code, String message) {
		rc.fail(RESTException.create(code).setMessage(message));
	}

	public static void sendJson(RoutingContext rc, int code, JsonObject payload) {
		HttpServerResponse response = rc.response();
		response.setStatusCode(201);
		response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		rc.end(payload.encodePrettily());
	}
}
