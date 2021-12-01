package io.metaloom.poc.server;

import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;

import javax.inject.Inject;
import javax.inject.Provider;

import io.metaloom.poc.db.PocGroupDao;
import io.metaloom.poc.server.crud.CrudHandler;
import io.metaloom.poc.server.crud.impl.GroupCrudHandler;
import io.metaloom.poc.server.crud.impl.UserCrudHandler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.Router;

public class ServerVerticle extends AbstractVerticle {

	public static final int MAX_RANGE = 5000;

	private final HttpServer rxHttpServer;
	private final CrudHandler groupHandler;
	private final CrudHandler userHandler;

	@Inject
	public ServerVerticle(Provider<HttpServer> rxHttpServerProvider, UserCrudHandler userCrudHandler, GroupCrudHandler groupCrudHandler,
		PocGroupDao groupDao) {
		this.rxHttpServer = rxHttpServerProvider.get();
		this.userHandler = userCrudHandler;
		this.groupHandler = groupCrudHandler;
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		Router router = createRouter();

		rxHttpServer.requestHandler(router.getDelegate()::handle);
		rxHttpServer.listen(lh -> {
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
		if (rxHttpServer != null) {
			rxHttpServer.close(ch -> {
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

		addFailureHandler(router);
		addUserCrud(router);
		addGroupCrud(router);

		return router;
	}

	private void addFailureHandler(Router router) {
		router.route().failureHandler(rc -> {
			// Handle common 404 errors
			if (rc.statusCode() == 404) {
				JsonObject json = new JsonObject();
				json.put("message", "Element or path not found");
				rc.end(Buffer.newInstance(json.toBuffer()));
				return;
			}
			// Handle REST errors
			Throwable failure = rc.failure();
			if (rc.failed() && failure != null && failure instanceof RESTException) {
				RESTException restFailure = (RESTException) failure;
				int code = restFailure.code();
				String path = rc.normalizedPath();
				System.out.println("Error " + code + " in " + path);
				JsonObject json = new JsonObject();
				json.put("message", restFailure.message());
				rc.response().setStatusCode(code);
				rc.end(Buffer.newInstance(json.toBuffer()));
				return;
			}
			// Fallback to default handler
			rc.next();
		});

	}

	private void addGroupCrud(Router router) {
		router.route("/groups").method(POST).handler(groupHandler::create);
		router.route("/groups/:uuid").method(GET).handler(groupHandler::read);
		router.route("/groups/:uuid").method(DELETE).handler(groupHandler::delete);
		router.route("/groups").method(GET).handler(groupHandler::list);

		// Method added just to ease testing
		router.route("/addGroup").method(GET).handler(groupHandler::create);

	}

	private void addUserCrud(Router router) {
		router.route("/users").method(POST).handler(userHandler::create);
		router.route("/users/:uuid").method(GET).handler(groupHandler::read);
		router.route("/users/:uuid").method(DELETE).handler(groupHandler::delete);
		router.route("/users").method(GET).handler(userHandler::list);

		// Method added just to ease testing
		router.route("/addUser").method(GET).handler(userHandler::create);

	}

}
