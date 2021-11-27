package io.metaloom.poc.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

public class ServerVerticle extends AbstractVerticle {

	public static final int SERVER_PORT = 8080;
	public static final String SERVER_HOST = "localhost";

	public static final int MAX_RANGE = 5000;

	public HttpServer server;

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		HttpServerOptions options = new HttpServerOptions();
		options
			.setPort(SERVER_PORT)
			.setHost(SERVER_HOST)
			.setCompressionSupported(true)
			.setHandle100ContinueAutomatically(true)
			.setTcpFastOpen(true)
			.setTcpNoDelay(true)
			.setTcpQuickAck(true);

		server = vertx.createHttpServer(options);
		Router router = createRouter();

		server.requestHandler(router::handle);
		server.listen(lh -> {
			if (lh.failed()) {
				startPromise.fail(lh.cause());
			} else {
				System.out.println("Server started..");
				startPromise.complete();
			}
		});
	}

	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		if (server != null) {
			server.close(ch -> {
				if (ch.failed()) {
					stopPromise.fail(ch.cause());
				} else {
					stopPromise.complete();
				}
			});
		}
	}

	private Router createRouter() {
		Router router = Router.router(vertx);
		router.route("/test").handler(rc -> {
			rc.end("hello-world");
		});
		return router;
	}

}
