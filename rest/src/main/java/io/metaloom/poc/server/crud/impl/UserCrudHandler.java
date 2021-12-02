package io.metaloom.poc.server.crud.impl;

import static io.metaloom.poc.server.ResponseHelper.sendFail;
import static io.metaloom.poc.server.ResponseHelper.sendJson;
import static io.metaloom.poc.server.ResponseHelper.sendMessage;

import java.util.UUID;

import javax.inject.Inject;

import io.metaloom.poc.db.PocUser;
import io.metaloom.poc.db.PocUserDao;
import io.metaloom.poc.server.RESTException;
import io.metaloom.poc.server.crud.CrudHandler;
import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class UserCrudHandler implements CrudHandler {

	private final PocUserDao userDao;

	@Inject
	public UserCrudHandler(PocUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void create(RoutingContext rc) {
		JsonObject payload = rc.getBodyAsJson();
		String username = payload.getString("username");
		String firstname = payload.getString("firstname");
		String lastname = payload.getString("lastname");
		String email = payload.getString("email");

		if (username == null) {
			sendFail(rc, 400, "username not specified");
			return;
		}

		userDao.createUser(username, user -> {
			if (email != null) {
				user.setEmail(email);
			}
			if (firstname != null) {
				user.setFirstname(firstname);
			}
			if (lastname != null) {
				user.setLastname(lastname);
			}
		})
			.subscribe(user -> {
				sendJson(rc, 201, user.toJson());
			}, err -> {
				rc.fail(err);
			});
	}

	@Override
	public void list(RoutingContext rc) {
		userDao.loadUsers().toList().subscribe(list -> {
			JsonObject json = new JsonObject();
			JsonArray users = new JsonArray();
			for (PocUser user : list) {
				users.add(user.getUuid().toString());
			}
			json.put("data", users);
			sendJson(rc, 200, json);
		}, err -> {
			rc.fail(err);
		});
	}

	@Override
	public void read(RoutingContext rc) {
		String uuid = rc.pathParam("uuid");
		Maybe<? extends PocUser> user = userDao.loadUser(UUID.fromString(uuid));
		user.switchIfEmpty(RESTException.createMaybe(404))
			.subscribe(result -> {
				JsonObject json = result.toJson();
				sendJson(rc, 200, json);
			}, err -> {
				rc.fail(err);
			});
	}

	@Override
	public void delete(RoutingContext rc) {
		String uuid = rc.pathParam("uuid");
		Maybe<? extends PocUser> user = userDao.loadUser(UUID.fromString(uuid));
		user.switchIfEmpty(RESTException.createMaybe(404))
			.flatMapCompletable(result -> {
				return userDao.deleteUser(result);
			}).doOnDispose(() -> {
				rc.fail(RESTException.create(500));
			}).subscribe(() -> {
				sendMessage(rc, 200, "User deleted");
			}, err -> {
				rc.fail(err);
			});
	}
}
