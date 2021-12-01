package io.metaloom.poc.server.crud.impl;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.metaloom.poc.db.PocUser;
import io.metaloom.poc.db.PocUserDao;
import io.metaloom.poc.server.RESTException;
import io.metaloom.poc.server.crud.CrudHandler;
import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class UserCrudHandler implements CrudHandler {

	private final PocUserDao userDao;
	private final AtomicLong count = new AtomicLong(0);

	@Inject
	public UserCrudHandler(PocUserDao userDao) {
		this.userDao = userDao;
	}

	public void create(RoutingContext rc) {
		userDao.createUser("user_" + count.incrementAndGet()).subscribe(u -> {
			rc.end("Added " + u.getUuid());
		}, err -> {
			rc.fail(err);
		});
	}

	public void list(RoutingContext rc) {
		userDao.loadUsers().toList().subscribe(list -> {
			JsonObject json = new JsonObject();
			JsonArray users = new JsonArray();
			for (PocUser user : list) {
				users.add(user.getUuid().toString());
			}
			json.put("users", users);
			rc.end(new Buffer(json.toBuffer()));
		}, err -> {
			rc.fail(err);
		});
	}

	@Override
	public void read(RoutingContext rc) {
		String uuid = rc.pathParam("uuid");
		Maybe<? extends PocUser> user = userDao.loadUser(UUID.fromString(uuid));
		user.switchIfEmpty(Maybe.error(RESTException.create(404)))
			.subscribe(result -> {
				JsonObject json = result.toJson();
				rc.end(Buffer.newInstance(json.toBuffer()));
			}, err -> {
				rc.fail(err);
			});
	}

	@Override
	public void delete(RoutingContext rc) {
		String uuid = rc.pathParam("uuid");
		Maybe<? extends PocUser> user = userDao.loadUser(UUID.fromString(uuid));
		user.switchIfEmpty(Maybe.error(RESTException.create(404)))
			.flatMapCompletable(result -> {
				return userDao.deleteUser(result);
			}).doOnDispose(() -> {
				rc.fail(RESTException.create(500));
			}).subscribe(() -> {
				rc.end("Deleted " + uuid);
			}, err -> {
				rc.fail(err);
			});
	}
}
