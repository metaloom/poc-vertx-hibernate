package io.metaloom.poc;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class Runner {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		
		HttpServer server = vertx.createHttpServer();
	}

}
