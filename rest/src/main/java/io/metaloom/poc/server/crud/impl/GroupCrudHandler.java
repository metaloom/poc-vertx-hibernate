package io.metaloom.poc.server.crud.impl;

import static io.metaloom.poc.server.ResponseHelper.sendJson;
import static io.metaloom.poc.server.ResponseHelper.sendMessage;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.metaloom.poc.db.PocGroup;
import io.metaloom.poc.db.PocGroupDao;
import io.metaloom.poc.server.RESTException;
import io.metaloom.poc.server.crud.CrudHandler;
import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class GroupCrudHandler implements CrudHandler {

	private final PocGroupDao groupDao;
	private final AtomicLong count = new AtomicLong(0);

	@Inject
	public GroupCrudHandler(PocGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public void create(RoutingContext rc) {
		groupDao.createGroup("group_" + count.incrementAndGet())
			.subscribe(group -> {
				sendJson(rc, 201, group.toJson());
			}, err -> {
				rc.fail(err);
			});
	}

	@Override
	public void list(RoutingContext rc) {
		groupDao.loadGroups().toList().subscribe(list -> {
			JsonObject json = new JsonObject();
			JsonArray groups = new JsonArray();
			for (PocGroup group : list) {
				groups.add(group.getUuid().toString());
			}
			json.put("data", groups);
			sendJson(rc, 200, json);
		}, err -> {
			rc.fail(err);
		});
	}

	@Override
	public void read(RoutingContext rc) {
		String uuid = rc.pathParam("uuid");
		Maybe<? extends PocGroup> group = groupDao.loadGroup(UUID.fromString(uuid));
		group.switchIfEmpty(Maybe.error(RESTException.create(404)))
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
		groupDao.deleteGroup(UUID.fromString(uuid))
			.doOnDispose(() -> {
				rc.fail(RESTException.create(500));
			}).subscribe(() -> {
				sendMessage(rc, 200, "Group deleted");
			}, err -> {
				rc.fail(err);
			});
	}
}
