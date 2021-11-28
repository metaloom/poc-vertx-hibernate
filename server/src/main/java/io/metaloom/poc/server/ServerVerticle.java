package io.metaloom.poc.server;

import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;

import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.reactive.stage.Stage.SessionFactory;

import io.metaloom.poc.db.PocGroupDao;
import io.metaloom.poc.db.PocUserDao;
import io.metaloom.poc.db.impl.PocGroupDaoImpl;
import io.metaloom.poc.db.impl.PocUserDaoImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ServerVerticle extends AbstractVerticle {

	public static final int SERVER_PORT = 8080;
	public static final String SERVER_HOST = "localhost";

	public static final int MAX_RANGE = 5000;

	public HttpServer server;
	private SessionFactory factory;
	private PocUserDao userDao;
	private PocGroupDao groupDao;

	public ServerVerticle(SessionFactory factory) {
		this.factory = factory;
		this.userDao = new PocUserDaoImpl(factory);
		this.groupDao = new PocGroupDaoImpl(factory);
	}

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

	AtomicLong count = new AtomicLong(0);

	private Router createRouter() {
		Router router = Router.router(vertx);

		router.route("/users").method(POST).handler(rc -> {
			addUser(rc);
		});

		router.route("/addUser").method(GET).handler(rc -> {
			addUser(rc);
		});

		router.route("/users").method(GET).handler(rc -> {
			listUsers(rc);
		});

		return router;
	}

	private void addUser(RoutingContext rc) {
		userDao.createUser("user_" + count.incrementAndGet()).subscribe(u -> {
			rc.end("Added " + u.getUuid());
		}, err -> {
			rc.fail(err);
		});
	}

	private void listUsers(RoutingContext rc) {
		userDao.loadUsers().subscribe(u -> {
			rc.response().write(u.getUuid().toString());
		}, err -> {
			rc.fail(err);
		}, () -> {
			rc.end("End");
		});
	}

}
