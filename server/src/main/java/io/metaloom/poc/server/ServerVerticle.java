package io.metaloom.poc.server;

import static io.metaloom.poc.server.ResponseHelper.sendJson;
import static io.metaloom.poc.server.ResponseHelper.sendMessage;
import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.poc.server.crud.CrudHandler;
import io.metaloom.poc.server.crud.impl.GroupCrudHandler;
import io.metaloom.poc.server.crud.impl.UserCrudHandler;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;

public class ServerVerticle extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(ServerVerticle.class);

	private final HttpServer rxHttpServer;
	private CrudHandler groupHandler;
	private CrudHandler userHandler;

	@Inject
	public ServerVerticle(Provider<HttpServer> rxHttpServerProvider, UserCrudHandler userHandler, GroupCrudHandler groupHandler) {
		this.rxHttpServer = rxHttpServerProvider.get();
		this.userHandler = userHandler;
		this.groupHandler = groupHandler;
	}

	@Override
	public Completable rxStart() {
		Router router = Router.router(vertx);
		setupRouter(router);
		rxHttpServer.requestHandler(router::handle);
		Completable startHttpServer = rxHttpServer.rxListen()
			.doOnSubscribe(s -> {
				logger.info("✅ Hibernate Reactive is ready");
				logger.info("✅ Server started..");
			}).ignoreElement();

		return startHttpServer;
	}

	@Override
	public Completable rxStop() {
		return rxHttpServer.rxClose();
	}

	private void setupRouter(Router router) {

		// Internal handlers
		router.route().handler(BodyHandler.create());
		addFailureHandler(router);

		// Exposed routes
		addUserCrud(router);
		addGroupCrud(router);
	}

	private void addFailureHandler(Router router) {
		router.route().failureHandler(rc -> {
			// Handle REST errors
			Throwable failure = rc.failure();
			if (rc.failed() && failure != null && failure instanceof RESTException) {
				RESTException restFailure = (RESTException) failure;
				int code = restFailure.code();
				String path = rc.normalizedPath();
				System.out.println("Error " + code + " in " + path);
				JsonObject json = new JsonObject();
				json.put("message", restFailure.message());
				sendJson(rc, code, json);
				return;
			}

			// Handle common 404 errors
			if (rc.statusCode() == 404) {
				sendMessage(rc, 404, "Path not found");
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
		router.route("/users/:uuid").method(GET).handler(userHandler::read);
		router.route("/users/:uuid").method(DELETE).handler(userHandler::delete);
		router.route("/users").method(GET).handler(userHandler::list);

		// Method added just to ease testing
		router.route("/addUser").method(GET)
			.handler(rc -> {
				JsonObject json = new JsonObject();
				json.put("username", "joedoe");
				json.put("email", "joe.doe@acme.nowhere");
				json.put("firstname", "Joe");
				json.put("lastname", "Doe");
				rc.setBody(Buffer.newInstance(json.toBuffer()));
				rc.next();
			})
			.handler(userHandler::create);

	}

}
